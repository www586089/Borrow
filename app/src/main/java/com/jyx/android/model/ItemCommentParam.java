package com.jyx.android.model;

public class ItemCommentParam {
    private String function;
    private String userid;
    private String itemid;
    private String theme;
    private String contexts;
    private String commentfor;

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

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getContexts() {
        return contexts;
    }

    public void setContexts(String contexts) {
        this.contexts = contexts;
    }

    public String getCommentfor() {
        return commentfor;
    }

    public void setCommentfor(String commentfor) {
        this.commentfor = commentfor;
    }
}
