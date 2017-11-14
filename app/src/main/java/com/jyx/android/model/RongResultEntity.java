package com.jyx.android.model;

/**
 * 融云的返回数据
 * Created by gaobo on 2015/12/13.
 * {"code":200,"userId":"5614c36360b203b08f7345dc","token":"FJxsALl3Df5KMsu
 * kx+v8nUwV3zwjZk6qsi4KWNt1WQMCutjwA1yYLVwW94s5Rj4H8ZEGsPjukGaGWYNKbhW2a0615g2x/19eO+vdjqU119EPAPWzDIlEVQ=="}
 */
public class RongResultEntity {

    private  String code;


    private  String userId;
    private String token;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
