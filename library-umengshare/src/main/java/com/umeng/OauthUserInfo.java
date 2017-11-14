package com.umeng;

import java.io.Serializable;

/**
 * 第三方授权用户信息
 * Author : Tree
 * Date : 2015-12-30
 */
public class OauthUserInfo implements Serializable {
    private String uid;

    private String token;

    private String expiresTime;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(String expiresTime) {
        this.expiresTime = expiresTime;
    }
}
