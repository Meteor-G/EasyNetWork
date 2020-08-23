package com.meteor.easynetwork.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * @author GLL
 * @data: 2020-08-13 14:50
 * @description
 */
public class BaseBean <T> implements Serializable {
    @Expose
    private int errorCode;
    @Expose
    private String errorMsg;
    @Expose
    private T data;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
