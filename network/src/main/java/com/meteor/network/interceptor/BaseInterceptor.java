package com.meteor.network.interceptor;


import com.meteor.network.base.BaseParams;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author GLL
 * @data: 2020-06-02 10:56
 * @description
 */
public class BaseInterceptor implements Interceptor {

    //声明响应对象
    private Response response;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request baseRequest = chain.request();

        //获取请求的方法
        String method = baseRequest.method();
        if ("GET".equalsIgnoreCase(method)) {
            HttpUrl.Builder builder = baseRequest.url().newBuilder();
            for (Map.Entry<String, String> parameter : BaseParams.basicsParams().entrySet()) {
                builder.addQueryParameter(parameter.getKey(), parameter.getValue());
            }

            HttpUrl httpUrl = builder.build();
            //获取添加公共参数之后的requset对象
            Request request = new Request.Builder().url(httpUrl).build();
            //发送拼接完成后的请求
            response = chain.proceed(request);
        } else if ("POST".equalsIgnoreCase(method)) {
            RequestBody requestBody = baseRequest.body();
            if (requestBody instanceof FormBody) {
                FormBody.Builder builder = new FormBody.Builder();
                FormBody originalFormBody = (FormBody) baseRequest.body();
                for (int i = 0; i < originalFormBody.size(); i++) {
                    builder.add(originalFormBody.name(i), originalFormBody.value(i));
                }
                for (Map.Entry<String, String> parameter : BaseParams.basicsParams().entrySet()) {
                    builder.add(parameter.getKey(), parameter.getValue());
                }
                FormBody formBody = builder.build();
                Request request = baseRequest.newBuilder().post(formBody).build();
                response = chain.proceed(request);
            } else {
                response = chain.proceed(baseRequest);
            }
        }
        return response;
    }
}
