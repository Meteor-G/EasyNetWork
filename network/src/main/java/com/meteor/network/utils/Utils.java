package com.meteor.network.utils;

import android.os.Looper;

/**
 * @author GLL
 * @data: 2020-08-09 13:19
 * @description
 */
public class Utils {
    public static <T> T checkNotNull(T t, String message) {
        if (t == null) {
            throw new NullPointerException(message);
        }
        return t;
    }

    public static boolean checkMain() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

}
