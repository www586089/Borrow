package com.jyx.android.model;

/**
 * Created zfang 2016-02-02
 */
public class PraisenItemInfoBean {
    private String praise_item_id;
    private String user_Id;
    private String nickname;
    private String portraituri;
    private String createdat;

    public String getPraise_item_id() {
        return praise_item_id;
    }

    public void setPraise_item_id(String praise_item_id) {
        this.praise_item_id = praise_item_id;
    }

    public String getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(String user_id) {
        this.user_Id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPortraituri() {
        return portraituri;
    }

    public void setPortraituri(String portraituri) {
        this.portraituri = portraituri;
    }

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }
}
