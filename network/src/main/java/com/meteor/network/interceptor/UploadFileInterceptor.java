package com.meteor.network.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author GLL
 * @data: 2020-08-07 18:58
 * @description
 */
public class UploadFileInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "multipart/form-data")
                .build();
        return chain.proceed(request);
    }

    public static UploadFileInterceptor create() {
        return new UploadFileInterceptor();
    }
}
