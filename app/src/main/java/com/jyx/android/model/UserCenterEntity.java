package com.jyx.android.model;

/**
 * Created by yiyi on 2016/1/13.
 */
public class UserCenterEntity {
    /**
     * portraituri : http://localhost:8084/JYX/ImageShow?image=132274026293789696.jpg
     * balance : 0
     * user_id : 132512379702379520
     * signature : 签名
     * nickname : mm
     * borrow : 0
     * namediscrib : 999999999
     * backgrounduri : http://localhost:8084/JYX/ImageShow?image=132274026293789696.jpg
     * follow : 0
     * fans : 1
     * rental : 0
     */

    private String portraituri;
    private String balance;
    private String user_id;
    private String signature;
    private String nickname;
    private String borrow;
    private String namediscrib;
    private String backgrounduri;
    private String follow;
    private String fans;
    private String rental;

    public void setPortraituri(String portraituri) {
        this.portraituri = portraituri;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setBorrow(String borrow) {
        this.borrow = borrow;
    }

    public void setNamediscrib(String namediscrib) {
        this.namediscrib = namediscrib;
    }

    public void setBackgrounduri(String backgrounduri) {
        this.backgrounduri = backgrounduri;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

    public void setRental(String rental) {
        this.rental = rental;
    }

    public String getPortraituri() {
        return portraituri;
    }

    public String getBalance() {
        return balance;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getSignature() {
        return signature;
    }

    public String getNickname() {
        return nickname;
    }

    public String getBorrow() {
        return borrow;
    }

    public String getNamediscrib() {
        return namediscrib;
    }

    public String getBackgrounduri() {
        return backgrounduri;
    }

    public String getFollow() {
        return follow;
    }

    public String getFans() {
        return fans;
    }

    public String getRental() {
        return rental;
    }
}
