package com.jyx.android.model;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2015/10/28.
 */
public class ItemTypeList  implements ListEntity{
private List<ItemType> itemTypeList = new ArrayList<ItemType>();
    @Override
    public List<?> getList() {
        return itemTypeList;
    }

    public List<ItemType> getItemTypeList() {
        return itemTypeList;
    }

    public void setItemTypeList(List<ItemType> itemTypeList) {
        this.itemTypeList = itemTypeList;
    }

//    public static   ItemType toItem(AVObject avo) {
//        ItemType model = new ItemType();
//        model.setObjectId(avo.getString("objectId"));
//        model.setCreatedAt(avo.getDate("createdAt"));
//        model.setUpdatedAt(avo.getDate("updatedAt"));
//        model.setCode(avo.getString("code"));
//        model.setName(avo.getString("name"));
//        return model;
//    }
//
//
//    public static AVObject toAVObject(ItemType model ) {
//        AVObject post = new AVObject("itemType");
//        post.put("objectId", model.getObjectId());
//        post.put("createdAt", model.getCreatedAt());
//        post.put("updatedAt", model.getUpdatedAt());
//        post.put("code", model.getCode());
//        post.put("name", model.getName());
//        return  post;
//    }
}
