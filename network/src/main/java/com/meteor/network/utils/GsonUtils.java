package com.meteor.network.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author GLL
 * @data: 2020-06-07 14:14
 * @description
 */
public class GsonUtils {
    private static GsonUtils gsonMan;

    static {
        gsonMan = new GsonUtils();
    }

    private GsonUtils() {
    }

    public static GsonUtils getMananger() {
        return gsonMan;
    }

    private Gson mGson;

    public synchronized Gson getGson() {
        if (mGson == null)
            // 不转换没有 @Expose 注解的字段
            mGson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
        return mGson;
    }
}
