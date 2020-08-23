package com.meteor.network.callback;

/**
 * @author GLL
 * @data: 2020-08-07 18:56
 * @description
 */
public interface FileResponseResult {
    void onSuccess();

    void onFailure(Throwable throwable, String content);
}
