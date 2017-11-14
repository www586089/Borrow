package com.jyx.android.model;

import java.util.List;

public class NewsFriendsBean {
    private String portraituri;
    private String discribe;
    private String opennum;
    private String news_id;
    private String likenum;
    private String imagejson;
    private String createdat;
    private List<PraisenNewsInfoBean> praisenews ;
    private String user_id;
    private List<NewsCommentBean> newscomment ;
    private String nickname;
    private String theme;
    private String commentnum;
    private String updatedat;


    private String group_id;
    private String groupPortraitUri;
    private String groupName;
    public String getPortraituri() {
        return portraituri;
    }

    public void setPortraituri(String portraituri) {
        this.portraituri = portraituri;
    }

    public String getDiscribe() {
        return discribe;
    }

    public void setDiscribe(String discribe) {
        this.discribe = discribe;
    }

    public String getOpennum() {
        return opennum;
    }

    public void setOpennum(String opennum) {
        this.opennum = opennum;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public String getLikenum() {
        return likenum;
    }

    public void setLikenum(String likenum) {
        this.likenum = likenum;
    }

    public String getImagejson() {
        return imagejson;
    }

    public void setImagejson(String imagejson) {
        this.imagejson = imagejson;
    }

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }

    public List<PraisenNewsInfoBean> getPraisenews() {
        return praisenews;
    }

    public void setPraisenews(List<PraisenNewsInfoBean> praisenews) {
        this.praisenews = praisenews;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<NewsCommentBean> getNewscomment() {
        return newscomment;
    }

    public void setNewscomment(List<NewsCommentBean> newscomment) {
        this.newscomment = newscomment;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(String commentnum) {
        this.commentnum = commentnum;
    }

    public String getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(String updatedat) {
        this.updatedat = updatedat;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroupPortraitUri() {
        return groupPortraitUri;
    }

    public void setGroupPortraitUri(String groupPortraitUri) {
        this.groupPortraitUri = groupPortraitUri;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    @Override
    public boolean equals(Object item) {
        if (item instanceof NewsFriendsBean) {
            return this.news_id.equals(((NewsFriendsBean) item).getNews_id());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
