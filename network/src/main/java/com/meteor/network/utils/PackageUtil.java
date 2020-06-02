package com.meteor.network.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * Author: GLL
 * Date: 2019/10/27 15:04
 * Description:
 */
public class PackageUtil {
    private static final String TAG = "PackageUtil";

    /**
     * 获取PackageName
     *
     * @param context
     * @return
     */
    public static String getAppPackageName(Context context) {
        try {
            final String packageName = context.getPackageName();
            return packageName;
        } catch (Exception exception) {
            return "";
        }
    }

    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            String pkName = context.getPackageName();
            verCode = context.getPackageManager().getPackageInfo(pkName, 0).versionCode;
        } catch (NameNotFoundException e) {
            LogUtils.e(TAG, e.getMessage());
        }
        return verCode;
    }

    public static String getVerName(Context context) {
        String verName = "";
        try {
            String pkName = context.getPackageName();
            verName = context.getPackageManager().getPackageInfo(pkName, 0).versionName;
        } catch (NameNotFoundException e) {
            LogUtils.e(TAG, e.getMessage());
        }
        return verName;

    }
}
