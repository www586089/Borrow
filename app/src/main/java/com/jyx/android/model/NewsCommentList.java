package com.jyx.android.model;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by zfang on 2015/12/24.
 */
public class NewsCommentList implements ListEntity {

    private List<NewsComment> newsCommentsList = new ArrayList<NewsComment>();
    @Override
    public List<?> getList() {
        return newsCommentsList;
    }

    public List<NewsComment> getNewsList() {
        return newsCommentsList;
    }

    public void setNewsList(List<NewsComment> newsList) {
        this.newsCommentsList = newsList;
    }
//    public static AVObject toAVObject(NewsComment mode ) {
//        AVObject post = new AVObject("NewsComment");
//        post.put("objectId", mode.getObjectId());
//        post.put("createdAt", mode.getCreatedAt());
//        post.put("updatedAt", mode.getUpdatedAt());
//        post.put("news_Id", mode.getNews_Id());
//        post.put("contexts", mode.getContexts());
//        post.put("userName", mode.getUserName());
//        post.put("user_Id", mode.getUser_Id());
//        post.put("theme", mode.getTheme());
//        post.put("news", mode.getNews());
//        return post;
//    }
//
//    public static   NewsComment toItem(AVObject avo) {
//        NewsComment model = new NewsComment();
//        model.setObjectId(avo.getString("objectId"));
//        model.setCreatedAt(avo.getDate("createdAt"));
//        model.setUpdatedAt(avo.getDate("updatedAt"));
//        model.setNews_Id(avo.getString("news_Id"));
//        model.setContexts(avo.getString("contexts"));
//        model.setUserName(avo.getString("userName"));
//        model.setUser_Id(avo.getString("user_Id"));
//        model.setTheme(avo.getString("theme"));
//        model.setNews(avo.getAVObject("news"));
//        return model;
//    }
}
