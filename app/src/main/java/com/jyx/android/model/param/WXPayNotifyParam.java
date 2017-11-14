package com.jyx.android.model.param;

/**
 * Author : zfang
 * Date : 2016-02-28
 */
public class WXPayNotifyParam {
    private String function = null;
    private String userid = null;
    private String orderid = null;
    private String prepayid = null;
    private String payjson = null;

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPayjson() {
        return payjson;
    }

    public void setPayjson(String payjson) {
        this.payjson = payjson;
    }
}
