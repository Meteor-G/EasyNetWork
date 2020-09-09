package com.meteor.network.interceptor;


import android.util.Log;

import com.meteor.network.HttpManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author GLL
 * @data: 2020-06-02 10:57
 * @description
 */
public class HeaderInterceptor implements Interceptor {

    private Map<String, String> headers;

    public HeaderInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder builder = chain.request().newBuilder();
        for (String key : headers.keySet()) {
            builder.addHeader(key, headers.get(key));
        }

        Request request = builder.build();
        return chain.proceed(request);
    }
}
