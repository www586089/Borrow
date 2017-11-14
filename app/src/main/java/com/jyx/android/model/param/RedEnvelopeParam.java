package com.jyx.android.model.param;

/**
 * Created by Administrator on 3/23/2016.
 */
public class RedEnvelopeParam {
    /**
     * {"function": "personalredpacket","userid":"132512379702379520",
     * "password":"tt","targetuser" :"153561152041812992","amount":"99",
     * "note":"测试"}
     */
    String function;
    String userid;
    String password;
    String targetuser;
    String amount;
    String note;


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


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getTargetuser() {
        return targetuser;
    }


    public void setTargetuser(String targetuser) {
        this.targetuser = targetuser;
    }


    public String getAmount() {
        return amount;
    }


    public void setAmount(String amount) {
        this.amount = amount;
    }


    public String getNote() {
        return note;
    }


    public void setNote(String note) {
        this.note = note;
    }
}
