package com.jyx.android.model.param;

import com.google.gson.annotations.SerializedName;

/**
 * Author : Tree
 * Date : 2016-01-24
 */
public class ContactMobileEntity {
    @SerializedName("name")
    private String name;

    @SerializedName("mobile")
    private String mobile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "ContactMobileEntity{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
