package com.fpt.okhttp.progress;

import com.fpt.okhttp.listener.OnProgressListener;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 带进度的输出流
 */
public class ProgressOutputStream extends OutputStream {
    private final OutputStream stream;
    private final OnProgressListener listener;

    private long total;
    private long totalWritten;

    public ProgressOutputStream(OutputStream stream, OnProgressListener listener, long total) {
        this.stream = stream;
        this.listener = listener;
        this.total = total;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.stream.write(b, off, len);
        if (this.total < 0) {
            this.listener.onProgressChanged(-1, -1, -1);
            return;
        }
        if (len < b.length) {
            this.totalWritten += len;
        } else {
            this.totalWritten += b.length;
        }
        this.listener.onProgressChanged(this.totalWritten, this.total, (this.totalWritten * 1.0F) / this.total);
    }

    @Override
    public void write(int b) throws IOException {
        this.stream.write(b);
        if (this.total < 0) {
            this.listener.onProgressChanged(-1, -1, -1);
            return;
        }
        this.totalWritten++;
        this.listener.onProgressChanged(this.totalWritten, this.total, (this.totalWritten * 1.0F) / this.total);
    }

    @Override
    public void close() throws IOException {
        if (this.stream != null) {
            this.stream.close();
        }
    }

    @Override
    public void flush() throws IOException {
        if (this.stream != null) {
            this.stream.flush();
        }
    }
}
