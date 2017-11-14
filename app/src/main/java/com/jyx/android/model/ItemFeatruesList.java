package com.jyx.android.model;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by zfang on 2015/12/29.
 */
public class ItemFeatruesList implements ListEntity{
    private List<ItemFeatures> itemFeatruesList = new ArrayList<ItemFeatures>();
    @Override
    public List<?> getList() {
        return itemFeatruesList;
    }

    public List<ItemFeatures> getNewsList() {
        return itemFeatruesList;
    }

    public void setNewsList(List<ItemFeatures> newsList) {
        this.itemFeatruesList = newsList;
    }
//    public static AVObject toAVObject(ItemFeatures mode ) {
//        AVObject post = new AVObject("NewsComment");
//        post.put("objectId", mode.getObjectId());
//        post.put("createdAt", mode.getCreatedAt());
//        post.put("updatedAt", mode.getUpdatedAt());
//        post.put("code", mode.getCode());
//        post.put("name", mode.getName());
//        post.put("type", mode.getType());
//        return post;
//    }
//
//    public static  ItemFeatures toItem(AVObject avo) {
//        ItemFeatures model = new ItemFeatures();
//        model.setObjectId(avo.getString("objectId"));
//        model.setCreatedAt(avo.getDate("createdAt"));
//        model.setUpdatedAt(avo.getDate("updatedAt"));
//        model.setCode(avo.getString("code"));
//        model.setName(avo.getString("name"));
//        model.setType(avo.getString("type"));
//        return model;
//    }
}
