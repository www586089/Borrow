package com.jyx.android.model;

public class WXPrePayBean {
    private String amount;
    private String orderid;
    private String prepayid;
    private String payjson;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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
