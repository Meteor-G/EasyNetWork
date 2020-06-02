package com.meteor.network;

import com.meteor.network.interceptor.BaseInterceptor;
import com.meteor.network.interceptor.HeaderInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author GLL
 * @data: 2020-06-02 10:49
 * @description
 */
public class NetCreator {

    private static String BASE_URL = "";

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }


    private NetCreator() {
    }

    public static NetService getNetService() {
        return NetServiceHolder.NET_SERVICE;

    }

    public static final class NetServiceHolder {
        private static final NetService NET_SERVICE = RetrofitHolder.RETROFIT_CLIENT.create(NetService.class);
    }

    /**
     * 创建Retrofit
     */
    private static final class RetrofitHolder {
        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(ScalarsConverterFactory.create())//坑   addConverterFactory有先后顺序之分,如果想获取到String,这个必须写前边
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .client(OkHttpHolder.OK_HTTP_CLIENT)
                .build();
    }

    /**
     * 创建OkHttp
     */
    private static final class OkHttpHolder {
        private static final int TIME_OUT = 60;
        private static final int READ_TIME_OUT = 20 * 1000;
        private static final OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();


        //初始化拦截器
        private static OkHttpClient.Builder addInterceptor() {
            BUILDER.addInterceptor(new HeaderInterceptor());
            BUILDER.addInterceptor(new BaseInterceptor());
//            BUILDER.addInterceptor(new ProgressInterceptor());
            return BUILDER;
        }

        private static final OkHttpClient OK_HTTP_CLIENT = addInterceptor()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true) // 失败重发
                .build();
    }
}
