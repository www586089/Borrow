package com.jyx.android.model.param;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author : Tree
 * Date : 2016-01-24
 */
public class ContactUploadParam {
    @SerializedName("function")
    private String function;

    @SerializedName("userid")
    private String userId;

    @SerializedName("mobilejson")
    private List<ContactMobileEntity> datas;

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<ContactMobileEntity> getDatas() {
        return datas;
    }

    public void setDatas(List<ContactMobileEntity> datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return "ContactUploadParam{" +
                "function='" + function + '\'' +
                ", userId='" + userId + '\'' +
                ", datas=" + datas +
                '}';
    }
}
