package com.jyx.android.model;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by zfang on 2015/12/24.
 */
public class NewsList implements ListEntity {

    private List<NewsEntity> newsList = new ArrayList<NewsEntity>();
    @Override
    public List<?> getList() {
        return newsList;
    }

    public List<NewsEntity> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<NewsEntity> newsList) {
        this.newsList = newsList;
    }
//    public static AVObject toAVObject(NewsEntity mode ) {
//        AVObject post = new AVObject("news");
//        post.put("objectId", mode.getObjectId());
//        post.put("createdAt", mode.getCreatedAt());
//        post.put("updatedAt", mode.getUpdatedAt());
//        post.put("code", mode.getCode());
//        post.put("theme", mode.getTheme());
//        post.put("user_Id", mode.getUser_Id());
//        post.put("userName", mode.getUserName());
//        post.put("labelsJson", mode.getLabelsJson());
//        post.put("imageJson", mode.getImageJson());
//        post.put("imageUrl", mode.getImageUrl());
//        post.put("discribe", mode.getDiscribe());
//        post.put("newsType_Id", mode.getNewsType_Id());
//        post.put("type", mode.getType());
//        post.put("newsTypeName", mode.getNewsTypeName());
//        post.put("uid", mode.getUid());
//        post.put("openNum", mode.getOpenNum());
//        post.put("likeNum", mode.getLikeNum());
//        post.put("commentNum", mode.getCommentNum());
//        post.put("seeFlag", mode.getSeeFlag());
//        post.put("seeManJson", mode.getSeeManJson());
//        post.put("user", mode.getUser());
//        post.put("newsComment", mode.getNewsComment());
//        return post;
//    }
//
//    public static   NewsEntity toItem(AVObject avo) {
//        NewsEntity model = new NewsEntity();
//        model.setObjectId(avo.getObjectId());
//        model.setCreatedAt(avo.getDate("createdAt"));
//        model.setUpdatedAt(avo.getDate("updatedAt"));
//        model.setCode(avo.getString("code"));
//        model.setTheme(avo.getString("theme"));
//        model.setUser_Id(avo.getString("user_Id"));
//        model.setUserName(avo.getString("userName"));
//        model.setLabelsJson(avo.getString("labelsJson"));
//        model.setImageUrl(avo.getJSONArray("imageUrl"));
//        model.setImageJson(avo.getString("imageJson"));
//        model.setDiscribe(avo.getString("discribe"));
//        model.setNewsType_Id(avo.getString("newsType_Id"));
//        model.setType(avo.getInt("type"));
//        model.setNewsTypeName(avo.getString("newsTypeName"));
//        model.setUid(avo.getString("uid"));
//        model.setOpenNum(avo.getInt("openNum"));
//        model.setLikeNum(avo.getInt("likeNum"));
//        model.setCommentNum(avo.getInt("commentNum"));
//        model.setSeeFlag(avo.getInt("seeFlag"));
//        model.setSeeManJson(avo.getString("seeManJson"));
//        model.setUser(avo.getAVObject("user"));
//        model.setNewsComment(avo.getRelation("newsComment"));
//        return model;
//    }
}
