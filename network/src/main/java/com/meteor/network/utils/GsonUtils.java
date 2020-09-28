package com.meteor.network.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

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

    /**
     * 转成json
     *
     * @param object
     * @return
     */
    public String GsonString(Object object) {
        String gsonString = null;
        if (getGson() != null) {
            gsonString = getGson().toJson(object);
        }
        return gsonString;
    }

    /**
     * 转成bean
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public <T> T GsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (getGson() != null) {
            t = getGson().fromJson(gsonString, cls);
        }
        return t;
    }

    /**
     * 转成list
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public <T> List<T> GsonToList(String gsonString, Class<T> cls) {
        List<T> list = null;
        if (getGson() != null) {
            list = getGson().fromJson(gsonString, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (getGson() != null) {
            list = getGson().fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    public <T> Map<String, T> GsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (getGson() != null) {
            map = getGson().fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }
}
