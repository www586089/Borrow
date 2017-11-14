package com.jyx.android.model.param;

/**
 * Author : zfang
 * Date : 2016-02-02
 */
public class GetItemInfoParam {
    private String function;
    private String userid;
    private String itemid;

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

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }
}
