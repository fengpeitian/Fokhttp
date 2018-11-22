package com.fpt.okhttp.listener;

/**
 * <pre>
 *   @author  : fpt
 *   e-mail  : fengfei0205@sina.com
 *   time    : 2018/07/26 18:55
 *   desc    :
 *   version : 1.0
 * </pre>
 */

public interface OnDownloadListener extends OnProgressListener{
    //下载成功回调事件处理
    void onSuccess();

    //下载失败回调事件处理
    void onFailure(Exception exception);
}
