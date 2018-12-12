package com.fpt.okhttp.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fpt.okhttp.listener.OnProgressListener;
import com.fpt.okhttp.progress.UploadRequestBody;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by FPT.
 * 创建一个OkHttpRequest请求
 */

public class Request {

    /**
     * 创建一个post的request
     *
     * @param url    联网地址
     * @param params 参数
     */
    public static <T extends Object> okhttp3.Request createPostJsonRequest(String url, @NonNull Map<String, T> params) {
        //MediaType  设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , new Gson().toJson(params));
        return new okhttp3.Request.Builder().url(url).post(requestBody).build();
    }

    /**
     * 创建一个post的request
     *
     * @param url    联网地址
     * @param params 参数
     */
    public static okhttp3.Request createPostStringRequest(String url, @NonNull String params) {
        //MediaType  设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , params);
        return new okhttp3.Request.Builder().url(url).post(requestBody).build();
    }

    /**
     * 创建一个get的request
     *
     * @param url 联网地址
     */
    public static okhttp3.Request createGetRequest(String url) {

        return new okhttp3.Request.Builder().url(url).get().build();
    }

    /**
     * 创建一个get的request
     *
     * @param url
     * @param params
     * @return
     */
    public static okhttp3.Request createGetRequest(String url, Map<String, String> params) {

        StringBuilder urlBuilder = new StringBuilder(url).append("?");

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }

        return new okhttp3.Request.Builder().url(urlBuilder.substring(0, urlBuilder.length() - 1)).get().build();
    }

    /**
     * 创建一个post的request
     *
     * @param url    联网地址
     * @param params 参数
     */
    public static <T extends Object> okhttp3.Request createPostRequest(String url, Map<String,T> params) {

        FormBody.Builder mFormBodyBuilder = new FormBody.Builder();

        if (params != null) {
            for (Map.Entry<String,T> entry : params.entrySet()) {
                //将请求参数遍历添加到请求构建类中
                mFormBodyBuilder.add(entry.getKey(), entry.getValue().toString());
            }
        }

        FormBody mFormBody = mFormBodyBuilder.build();

        return new okhttp3.Request.Builder().url(url).post(mFormBody).build();
    }

    /**
     * 创建一个post的二进制断点上传的request
     *
     * @param url        上传地址
     * @param uploadFile 上传的文件
     * @param offset     断点上传的断点位置（这里的offset不能等于content.length）
     * @param listener   上传进度监听
     * @return 请求体
     */
    public static okhttp3.Request createPostUploadRequest(String url, File uploadFile,
                                                          final int offset,
                                                          OnProgressListener listener) {
        final byte[] content = file2Bytes(uploadFile);
        final MediaType contentType = MediaType.parse("application/octet-stream");
        final int contentLength = content.length;
        final int byteCount = contentLength - offset;

        RequestBody requestBody = new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() throws IOException {
                return contentLength;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.write(content, offset, byteCount);//写byte数组，从offset开始，写byteCount长度
            }
        };

        RequestBody uploadBody = new UploadRequestBody(requestBody, listener);

        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        // 参数分别为， 请求key ，文件名称 ， RequestBody
        body.addFormDataPart("file", uploadFile.getName(), uploadBody);
        return new okhttp3.Request.Builder()
                .url(url)
                .addHeader("Content-Range", offset + "")//请求头
                .post(body.build())
                .build();
    }

    /**
     * 文件转字节
     *
     * @param file 文件
     * @return 字节流
     */
    private static byte[] file2Bytes(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 4];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

}
