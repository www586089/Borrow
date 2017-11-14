package com.jyx.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ItemBean implements Parcelable {
    private String portraituri;
    private String feature_name;
    private String item_id;
    private String discribe;
    private String opennum;
    private int praisenum;
    private String operatype_name;
    private double rent;
    private String likenum;
    private String imagejson;
    private String createdat;
    private String user_id;
    private String name;
    private String nickname;
    private int deposit;
    private String renttype_name;
    private String commentnum;
    private String updatedat;
    private String address;
    private int renttype_id;
    private int operatype;
    private double price;
    private List<ItemCommentBean> itemcomment;
    private List<PraisenItemInfoBean> praisenitem ;
    private String remarks;
    private double discountprice;

    private String stocknumber;


    private String gettype_name;
    private String type_name;
    private String group_id;
    private String groupPortraitUri;
    private String groupName;

    public String getGettype_name() {
        return gettype_name;
    }

    public void setGettype_name(String gettype_name) {
        this.gettype_name = gettype_name;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String typename) {
        this.type_name = type_name;
    }


    public String getStocknumber() {
        return stocknumber;
    }

    public void setStocknumber(String stocknumber) {
        this.stocknumber = stocknumber;
    }

    public String getPortraituri() {
        return portraituri;
    }

    public void setPortraituri(String portraituri) {
        this.portraituri = portraituri;
    }

    public String getFeature_name() {
        return feature_name;
    }

    public void setFeature_name(String feature_name) {
        this.feature_name = feature_name;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
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

    public int getPraisenum() {
        return praisenum;
    }

    public void setPraisenum(int praisenum) {
        this.praisenum = praisenum;
    }

    public String getOperatype_name() {
        return operatype_name;
    }

    public void setOperatype_name(String operatype_name) {
        this.operatype_name = operatype_name;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public String getRenttype_name() {
        return renttype_name;
    }

    public void setRenttype_name(String renttype_name) {
        this.renttype_name = renttype_name;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRenttype_id() {
        return renttype_id;
    }

    public void setRenttype_id(int renttype_id) {
        this.renttype_id = renttype_id;
    }

    public int getOperatype() {
        return operatype;
    }

    public void setOperatype(int operatype) {
        this.operatype = operatype;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<ItemCommentBean> getItemcomment() {
        return itemcomment;
    }

    public void setItemcomment(List<ItemCommentBean> itemcomment) {
        this.itemcomment = itemcomment;
    }

    public List<PraisenItemInfoBean> getPraisenitem() {
        return praisenitem;
    }

    public void setPraisenitem(List<PraisenItemInfoBean> praisenitem) {
        this.praisenitem = praisenitem;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public double getDiscountprice() {
        return discountprice;
    }

    public void setDiscountprice(double discountprice) {
        this.discountprice = discountprice;
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
        if (item instanceof ItemBean) {
            return this.item_id.equals(((ItemBean) item).getItem_id());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    public ItemBean(Parcel in) {
        this.portraituri = in.readString();
        this.feature_name = in.readString();
        this.item_id = in.readString();
        this.discribe = in.readString();
        this.opennum = in.readString();
        this.praisenum = in.readInt();
        this.operatype_name = in.readString();
        this.rent = in.readDouble();
        this.likenum = in.readString();
        this.imagejson = in.readString();
        this.createdat = in.readString();
        this.user_id = in.readString();
        this.name = in.readString();
        this.nickname = in.readString();
        this.deposit = in.readInt();
        this.renttype_name = in.readString();
        this.commentnum = in.readString();
        this.address = in.readString();
        this.renttype_id = in.readInt();
        this.operatype = in.readInt();
        this.price = in.readDouble();
        this.updatedat = in.readString();
        this.remarks = in.readString();
        this.discountprice = in.readDouble();
        this.stocknumber = in.readString();
        this.gettype_name = in.readString();
        this.type_name = in.readString();
    }

    public static final Parcelable.Creator<ItemBean> CREATOR = new Creator<ItemBean>() {
        @Override
        public ItemBean[] newArray(int size) {
            return new ItemBean[size];
        }

        @Override
        public ItemBean createFromParcel(Parcel in) {
            return new ItemBean(in);
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.portraituri);
        dest.writeString(this.feature_name);
        dest.writeString(this.item_id);
        dest.writeString(this.discribe);
        dest.writeString(this.opennum);
        dest.writeInt(this.praisenum);
        dest.writeString(this.operatype_name);
        dest.writeDouble(this.rent);
        dest.writeString(this.likenum);
        dest.writeString(this.imagejson);
        dest.writeString(this.createdat);
        dest.writeString(this.user_id);
        dest.writeString(this.name);
        dest.writeString(this.nickname);
        dest.writeInt(this.deposit);
        dest.writeString(this.renttype_name);
        dest.writeString(this.commentnum);
        dest.writeString(this.address);
        dest.writeInt(this.renttype_id);
        dest.writeInt(this.operatype);
        dest.writeDouble(this.price);
        dest.writeString(this.updatedat);
        dest.writeString(this.remarks);
        dest.writeDouble(this.discountprice);
        dest.writeString(this.stocknumber);
        dest.writeString(this.gettype_name);
        dest.writeString(this.type_name);
    }
}