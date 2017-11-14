package com.jyx.android.model.param;

/**
 * Author : zfang
 * Date : 2016-03-09
 */
public class GetNearByGroupListParam {
    private String function = null;
    private String userid = null;
    private String querytype = null;

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

    public String getQuerytype() {
        return querytype;
    }

    public void setQuerytype(String querytype) {
        this.querytype = querytype;
    }
}
