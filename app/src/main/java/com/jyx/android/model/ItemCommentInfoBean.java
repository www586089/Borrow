package com.jyx.android.model;

/**
 * create by zfang at 2016-01-27
 */
public class ItemCommentInfoBean {
    private String item_comment_id;
    private String user_id;
    private String nickname;
    private String portraituri;
    private String commentfor;
    private String commentfor_name;
    private String contexts;
    private String createdat;

    public String getItem_comment_id() {
        return item_comment_id;
    }

    public void setItem_comment_id(String item_comment_id) {
        this.item_comment_id = item_comment_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getCommentfor() {
        return commentfor;
    }

    public void setCommentfor(String commentfor) {
        this.commentfor = commentfor;
    }

    public String getCommentfor_name() {
        return commentfor_name;
    }

    public void setCommentfor_name(String commentfor_name) {
        this.commentfor_name = commentfor_name;
    }

    public String getContexts() {
        return contexts;
    }

    public void setContexts(String contexts) {
        this.contexts = contexts;
    }

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }
}
