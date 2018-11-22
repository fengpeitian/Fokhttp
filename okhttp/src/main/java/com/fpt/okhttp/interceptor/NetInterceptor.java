package com.fpt.okhttp.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <pre>
 *   @author  : fpt
 *   e-mail  : fengfei0205@sina.com
 *   time    : 2018/07/26 18:55
 *   desc    :
 *   version : 1.0
 * </pre>
 */

public class NetInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader("Connection","close").build();

        return chain.proceed(request);
    }
}
