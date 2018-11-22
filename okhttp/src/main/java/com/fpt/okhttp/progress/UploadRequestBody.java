package com.fpt.okhttp.progress;

import android.support.annotation.Nullable;

import com.fpt.okhttp.listener.OnProgressListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;

/**
 * 上传RequestBody的构建
 */

public class UploadRequestBody extends RequestBody{
    private final RequestBody requestBody;
    private OnProgressListener progressListener;

    public UploadRequestBody(RequestBody requestBody, OnProgressListener listener) {
        this.requestBody = requestBody;
        this.progressListener = listener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (progressListener == null) {
            requestBody.writeTo(sink);
            return;
        }
        ProgressOutputStream progressOutputStream = new ProgressOutputStream(sink.outputStream(), progressListener, contentLength());
        BufferedSink progressSink = Okio.buffer(Okio.sink(progressOutputStream));
        requestBody.writeTo(progressSink);
        progressSink.flush();
    }

}
