package com.meteor.network.subscriber;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.meteor.network.HttpManager;
import com.meteor.network.callback.HttpCallback;
import com.meteor.network.utils.GsonUtils;
import com.meteor.network.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * @author GLL
 * @data: 2020-08-08 13:56
 * @description
 */
public class CommonResultSubscriber<T extends ResponseBody> extends Subscriber<T> {

    public static final String TAG = "CommonResultSubscriber";

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

    private HttpCallback httpCallback;

    /**
     * 回调
     *
     * @param callback
     */
    public CommonResultSubscriber(HttpCallback callback) {
        this.httpCallback = callback;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!NetworkUtils.isNetworkConnected(HttpManager.getInstance().getContext())) {
            onException(NETWORK_ERROR, "网络连接失败");
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof HttpException) {
            //   HTTP错误
            onException(BAD_NETWORK, e.getMessage());
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {
            //   连接错误
            onException(CONNECT_ERROR, e.getMessage());
        } else if (e instanceof InterruptedIOException) {
            //  连接超时
            onException(CONNECT_TIMEOUT, e.getMessage());
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //  解析错误
            onException(PARSE_ERROR, e.getMessage());
            e.printStackTrace();
        } else {
            if (e != null) {
                onException(NOT_TRUE_OVER, e.toString());
            } else {
                onException(NOT_TRUE_OVER, "未知错误");
            }
        }
    }

    @Override
    public void onNext(T t) {
        if (t.contentLength() == 0) {
            return;
        }
        if (httpCallback != null) {
            boolean returnJson = false;
            Type genericityType = httpCallback.getGenericityType();
            if (genericityType instanceof Class) {
                switch (((Class) genericityType).getSimpleName()) {
                    case "Object":
                    case "String":
                        returnJson = true;
                        break;
                    default:
                        break;
                }
            }

            if (returnJson) {
                try {
                    httpCallback.onResolve(t.string());
                } catch (IOException e) {
                    onException(NOT_TRUE_OVER, e.getMessage());
                    e.printStackTrace();
                }
            } else {
                try {
                    this.httpCallback.onResolve(GsonUtils.getMananger().getGson().fromJson(t.string(), genericityType));
                } catch (Exception e) {
                    onException(NOT_TRUE_OVER, e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private void onException(int unknownError, String message) {
        switch (unknownError) {
            case CONNECT_ERROR:
                httpCallback.onFailed(unknownError, "连接错误" + message);
                break;
            case CONNECT_TIMEOUT:
                httpCallback.onFailed(unknownError, "连接超时" + message);
                break;
            case BAD_NETWORK:
                httpCallback.onFailed(unknownError, "网络超时" + message);
                break;
            case PARSE_ERROR:
                httpCallback.onFailed(unknownError, "数据解析失败" + message);
                break;
            //非true的所有情况
            case NOT_TRUE_OVER:
                httpCallback.onFailed(unknownError, message);
                break;
            default:
                break;
        }
    }
}
