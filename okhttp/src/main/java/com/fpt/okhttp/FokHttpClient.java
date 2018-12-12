package com.fpt.okhttp;

import com.fpt.okhttp.callback.DownloadCallback;
import com.fpt.okhttp.callback.JsonCallback;
import com.fpt.okhttp.exception.OkHttpException;
import com.fpt.okhttp.util.HttpsUtils;

import org.reactivestreams.Subscriber;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocketListener;

/**
 * 构建一个OkHttpClient和请求参数的配置，https支持
 * @author fengpeitian
 */
public class FokHttpClient {
    /**
     * 超时参数
     */
    private static final int TIME_CONNECT_OUT = 5;
    /**
     * 超时参数
     */
    private static final int TIME_OUT = 10;
    private static OkHttpClient mOkHttpClient;

    //配置OkHttpClient参数
    static {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        //配置 连接、读、写超时时间
        okHttpClientBuilder.connectTimeout(TIME_CONNECT_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        //允许重定向
        okHttpClientBuilder.followRedirects(true);
        //允许失败重连
        okHttpClientBuilder.retryOnConnectionFailure(true);
        //添加https支持
        okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        X509TrustManager x509TrustManager = HttpsUtils.getX509TrustManager();
        okHttpClientBuilder.sslSocketFactory(HttpsUtils.getSSLSocketFactory(x509TrustManager), x509TrustManager);
        //添加拦截器
        // okHttpClientBuilder.addInterceptor(new NetInterceptor());
        //okHttpClientBuilder.addInterceptor();

        mOkHttpClient = okHttpClientBuilder.build();
    }

    /**
     * 发送具体的http/https的请求
     *
     * @param request
     * @param callback
     * @return 返回的call对象是为了在某个页面销毁（onDestroy）的时候去及时调用call.cancel()方法
     */
    public static Call sendRequest(Request request, JsonCallback callback) {

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(callback);

        return call;
    }

    /**
     * 发送具体的http/https的请求
     *
     * @param request
     * @param callback
     * @return 返回的call对象是为了在某个页面销毁（onDestroy）的时候去及时调用call.cancel()方法
     */
    public static Call sendRequest(Request request, Callback callback) {

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(callback);

        return call;
    }

    /**
     * 发送具体的http/https的请求
     * @param request
     * @return rxJava可用的Observable对象
     */
    public static Observable<String> sendRequest(Request request) {

        final Call call = mOkHttpClient.newCall(request);
        ObservableOnSubscribe<String> source = new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException ex) {
                        if (!e.isDisposed()) {
                            e.onError(ex);
                        }
                        if (!call.isCanceled()) {
                            call.cancel();
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful() && !e.isDisposed()){
                            String json = response.body().string();
                            e.onNext(json);
                        }else {
                            if (!e.isDisposed()) {
                                e.onError(new OkHttpException(-1, "发生未知错误!"));
                            }
                            if (!call.isCanceled()) {
                                call.cancel();
                            }
                        }
                    }
                });
            }
        };

        return Observable.create(source);
    }

    /**
     * 发送具体的http/https的请求
     * @param request
     * @param strategy 背压策略 记得一定要在Subscriber中调用request()方法
     * @return rxJava可用的Flowable对象
     */
    public static Flowable<String> sendRequest(Request request,BackpressureStrategy strategy) {

        final Call call = mOkHttpClient.newCall(request);
        Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(final FlowableEmitter<String> e) throws Exception {
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException ex) {
                        if (!e.isCancelled()) {
                            e.onError(ex);
                        }
                        if (!call.isCanceled()) {
                            call.cancel();
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful() && !e.isCancelled()){
                            String json = response.body().string();
                            e.onNext(json);
                        }else {
                            if (!e.isCancelled()) {
                                e.onError(new OkHttpException(-1, "发生未知错误!"));
                            }
                            if (!call.isCanceled()) {
                                call.cancel();
                            }
                        }
                    }
                });
            }
        }, strategy);

        return flowable;
    }


    /**
     * 下载
     *
     * @param request
     * @param callback
     * @return 返回的call对象是为了在某个页面销毁（onDestroy）的时候去及时调用call.cancel()方法
     */
    public static Call sendRequest(Request request, DownloadCallback callback) {

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(callback);

        return call;
    }

    /**
     * 添加网络拦截器
     *
     * @param netInterceptor 网络拦截器
     */
    public static void setNetInterceptor(Interceptor netInterceptor) {
        mOkHttpClient = mOkHttpClient.newBuilder().addInterceptor(netInterceptor).build();
    }

    /**
     * 取消所有请求
     */
    public static void cancelAllRequest() {
        if (mOkHttpClient != null) {
            mOkHttpClient.dispatcher().cancelAll();
        }
    }

    /**
     * 设置一个WebSocket长连接
     * @param url
     * @param listener
     */
    public static void setWebSocket(String url, WebSocketListener listener){
        if (mOkHttpClient != null) {
            Request request = new Request.Builder().url(url).build();
            mOkHttpClient.newWebSocket(request, listener);
        }
    }

    /**
     * WebSocket的关闭的参数
     * https://tools.ietf.org/html/rfc6455#section-7.4
     */
    public static final int normal_closure = 1000;

}
