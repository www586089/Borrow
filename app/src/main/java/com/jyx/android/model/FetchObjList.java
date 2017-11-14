package com.jyx.android.model;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by zfang on 2015/12/29.
 */
public class FetchObjList implements ListEntity{
    private List<FectObjWay> itemWayFecthList = new ArrayList<FectObjWay>();
    @Override
    public List<?> getList() {
        return itemWayFecthList;
    }

    public List<FectObjWay> getNewsList() {
        return itemWayFecthList;
    }

    public void setItemList(List<FectObjWay> newsList) {
        this.itemWayFecthList = newsList;
    }
//    public static AVObject toAVObject(FectObjWay mode ) {
//        AVObject post = new AVObject("FetchObj");
//        post.put("objectId", mode.getObjectId());
//        post.put("createdAt", mode.getCreatedAt());
//        post.put("updatedAt", mode.getUpdatedAt());
//        post.put("code", mode.getCode());
//        post.put("name", mode.getName());
//        post.put("wayDes", mode.getWayDes());
//        return post;
//    }
//
//    public static  FectObjWay toItem(AVObject avo) {
//        FectObjWay model = new FectObjWay();
//        model.setObjectId(avo.getString("objectId"));
//        model.setCreatedAt(avo.getDate("createdAt"));
//        model.setUpdatedAt(avo.getDate("updatedAt"));
//        model.setCode(avo.getString("code"));
//        model.setName(avo.getString("name"));
//        model.setWayDes(avo.getString("wayDes"));
//        return model;
//    }
}
