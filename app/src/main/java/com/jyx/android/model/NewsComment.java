package com.jyx.android.model;

import com.jyx.android.base.UserRecord;

import java.io.Serializable;
import java.util.Date;

public class NewsComment implements Serializable{
    private String objectId;
    private Date createdAt;
    private Date updatedAt;
    private String news_Id;
    private String contexts;
    private String userName;
    private String user_Id;
    private String theme;
//    private AVObject news;

    public NewsComment() {
        setUserName(UserRecord.getInstance().getUserName());
        setUser_Id(UserRecord.getInstance().getUserId());
    }
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getNews_Id() {
        return news_Id;
    }

    public void setNews_Id(String news_Id) {
        this.news_Id = news_Id;
    }

    public String getContexts() {
        return contexts;
    }

    public void setContexts(String contexts) {
        this.contexts = contexts;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(String user_Id) {
        this.user_Id = user_Id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

//    public AVObject getNews() {
//        return news;
//    }
//
//    public void setNews(AVObject news) {
//        this.news = news;
//    }
}
