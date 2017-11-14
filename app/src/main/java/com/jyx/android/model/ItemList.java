package com.jyx.android.model;





import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2015/10/11.
 */
public class ItemList  implements ListEntity  {

    public List<ItemEntity> getItemlist() {
        return itemlist;
    }

    public void setItemlist(List<ItemEntity> itemlist) {
        this.itemlist = itemlist;
    }

    private List<ItemEntity> itemlist = new ArrayList<ItemEntity>();

//    /**
//     * 将对象转换为存储对象
//     * @param mode
//     * @return
//     */
//
//    public static AVObject toAVObject(ItemEntity mode ) {
//        AVObject post = new AVObject("item");
//        // UUID.randomUUID().toString()
//        post.put("uid",mode.getUid());
//        post.put("brand_Id", mode.getBrand_Id());
//        post.put("brandName", mode.getBrandName());
//        post.put("code", mode.getCode());
//        post.put("deposit", mode.getDeposit());
//        post.put("discribe", mode.getDiscribe());
//        post.put("feature_Id", mode.getFeature_Id());
//        post.put("featureName", mode.getFeatureName());
//        post.put("imageJson", mode.getImageJson());
//        post.put("labelsJson", mode.getLabelsJson());
//        post.put("imageUrl", mode.getImageUrl());
////        post.put("name", mode.getName());
//        post.put("operaType", mode.getOperaType());
//        post.put("price", mode.getPrice());
//        post.put("priceType", mode.getPriceType());
//        post.put("rentMoney", mode.getRentMoney());
//        post.put("shopType_Id", mode.getShopType_Id());
//        post.put("type_Id", mode.getType_Id());
//        post.put("typeName", mode.getTypeName());
//        post.put("user_Id", mode.getUser_Id());
//        post.put("userName", mode.getUserName());
//        post.put("discountPrice", mode.getDiscountPrice());
//        post.put("user", mode.getUser());
////        post.put("",mode.get);
//        return post;
//    }
////
//
//    public static   ItemEntity toItem(AVObject avo)
//    {
//        ItemEntity model=new ItemEntity();
//        model.setBrand_Id(avo.getString("brand_Id"));
//        model.setBrandName(avo.getString("brandName"));
//        model.setCode(avo.getString("code"));
//        model.setDeposit(avo.getInt("deposit"));
//        model.setDiscribe(avo.getString("discribe"));
//        model.setFeature_Id(avo.getInt("feature_Id"));
//        model.setFeatureName(avo.getString("featureName"));
//        model.setImageJson(avo.getString("imageJson"));
//        model.setImageUrl(avo.getJSONArray("imageUrl"));
//        model.setLabelsJson(avo.getString("labelsJson"));
//        model.setName(avo.getString("name"));
//        model.setOperaType(avo.getInt("operaType"));
//        model.setPrice(avo.getInt("price"));
//        model.setPriceType(avo.getString("priceType"));
//        model.setRentMoney(avo.getInt("rentMoney"));
//        model.setShopType_Id(avo.getString("shopType_Id"));
//        model.setType_Id(avo.getString("type_Id"));
//        model.setTypeName(avo.getString("typeName"));
//        model.setUid(avo.getString("uid"));
//        model.setUser_Id(avo.getString("user_Id"));
//        model.setUserName(avo.getString("userName"));
//        model.setObjectId(avo.getString("objectId"));
//        model.setUpdatedAt(avo.getDate("updatedAt"));
//        model.setOpenNum(avo.getInt("openNum"));
//        model.setOrderNum(avo.getInt("orderNum"));
//        model.setLikeNum(avo.getInt("likeNum"));
//        model.setCommentNum(avo.getInt("commentNum"));
//        model.setDiscountPrice(avo.getInt("discountPrice"));
//        model.setUser(avo.getAVObject("user"));
//
//
//        return model;
//
//        //  avo.getInt("")
//
//    }


    @Override
    public List<?> getList() {
        return itemlist;
    }





}
