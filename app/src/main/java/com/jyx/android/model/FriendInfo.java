package com.jyx.android.model;

import com.google.gson.annotations.SerializedName;

/**
 * Author : Tree
 * Date : 2016-01-10
 */
public class FriendInfo {

    public static final int ITEM = 0;
    public static final int SECTION = 1;

    /**
     * portraituri :
     * user_id : 134355326907483136
     * signature : 3
     * user_relation_id : 133631443107611648
     * nickname : 789
     * remarks :
     */

    @SerializedName("portraituri")
    private String portraitUri;
    @SerializedName("user_id")
    private String userId;
    private String signature;
    @SerializedName("user_relation_id")
    private String userRelationId;
    @SerializedName("nickname")
    private String nickName;
    private String remarks;

    private String pinyin;

    private String firstSpell;

    private int type;

    private int attention;

    private boolean isSelected;

    private String mobilephonenumber;

    public String getMobilephonenumberr() {
        return mobilephonenumber;
    }

    public void setMobilephonenumber(String mobilephonenumber) {
        this.mobilephonenumber = mobilephonenumber;
    }


    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setUserRelationId(String userRelationId) {
        this.userRelationId = userRelationId;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public String getUserId() {
        return userId;
    }

    public String getSignature() {
        return signature;
    }

    public String getUserRelationId() {
        return userRelationId;
    }

    public String getNickName() {
        return nickName;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFirstSpell() {
        return firstSpell;
    }

    public void setFirstSpell(String firstSpell) {
        this.firstSpell = firstSpell;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FriendInfo that = (FriendInfo) o;

        return userId.equals(that.userId);

    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    @Override
    public String toString() {
        return "FriendInfo{" +
                "portraitUri='" + portraitUri + '\'' +
                ", userId='" + userId + '\'' +
                ", signature='" + signature + '\'' +
                ", userRelationId='" + userRelationId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", remarks='" + remarks + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", firstSpell='" + firstSpell + '\'' +
                ", type=" + type +
                ", attention=" + attention +
                '}';
    }
}
