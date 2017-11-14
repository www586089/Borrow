package com.jyx.android.model.param;

/**
 * Created by Administrator on 3/23/2016.
 */
public class GroupRedEnvelopParam {
    /**
     * {"function":"groupredpacket", "userid":"132512379702379520",
     "password":"tt","groupid":"153561152041812992", "amount":"99",
     "note":"测试","num":"9"}
     */
    String function;
    String userid;
    String password;
    String groupid;
    String amount;
    String note;
    String num;


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


    public String getGroupid() {
        return groupid;
    }


    public void setGroupid(String groupid) {
        this.groupid = groupid;
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


    public String getNum() {
        return num;
    }


    public void setNum(String num) {
        this.num = num;
    }
}
