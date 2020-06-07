package com.meteor.network.base;

import android.net.ParseException;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.meteor.network.utils.GsonUtils;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;

/**
 * @author GLL
 * @data: 2020-06-02 21:36
 * @description
 */
public abstract class BaseObserver implements Observer<String> {

    /**
     * 网络连接失败  无网
     */
    private static final int NETWORK_ERROR = 100000;
    /**
     * 解析数据失败
     */
    private static final int PARSE_ERROR = 1008;
    /**
     * 网络问题
     */
    private static final int BAD_NETWORK = 1007;
    /**
     * 连接错误
     */
    private static final int CONNECT_ERROR = 1006;
    /**
     * 连接超时
     */
    private static final int CONNECT_TIMEOUT = 1005;
    /**
     * 非 true的所有情况
     */
    private static final int NOT_TRUE_OVER = 1004;

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            //   HTTP错误
            onException(BAD_NETWORK, "");
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {
            //   连接错误
            onException(CONNECT_ERROR, "");
        } else if (e instanceof InterruptedIOException) {
            //  连接超时
            onException(CONNECT_TIMEOUT, "");
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //  解析错误
            onException(PARSE_ERROR, "");
            e.printStackTrace();
        } else {
            if (e != null) {
                onError(e.toString());
            } else {
                onError("未知错误");
            }
        }
    }


    @Override
    public void onNext(String data) {
        onSuccess(data);
    }

//    @Override
//    public void onNext(BaseBean<T> tBaseBean) {
//        try {
//            if (tBaseBean.getErrorCode() == 0) {
//                onSuccess(tBaseBean.getData());
//            } else {
//                onError(tBaseBean.getMsg());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            onError(e.toString());
//        }
//    }

    private void onException(int unknownError, String message) {
        switch (unknownError) {
            case CONNECT_ERROR:
                onError("连接错误");
                break;
            case CONNECT_TIMEOUT:
                onError("连接超时");
                break;
            case BAD_NETWORK:
                onError("网络超时");
                break;
            case PARSE_ERROR:
                onError("数据解析失败");
                break;
            //非true的所有情况
            case NOT_TRUE_OVER:
                onError(message);
                break;
            default:
                break;
        }
    }

    public abstract void onSuccess(String data);

    public abstract void onError(String msg);
}

