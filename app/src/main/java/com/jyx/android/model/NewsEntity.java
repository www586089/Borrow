package com.jyx.android.model;


import com.jyx.android.base.UserRecord;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zfang on 2015/12/24.
 * 动态、群动态、活动都取这个表显示，用type作区分，1[动态、群动态]，2[活动]
 */
public class NewsEntity implements Serializable{
    private String objectId;
    private Date createdAt;
    private Date updatedAt;
    private String code;
    private String theme;
    private String user_Id;
    private String userName;
    private String labelsJson;
    private JSONArray imageUrl;
    private String imageJson;
    private String discribe;
    private String newsType_Id;
    private int type;//1 动态， 2 活动
    private String newsTypeName;
    private String uid;
    private int openNum;
    private int  likeNum;
    private int commentNum;
    private int seeFlag;
    private String seeManJson;
//    private AVObject user;
//    private AVRelation newsComment;//relate to comment list

    public NewsEntity() {
//        user = AVObject.createWithoutData("_User", UserRecord.getInstance().getUserId());
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(String user_Id) {
        this.user_Id = user_Id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLabelsJson() {
        return labelsJson;
    }

    public void setLabelsJson(String labelsJson) {
        this.labelsJson = labelsJson;
    }

    public String getImageJson() {
        return imageJson;
    }

    public void setImageJson(String imageJson) {
        this.imageJson = imageJson;
    }

    public JSONArray getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(JSONArray imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDiscribe() {
        return discribe;
    }

    public void setDiscribe(String discribe) {
        this.discribe = discribe;
    }

    public String getNewsType_Id() {
        return newsType_Id;
    }

    public void setNewsType_Id(String newsType_Id) {
        this.newsType_Id = newsType_Id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNewsTypeName() {
        return newsTypeName;
    }

    public void setNewsTypeName(String newsTypeName) {
        this.newsTypeName = newsTypeName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getOpenNum() {
        return openNum;
    }

    public void setOpenNum(int openNum) {
        this.openNum = openNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getSeeFlag() {
        return seeFlag;
    }

    public void setSeeFlag(int seeFlag) {
        this.seeFlag = seeFlag;
    }

    public String getSeeManJson() {
        return seeManJson;
    }

    public void setSeeManJson(String seeManJson) {
        this.seeManJson = seeManJson;
    }

}
