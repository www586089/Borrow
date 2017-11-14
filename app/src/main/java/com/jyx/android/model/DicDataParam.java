package com.jyx.android.model;

import com.google.gson.annotations.SerializedName;

public class DicDataParam{
    @SerializedName("function")
    private String function;

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}
