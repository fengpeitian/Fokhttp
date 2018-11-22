package com.fpt.okhttp.progress;

import com.fpt.okhttp.listener.OnProgressListener;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;

/**
 * 带进度响应体
 */
public class DownloadResponseBody extends ResponseBody {
    private ResponseBody responseBody;
    private OnProgressListener progressListener;
    private BufferedSource progressSource;


    public DownloadResponseBody(ResponseBody responseBody, OnProgressListener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }


    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }


    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }


    @Override
    public BufferedSource source() {
        if (progressListener == null) {
            return responseBody.source();
        }
        ProgressInputStream progressInputStream = new ProgressInputStream(responseBody.source().inputStream(), progressListener, contentLength());
        progressSource = Okio.buffer(Okio.source(progressInputStream));
        return progressSource;
    }

    @Override
    public void close() {
        if (progressSource != null) {
            try {
                progressSource.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}