package com.jyx.android.model.param;

/**
 * Author : zfang
 * Date : 2016-02-27
 */
public class NewsCommentParam {
    private String function;
    private String userid;
    private String newsid;
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

    public String getNewsid() {
        return newsid;
    }

    public void setNewsid(String newsid) {
        this.newsid = newsid;
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
