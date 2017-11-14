package com.jyx.android.model.param;

/**
 * Created by Administrator on 3/24/2016.
 */
public class QueryRedEnvelopParam {
/**
 * 查询个人红包状态
 {"function":"getpersonalredpacket","userid":"132512379702379520","redpacketid":""}
 */
    String function;
    String userid;
    String redpacketid;


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


    public String getRedpacketid() {
        return redpacketid;
    }


    public void setRedpacketid(String redpacketid) {
        this.redpacketid = redpacketid;
    }
}
