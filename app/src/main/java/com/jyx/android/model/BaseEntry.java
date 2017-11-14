package com.jyx.android.model;

import com.google.gson.annotations.SerializedName;

/**
 * Author : Tree
 * Date : 2016-01-07
 */
public class BaseEntry<T> {

    @SerializedName("result")
    private int result;

    @SerializedName("msg")
    private String msg;

    @SerializedName("datas")
    private T data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
