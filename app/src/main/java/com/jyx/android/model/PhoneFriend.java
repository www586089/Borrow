package com.jyx.android.model;

/**
 * Author : Tree
 * Date : 2016-01-23
 */
public class PhoneFriend {


    public static final int ITEM = 0;
    public static final int SECTION = 1;

    private String pinyin;
    private String firstSpell;

    private int type;
    /**
     * portraituri :
     * nickname : 何总
     * name : 和
     * mobile : 18576461262
     * userid : 134354838778577920
     * username : 18576461262
     * status : 2
     */

    private String portraituri;
    private String nickname;
    private String name;
    private String mobile;
    private String userid;
    private String username;
    private String status;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getFirstSpell() {
        return firstSpell;
    }

    public void setFirstSpell(String firstSpell) {
        this.firstSpell = firstSpell;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPortraituri() {
        return portraituri;
    }

    public void setPortraituri(String portraituri) {
        this.portraituri = portraituri;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PhoneFriend{" +
                "pinyin='" + pinyin + '\'' +
                ", firstSpell='" + firstSpell + '\'' +
                ", type=" + type +
                ", portraituri='" + portraituri + '\'' +
                ", nickname='" + nickname + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
