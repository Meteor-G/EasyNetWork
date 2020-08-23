package com.meteor.easynetwork.api.callback;

import android.widget.Toast;

import com.meteor.easynetwork.MyApplication;
import com.meteor.network.callback.HttpCallback;


/**
 * @Description: Created by jadyli on 2017/5/4.
 */
public abstract class CommonCallback<T> extends HttpCallback<T> {
    @Override
    public void onResolve(T t) {
        onSuccess(t);
    }

    @Override
    public void onFailed(int error_code, String error_message) {
        if (enableShowToast()) {
            Toast.makeText(MyApplication.getContext(), error_message, Toast.LENGTH_SHORT);
        }
        onFailure(error_code, error_message);
    }

    public abstract void onSuccess(T data);

    public abstract void onFailure(int error_code, String error_message);

    public boolean enableShowToast() {
        return false;
    }
}
