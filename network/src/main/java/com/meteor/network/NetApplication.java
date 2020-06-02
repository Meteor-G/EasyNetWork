package com.meteor.network;

import android.app.Application;

/**
 * @author GLL
 * @data: 2020-06-02 15:42
 * @description
 */
public class NetApplication  {
    private static Application instance;

    public static void initConfig(final Application _instance) {
        instance = _instance;
    }

    public static Application getInstance() {
        return instance;
    }
}
