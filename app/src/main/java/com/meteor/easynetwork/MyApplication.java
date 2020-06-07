package com.meteor.easynetwork;

import android.app.Application;

import com.meteor.network.NetApplication;
import com.meteor.network.NetCreator;

/**
 * @author GLL
 * @data: 2020-06-02 21:32
 * @description
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetApplication.initConfig(this);
        NetCreator.setBaseUrl("http://418783965b51.ngrok.io/");
    }
}
