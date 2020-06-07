package com.meteor.network.interceptor;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author GLL
 * @data: 2020-06-02 10:57
 * @description
 */
public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request baseRequest = chain.request();
        return chain.proceed(baseRequest);
    }
}
