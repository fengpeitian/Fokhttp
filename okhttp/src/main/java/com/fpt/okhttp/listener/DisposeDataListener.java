package com.fpt.okhttp.listener;

import com.fpt.okhttp.exception.OkHttpException;

/**
 * Created by FPT.
 * 自定义监听事件
 */

public interface DisposeDataListener {

    //请求成功回调事件处理
    void onSuccess(String json);

    //请求失败回调事件处理
    void onFailure(OkHttpException exception);
}
