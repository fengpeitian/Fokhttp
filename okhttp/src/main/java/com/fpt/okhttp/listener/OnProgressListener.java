package com.fpt.okhttp.listener;

/**
 * 流读写进度
 */
public interface OnProgressListener {

    /**
     * 进度发生了改变，如果numBytes，totalBytes，percent都为-1，则表示总大小获取不到
     *
     * @param numBytes   已读/写大小
     * @param totalBytes 总大小
     * @param percent    百分比
     */
    void onProgressChanged(long numBytes, long totalBytes, float percent);
}