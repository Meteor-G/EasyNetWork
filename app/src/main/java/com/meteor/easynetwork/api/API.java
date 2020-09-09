package com.meteor.easynetwork.api;

import android.text.TextUtils;


import com.meteor.easynetwork.bean.UserForLogin;
import com.meteor.network.HttpManager;
import com.meteor.network.HttpSlot;
import com.meteor.network.callback.FileResponseResult;
import com.meteor.network.callback.HttpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.Url;

/**
 * Created by lipingfa on 2017/6/12.
 */
public class API {

    private static HttpSlot getHttpSlot() {
        HashMap<String, String> params = new HashMap<>();
        params.put("params1111", "123");
        params.put("params21111", "123456");
        HashMap<String, String> header = new HashMap<>();
        header.put("header11111", "headerrrrr");
        header.put("header21111", "headerrrrr1");
        return new HttpSlot.Builder()
//                .setBaseUrl("https://www.wanandroid.com")
                .setBaseUrl("http://192.168.0.120:8080")
                .setReadTimeOut(10 * 1000)
                .setWriteTimeOut(10 * 1000)
                .setConnectTimeout(10 * 1000)
                .addCommonParams(params)
                .addCommonHeaders(header)
                .build();

    }

    public static void testGet(HttpCallback callback) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("aaa", "adafd");
        HttpManager.getInstance().get(getHttpSlot(), UrlConfig.USER_INFO, null, callback);
    }

    public static void testPost(String name, String password, HttpCallback callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("password", password);
        HttpManager.getInstance().post(UrlConfig.USER_LOGIN, parameters, callback);
    }

    public static void testPost(UserForLogin userForLogin, HttpCallback callback) {
        HttpManager.getInstance().postByBody(UrlConfig.USER_LOGIN_BY_BODY, userForLogin, callback);
    }

    public static void testSingleFileUpload(String url, String filePath, String fileDes, FileResponseResult fileResponseResult) {
        HttpManager.getInstance().uploadFile(url, filePath, fileDes, true, fileResponseResult);
    }

    public static void testMultipleFileUpload(String url, List<String> filePathList, FileResponseResult fileResponseResult) {
        HttpManager.getInstance().uploadFiles(url, filePathList, true, fileResponseResult);
    }
}
