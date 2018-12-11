package com.fpt.okhttp.exception;

/**
 * Created by FPT.
 * 自定义异常
 */

public class OkHttpException extends Exception{
    private static final long serialVersionUID = 1L;

    private int error_code;
    private String error_msg;

    public OkHttpException(int error_code, String error_msg) {
        this.error_code = error_code;
        this.error_msg = error_msg;
    }

    public int getErrorCode() {
        return error_code;
    }

    @Override
    public String getMessage() {
        return error_msg;
    }

}
