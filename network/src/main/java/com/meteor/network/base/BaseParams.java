package com.meteor.network.base;

import com.meteor.network.NetApplication;
import com.meteor.network.utils.DeviceUtils;
import com.meteor.network.utils.PackageUtil;

import java.util.HashMap;

/**
 * @author GLL
 * @data: 2020-06-02 15:40
 * @description
 */
public class BaseParams {
    /**
     * 基础参数
     */
    public static HashMap<String, String> basicsParams() {
        HashMap<String, String> basics = new HashMap<>();
        //imei
        basics.put("imei", DeviceUtils.getIMEI(NetApplication.getInstance()));
        //获取系统当前可用内存大小
        basics.put("memory", DeviceUtils.getAvailMemory(NetApplication.getInstance()));
        //获取 MAC 地址
        basics.put("mac", DeviceUtils.getMacAddress(NetApplication.getInstance()));
        //获取手机网络运营商类型
        basics.put("phoneIsp", DeviceUtils.getPhoneISP(NetApplication.getInstance()));
        //手机名称
        basics.put("deviceName", DeviceUtils.getDeviceName());
        //手机版本
        basics.put("deviceSdk", String.valueOf(DeviceUtils.getDeviceSDK()));
        //获取AndroidId
        basics.put("aid", DeviceUtils.getAndroidId(NetApplication.getInstance()));
        //获取AndroidId
        basics.put("pkn", PackageUtil.getAppPackageName(NetApplication.getInstance()));
        //获取版本名
        basics.put("verName", PackageUtil.getVerName(NetApplication.getInstance()));
        //获取版本号
        basics.put("verCode", String.valueOf(PackageUtil.getVerCode(NetApplication.getInstance())));
        return basics;
    }
}
