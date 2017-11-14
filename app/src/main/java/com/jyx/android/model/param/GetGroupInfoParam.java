package com.jyx.android.model.param;

/**
 * Author : zfang
 * Date : 2016-03-07
 */
public class GetGroupInfoParam {
    private String function = null;
    private String userid = null;
    private String groupid = null;
    private int pagenumber;

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

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public int getPagenumber() {
        return pagenumber;
    }

    public void setPagenumber(int pagenumber) {
        this.pagenumber = pagenumber;
    }
}
