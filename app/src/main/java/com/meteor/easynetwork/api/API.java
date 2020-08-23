package com.meteor.easynetwork.api;

import android.text.TextUtils;


import com.meteor.easynetwork.bean.UserForLogin;
import com.meteor.network.HttpManager;
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

    public static void testGet(HttpCallback callback) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("aaa", "adafd");
        HttpManager.get(UrlConfig.USER_INFO, null, callback);
    }

    public static void testPost(String name, String password, HttpCallback callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("password", password);
        HttpManager.post(UrlConfig.USER_LOGIN, parameters, callback);
    }

    public static void testPost(UserForLogin userForLogin, HttpCallback callback) {
        HttpManager.postByBody(UrlConfig.USER_LOGIN_BY_BODY, userForLogin, callback);
    }

    public static void testSingleFileUpload(String url, String filePath, String fileDes, FileResponseResult fileResponseResult) {
        HttpManager.uploadFile(url, filePath, fileDes, true, fileResponseResult);
    }

    public static void testMultipleFileUpload(String url, List<String> filePathList, FileResponseResult fileResponseResult) {
        HttpManager.uploadFiles(url, filePathList, true, fileResponseResult);
    }

    /**
     * 获取电影列表
     *
     * @param start
     * @param count
     * @param city
     * @param callback
     */
    public static void getMovieList(int start, int count, String city, HttpCallback callback) {
        Map<String, Object> parameters = new HashMap<>();
        if (start > 0) {
            parameters.put("start", start);
        }
        if (count > 0) {
            parameters.put("count", count);
        }
        if (!TextUtils.isEmpty(city)) {
            parameters.put("city", city);
        }
        HttpManager.getFullPath(UrlConfig.DOUBAN_MOVIE_IN_THEATERS, parameters, callback);
    }
}
