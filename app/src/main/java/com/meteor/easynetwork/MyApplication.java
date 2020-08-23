package com.meteor.easynetwork;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.meteor.network.HttpManager;
import com.meteor.network.HttpSlot;

import java.util.HashMap;

/**
 * @author GLL
 * @data: 2020-06-02 21:32
 * @description
 */
public class MyApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        HashMap<String, String> params = new HashMap<>();
        params.put("123", "123");
        params.put("123456", "123456");
        HashMap<String, String> header = new HashMap<>();
        header.put("header1", "123");
        header.put("header2", "123456");
        HttpSlot httpSlot = new HttpSlot.Builder()
//                .setBaseUrl("https://www.wanandroid.com")
                .setBaseUrl("http://192.168.18.7:8080")
                .setReadTimeOut(10 * 1000)
                .setWriteTimeOut(10 * 1000)
                .setConnectTimeout(10 * 1000)
                .addCommonParams(params)
                .addCommonHeaders(header)
                .build();
        Log.e("gll", httpSlot.toString());
        HttpManager.init(this, httpSlot);
    }

    /**
     * 获取全局上下文
     */
    public static Context getContext() {
        return mContext;
    }

}
