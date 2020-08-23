package com.meteor.easynetwork.bean;

import com.google.gson.annotations.Expose;

/**
 * @author GLL
 * @data: 2020-08-13 14:56
 * @description
 */
public class WanBean {
    @Expose
    private String courseId;
    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String order;
    @Expose
    private String parentChapterId;
    @Expose
    private Boolean userControlSetTop;
    @Expose
    private String visible;

    @Override
    public String toString() {
        return "WanBean{" +
                "courseId='" + courseId + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", order='" + order + '\'' +
                ", parentChapterId='" + parentChapterId + '\'' +
                ", userControlSetTop=" + userControlSetTop +
                ", visible='" + visible + '\'' +
                '}';
    }
}
