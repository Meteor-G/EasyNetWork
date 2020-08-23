package com.meteor.network.listener;

/**
 * @author GLL
 * @data: 2020-08-07 18:59
 * @description
 */
public interface TransformProgressListener {
    void onProgress(long contentRead, long contentLength, boolean completed);

    void onFailed(String msg);
}
