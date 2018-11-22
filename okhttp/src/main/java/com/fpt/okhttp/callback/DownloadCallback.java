package com.fpt.okhttp.callback;

import android.os.Handler;
import android.os.Looper;

import com.fpt.okhttp.listener.OnDownloadListener;
import com.fpt.okhttp.listener.OnProgressListener;
import com.fpt.okhttp.progress.DownloadResponseBody;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * 下载的回调
 */

public class DownloadCallback implements Callback {
    private Handler mHandler;//将子线程数据转到主线程中
    private OnDownloadListener mListener;
    private String filePath;

    /**
     *
     * @param filePath   输出的文件本地路径
     * @param mListener
     */
    public DownloadCallback(String filePath, OnDownloadListener mListener) {
        this.filePath = filePath;
        this.mListener = mListener;
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(e);
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        File outFile = new File(filePath);
        if (outFile.exists()) {
            if (outFile.length() == 0) {
                outFile.delete();
            }else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onSuccess();
                    }
                });
                return;
            }
        }
        outFile.getParentFile().mkdirs();
        outFile.createNewFile();

        ResponseBody responseBody = new DownloadResponseBody(response.body(), new OnProgressListener() {
            @Override
            public void onProgressChanged(final long numBytes, final long totalBytes, final float percent) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onProgressChanged(numBytes,totalBytes,percent);
                    }
                });
            }
        });

        BufferedSource source = responseBody.source();
        BufferedSink sink = Okio.buffer(Okio.sink(outFile));
        source.readAll(sink);
        sink.flush();
        source.close();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onSuccess();
            }
        });

    }

}
