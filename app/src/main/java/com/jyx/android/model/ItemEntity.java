package com.jyx.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.jyx.android.base.UserRecord;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Date;

/**
 * Created by user on 2015/10/6.
 */
public class ItemEntity  implements Parcelable {
    private String  code;
    private String    name  ;
    private int   operaType ;
    private String   user_Id  ;
    private String   userName      ;
    private int   feature_Id  ;
    private String   featureName ;
    private String   labelsJson ;
    private String   imageJson  ;
    private JSONArray imageUrl;
    private String   discribe   ;
    private int   price  ;
    private String   priceType  ;
    private String   shopType_Id;
    private String   brand_Id   ;
    private int    deposit;
    private String    brandName  ;
    private String    type_Id    ;
    private String   typeName   ;
    private String    uid        ;
    private int    rentMoney ;
    private int discountPrice;
    private  String objectId;
    private int   openNum ;
    private int   likeNum   ;
    private int   commentNum   ;
    private int   orderNum    ;
    private Date updatedAt;


    public ItemEntity() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOperaType() {
        return operaType;
    }

    public void setOperaType(int operaType) {
        this.operaType = operaType;
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

    public int getFeature_Id() {
        return feature_Id;
    }

    public void setFeature_Id(int feature_Id) {
        this.feature_Id = feature_Id;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getShopType_Id() {
        return shopType_Id;
    }

    public void setShopType_Id(String shopType_Id) {
        this.shopType_Id = shopType_Id;
    }

    public String getBrand_Id() {
        return brand_Id;
    }

    public void setBrand_Id(String brand_Id) {
        this.brand_Id = brand_Id;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getType_Id() {
        return type_Id;
    }

    public void setType_Id(String type_Id) {
        this.type_Id = type_Id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getRentMoney() {
        return rentMoney;
    }

    public void setRentMoney(int rentMoney) {
        this.rentMoney = rentMoney;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


    public ItemEntity(Parcel in) {
        this.code = in.readString();
        this.name = in.readString();
        this.operaType = in.readInt();
        this.user_Id = in.readString();
        this.userName = in.readString();
        this.feature_Id = in.readInt();
        this.featureName = in.readString();
        this.labelsJson = in.readString();
        this.imageJson = in.readString();
        getImageUrlForParcel(in);
        this.discribe = in.readString();
        this.price = in.readInt();
        this.priceType = in.readString();
        this.shopType_Id = in.readString();
        this.brand_Id = in.readString();
        this.deposit = in.readInt();
        this.brandName = in.readString();
        this.type_Id = in.readString();
        this.typeName = in.readString();
        this.uid = in.readString();
        this.rentMoney = in.readInt();
        this.discountPrice = in.readInt();
        this.objectId = in.readString();
        this.openNum = in.readInt();
        this.likeNum = in.readInt();
        this.commentNum = in.readInt();
        this.orderNum = in.readInt();
        getUpdatedAtForParcel(in);
    }

    private void getUpdatedAtForParcel(Parcel in) {
        this.updatedAt = new Date();
        this.updatedAt.setTime(in.readLong());
    }
    private void getImageUrlForParcel(Parcel in) {
        try {
            String imageUrlStr = in.readString();
            if (null != imageUrlStr && imageUrlStr.trim().length() > 0) {
                this.imageUrl = new JSONArray(imageUrlStr);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUpdatedAtForParcel(Parcel dest) {
        if (null != this.updatedAt) {
            dest.writeLong(this.updatedAt.getTime());
        }
    }

    private void setImageUrlForParcel(Parcel dest) {
        if (null != this.imageUrl && this.imageUrl.toString().trim().length() > 0) {
            dest.writeString(this.imageUrl.toString());
        } else {
            dest.writeString("");
        }
    }
    public static final Parcelable.Creator<ItemEntity> CREATOR = new Creator<ItemEntity>() {
        @Override
        public ItemEntity[] newArray(int size) {
            return new ItemEntity[size];
        }

        @Override
        public ItemEntity createFromParcel(Parcel in) {
            return new ItemEntity(in);
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.name);
        dest.writeInt(this.operaType);
        dest.writeString(this.user_Id);
        dest.writeString(this.userName);
        dest.writeInt(this.feature_Id);
        dest.writeString(this.featureName);
        dest.writeString(this.labelsJson);
        dest.writeString(this.imageJson);
        setImageUrlForParcel(dest);
        dest.writeString(this.discribe);
        dest.writeInt(this.price);
        dest.writeString(this.priceType);
        dest.writeString(this.shopType_Id);
        dest.writeString(this.brand_Id);
        dest.writeInt(this.deposit);
        dest.writeString(this.brandName);
        dest.writeString(this.type_Id);
        dest.writeString(this.typeName);
        dest.writeString(this.uid);
        dest.writeInt(this.rentMoney);
        dest.writeInt(this.discountPrice);
        dest.writeString(this.objectId);
        dest.writeInt(this.openNum);
        dest.writeInt(this.likeNum);
        dest.writeInt(this.commentNum);
        dest.writeInt(this.orderNum);
        setUpdatedAtForParcel(dest);
    }
}
