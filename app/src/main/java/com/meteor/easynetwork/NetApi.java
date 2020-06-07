package com.meteor.easynetwork;

import com.meteor.network.NetCreator;
import com.meteor.network.base.BaseObserver;

import java.util.WeakHashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author GLL
 * @data: 2020-06-02 21:35
 * @description
 */
public class NetApi {
    private NetApi() {

    }

    private static final class NetApiHolder {
        private static final NetApi REST_API = new NetApi();
    }

    public static final NetApi getInstance() {
        return NetApiHolder.REST_API;
    }

    public void get(String url, WeakHashMap<String, Object> params, BaseObserver observer) {
        NetCreator.getNetService().get(url, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void post(String url, WeakHashMap<String, Object> params, BaseObserver observer) {
        NetCreator.getNetService().post(url, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
