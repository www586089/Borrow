package com.jyx.android.model.param;

/**
 * Author : zfang
 * Date : 2016-03-12
 */
public class GetActivityParam {
    private String function = null;
    private String userid = null;
    private String pagenumber = null;

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

    public String getPagenumber() {
        return pagenumber;
    }

    public void setPagenumber(String pagenumber) {
        this.pagenumber = pagenumber;
    }
}
