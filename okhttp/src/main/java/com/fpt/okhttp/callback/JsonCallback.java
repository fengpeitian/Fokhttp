package com.fpt.okhttp.callback;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.fpt.okhttp.exception.OkHttpException;
import com.fpt.okhttp.listener.DisposeDataListener;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by FPT.
 * 创建一个JsonCallback,回调直接在主线程，可以更新UI
 */

public class JsonCallback implements Callback {

    /**
     * 自定义异常code
     */
    protected final int NETWORK_ERROR = -1;
    /**
     * 自定义异常message
     */
    protected final String EMPTY_MSG = "发生未知错误!";
    /**
     * 将子线程数据转到主线程中
     */
    private Handler mHandler;
    /**
     * 回调
     */
    private DisposeDataListener mListener;
    /**
     * 错误代码
     */
    private int mErrorCode;
    /**
     * 错误信息
     */
    private String mErrorMsg;

    public JsonCallback() {
        this(null);
    }

    public JsonCallback(DisposeDataListener mListener) {
        this.mListener = mListener;
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    public DisposeDataListener getListener() {
        return mListener;
    }

    public void setListener(DisposeDataListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        mErrorCode = NETWORK_ERROR;
        // 连接异常
        if (e instanceof ConnectException ||
                e instanceof UnknownHostException) {
            mErrorMsg = "您的网络不太给力";
        } else {
            mErrorMsg = "未知错误";
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(mErrorCode, mErrorMsg));
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        final boolean isSuccess = response.isSuccessful();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(result) && isSuccess) {
                    mListener.onSuccess(result);
                } else {
                    mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
                }
            }
        });
    }

}
