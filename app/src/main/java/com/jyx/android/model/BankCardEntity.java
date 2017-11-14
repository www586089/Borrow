package com.jyx.android.model;

/**
 * Created by yiyi on 2016/1/14.
 */
public class BankCardEntity {

    /**
     * user_id : 132512379702379520
     * cardname : 农业银行.金穗通宝卡（个人普卡）
     * openbank :
     * cardno : 6228482898203884775
     * account : mm
     */

    private String user_id;
    private String cardname;
    private String openbank;
    private String cardno;
    private String account;

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public void setOpenbank(String openbank) {
        this.openbank = openbank;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCardname() {
        return cardname;
    }

    public String getOpenbank() {
        return openbank;
    }

    public String getCardno() {
        return cardno;
    }

    public String getAccount() {
        return account;
    }
}
