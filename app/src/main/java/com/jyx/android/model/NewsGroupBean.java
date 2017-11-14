package com.jyx.android.model;

import java.util.List;

public class NewsGroupBean {
    private String user_id;
    private String portraituri;
    private String news_id;
    private String theme;
    private String describe;
    private List<String> imsgejson ;
    private String createdat;
    private List<NewsCommentBean> newscomment ;
    private List<NewsPraiseBean> praisenews ;
    private String group_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPortraituri() {
        return portraituri;
    }

    public void setPortraituri(String portraituri) {
        this.portraituri = portraituri;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public List<String> getImsgejson() {
        return imsgejson;
    }

    public void setImsgejson(List<String> imsgejson) {
        this.imsgejson = imsgejson;
    }

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }

    public List<NewsCommentBean> getNewscomment() {
        return newscomment;
    }

    public void setNewscomment(List<NewsCommentBean> newscomment) {
        this.newscomment = newscomment;
    }

    public List<NewsPraiseBean> getPraisenews() {
        return praisenews;
    }

    public void setPraisenews(List<NewsPraiseBean> praisenews) {
        this.praisenews = praisenews;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }
}
