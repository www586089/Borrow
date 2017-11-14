package com.jyx.android.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 *
 * Created by gaobo on 2015/11/20.
 */
public class UserEntity implements Serializable{


    /**
     * birthday :
     * latestnews :
     * portraituri :
     * balance : 0
     * gender :
     * token : mtf3Tq6/y1x0rQX84gU/M0wV3zwjZk6qsi4KWNt1WQMJChtTaujd+Boz1E7CyYveuVT6HzJPS8Zf6TqdwFui5rLIsQDQLjTGHbe9SZodkck=
     * mobilephonenumber : 18992
     * createdat : 2016-01-04 18:03:49.0
     * password : tt
     * backgrounduri :
     * loveshopnum : 0
     * user_id : 133631443107611648
     * nickname :
     * deposit : 0
     * location : 100
     * namediscrib : mm1
     * latestshops :
     * mobilephoneverified : 0
     * lovenewsnum : 0
     * username : mm
     * updatedat :
     */

    private String birthday;
    @SerializedName("latestnews")
    private String latestNews;
    @SerializedName("portraituri")
    private String portraitUri;
    private String balance;
    private String gender;
    private String token;
    @SerializedName("mobilephonenumber")
    private String mobilePhoneNumber;
    @SerializedName("createdat")
    private String createdAt;
    private String password;
    @SerializedName("backgrounduri")
    private String backgroundUri;
    @SerializedName("loveshopnum")
    private String loveShopNum;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("nickname")
    private String nickName;
    private String deposit;
    private String location;
    @SerializedName("namediscrib")
    private String nameDiscrib;
    @SerializedName("latestshops")
    private String latestShops;
    @SerializedName("mobilephoneverified")
    private String mobilePhoneVerified;
    @SerializedName("lovenewsnum")
    private String loveNewsNum;
    @SerializedName("username")
    private String userName;
    @SerializedName("signature")
    private String signature;
    @SerializedName("updatedat")
    private String updatedAt;

    private String receipt_address;
    private String recipient_addressee;
    private String recipient_phone;
    public void setrecipient_addressee(String recipient_addressee){this.receipt_address = recipient_addressee;}
    public String recipient_addressee(){return receipt_address;}

    public void setrecipient_phone(String recipient_phone){this.receipt_address = recipient_phone;}
    public String getrecipient_phone(){return recipient_phone;}

    public void setreceipt_address(String receipt_address){this.receipt_address = receipt_address;}
    public String getreceipt_address(){return receipt_address;}

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setLatestNews(String latestNews) {
        this.latestNews = latestNews;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }

    public void setBalance(String blance) {
        this.balance = blance;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBackgroundUri(String backgroudUri) {
        this.backgroundUri = backgroudUri;
    }

    public void setLoveShopNum(String loveShopNum) {
        this.loveShopNum = loveShopNum;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setNameDiscrib(String nameDiscrib) {
        this.nameDiscrib = nameDiscrib;
    }

    public void setLatestShops(String latestShops) {
        this.latestShops = latestShops;
    }

    public void setMobilePhoneVerified(String mobilePhoneVerified) {
        this.mobilePhoneVerified = mobilePhoneVerified;
    }

    public void setLoveNewsNum(String loveNewsNum) {
        this.loveNewsNum = loveNewsNum;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setSignature(String sigNature) {
        this.signature = sigNature;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getLatestNews() {
        return latestNews;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public String getBalance() {
        return balance;
    }

    public String getGender() {
        return gender;
    }

    public String getToken() {
        return token;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getPassword() {
        return password;
    }

    public String getBackgroundUri() {
        return backgroundUri;
    }

    public String getLoveShopNum() {
        return loveShopNum;
    }

    public String getUserId() {
        return userId;
    }

    public String getNickName() {
        return nickName;
    }// 用户名

    public String getDeposit() {
        return deposit;
    }

    public String getLocation() {
        return location;
    }

    public String getNameDiscrib() {
        return nameDiscrib;
    }

    public String getLatestShops() {
        return latestShops;
    }

    public String getMobilePhoneVerified() {
        return mobilePhoneVerified;
    }

    public String getLoveNewsNum() {
        return loveNewsNum;
    }

    public String getUserName() {
        return userName;
    }  //1557564695

    public String getSignature() {
        return signature;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "birthday='" + birthday + '\'' +
                ", latestNews='" + latestNews + '\'' +
                ", portraitUri='" + portraitUri + '\'' +
                ", balance='" + balance + '\'' +
                ", gender='" + gender + '\'' +
                ", token='" + token + '\'' +
                ", mobilePhoneNumber='" + mobilePhoneNumber + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", password='" + password + '\'' +
                ", backgroundUri='" + backgroundUri + '\'' +
                ", loveShopNum='" + loveShopNum + '\'' +
                ", userId='" + userId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", deposit='" + deposit + '\'' +
                ", location='" + location + '\'' +
                ", nameDiscrib='" + nameDiscrib + '\'' +
                ", latestShops='" + latestShops + '\'' +
                ", mobilePhoneVerified='" + mobilePhoneVerified + '\'' +
                ", loveNewsNum='" + loveNewsNum + '\'' +
                ", userName='" + userName + '\'' +
                ", signature='" + signature + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}