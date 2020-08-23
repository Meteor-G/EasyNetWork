package com.meteor.network.upload;

import android.content.Context;
import android.text.TextUtils;

import com.meteor.network.callback.FileResponseResult;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * @author GLL
 * @data: 2020-08-07 19:01
 * @description
 */
public class UploadSubscriber<T extends ResponseBody> extends Subscriber<T> {

    private FileResponseResult callback;

    /**
     * 回调
     *
     * @param callback
     */
    public UploadSubscriber(FileResponseResult callback) {
        this.callback = callback;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCompleted() {
        callback.onSuccess();
    }

    @Override
    public void onError(Throwable e) {
        callback.onFailure(e, "服务器错误");
    }

    @Override
    public void onNext(T t) {
        if (t.contentLength() == 0) {
            return;
        }
    }
}
