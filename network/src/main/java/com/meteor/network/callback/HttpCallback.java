package com.meteor.network.callback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author GLL
 * @data: 2020-08-07 18:57
 * @description
 */
public abstract class HttpCallback<T> {

    public static final String TAG = "HttpCallback";

    protected Type genericityType;

    public HttpCallback() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            this.genericityType = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        } else {
            this.genericityType = Object.class;
        }
    }

    public abstract void onResolve(T data);

    public abstract void onFailed(int errCode, String msg);

    public Type getGenericityType() {
        return genericityType;
    }

}
