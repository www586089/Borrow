package com.jyx.android.model.param;

/**
 * Author : zfang
 * Date : 2016-01-24
 */
public class GetFriendNewsParam {
    private String function = null;
    private String userid = null;
    private int pagenumber = -1;

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

    public int getPagenumber() {
        return pagenumber;
    }

    public void setPagenumber(int pagenumber) {
        this.pagenumber = pagenumber;
    }
}
