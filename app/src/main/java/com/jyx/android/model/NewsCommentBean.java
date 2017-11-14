package com.jyx.android.model;

/**
 * create by zfang at 2016-01-24
 */
public class NewsCommentBean {
    private String news_comment_id;
    private String user_id;
    private String nickname;
    private String commentfor;
    private String commentfor_name;
    private String contexts;
    private String createdat;

    public String getNews_comment_id() {
        return news_comment_id;
    }

    public void setNews_comment_id(String news_comment_id) {
        this.news_comment_id = news_comment_id;
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
