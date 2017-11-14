package com.jyx.android.model.param;

import com.google.gson.annotations.SerializedName;

/**
 * Author : Tree
 * Date : 2016-01-07
 */
public class SignParam {
    @SerializedName("function")
    private String function;

    @SerializedName("username")
    private String userName;

    @SerializedName("password")
    private String password;

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "SignParam{" +
                "function='" + function + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
