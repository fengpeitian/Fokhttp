package com.fpt.fokhttp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fpt.okhttp.FokHttpClient;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Request;

public class MainActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    private String url = "https://service.51bjhzy.com/api/Configuration/getConfiguration";

    private NetworkStatusEvent event = new NetworkStatusEvent();

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateReturnView(NetworkStatusEvent event) {
        this.event.fill(event);

        updateView(event);
    }

    private void updateView(NetworkStatusEvent event) {
        if (event.isConnect()){
            tv_title.setText(String.format("已未连接网络，网络类型为%s",event.getNetworkType().name()));
        }else {
            tv_title.setText("未连接网络");
        }
    }

    @OnClick({R.id.bt_1, R.id.bt_2, R.id.bt_3, R.id.bt_4, R.id.bt_5, R.id.bt_6, R.id.bt_7})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.bt_1:
                Request string_request = com.fpt.okhttp.request.Request.createGetRequest(url);
                Observable<String> stringObservable = FokHttpClient.sendRequest(string_request);
                stringObservable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            private Disposable disposable;

                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable = d;
                            }

                            @Override
                            public void onNext(String s) {
                                Log.d("MainActivity","onSucceed:"+s);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("MainActivity","onError:"+e.getMessage());
                                if (!disposable.isDisposed()){
                                    disposable.dispose();
                                }
                            }

                            @Override
                            public void onComplete() {
                                if (!disposable.isDisposed()){
                                    disposable.dispose();
                                }
                            }
                        });
                break;
            case R.id.bt_2:
                Request t_request = com.fpt.okhttp.request.Request.createGetRequest(url);
                Observable<String> configBeanObservable = FokHttpClient.sendRequest(t_request);
                configBeanObservable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            private Disposable disposable;

                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable = d;
                            }

                            @Override
                            public void onNext(String json) {
                                ConfigBean configBean = new Gson().fromJson(json,ConfigBean.class);
                                Log.d("MainActivity","onSucceed:"+configBean);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("MainActivity","onError:"+e.getMessage());
                                if (!disposable.isDisposed()){
                                    disposable.dispose();
                                }
                            }

                            @Override
                            public void onComplete() {
                                if (!disposable.isDisposed()){
                                    disposable.dispose();
                                }
                            }
                        });
                break;
            case R.id.bt_3:

                break;
            case R.id.bt_4:

                break;
            case R.id.bt_5:

                break;
            case R.id.bt_6:

                break;
            case R.id.bt_7:

                break;
            default:
                break;
        }
    }

}
