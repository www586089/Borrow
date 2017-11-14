package com.jyx.android.base;

import android.content.SharedPreferences;
import com.jyx.android.model.UserEntity;

import java.util.List;

import okhttp3.Cookie;

/**
 * Author : Tree
 * Date : 2016-01-07
 */
public class UserRecord {
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_NICK_NAME = "nick_name";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_PORTRAIT = "user_portrait";


    private UserEntity userEntity;

    private List<Cookie> cookies;


    private UserRecord(){
        SharedPreferences sp = Application.getInstance().getSharedPreferences();
        String userId = sp.getString(KEY_USER_ID, "");
        String nickName = sp.getString(KEY_NICK_NAME, "");
        String token = sp.getString(KEY_TOKEN, "");
        String userName = sp.getString(KEY_USER_NAME, "");

        userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setNickName(nickName);
        userEntity.setToken(token);
        userEntity.setUserName(userName);
    }

    private static final class InstanceHolder{
        static final UserRecord USER_RECORD = new UserRecord();
    }

    public static UserRecord getInstance(){
        return InstanceHolder.USER_RECORD;
    }

    public void saveUser(UserEntity newUserEntity){
        userEntity.setUserId(newUserEntity.getUserId());
        userEntity.setNickName(newUserEntity.getNickName());
        userEntity.setToken(newUserEntity.getToken());
        userEntity.setUserName(newUserEntity.getUserName());
        userEntity.setMobilePhoneNumber(newUserEntity.getMobilePhoneNumber());
        userEntity.setPortraitUri(newUserEntity.getPortraitUri());

        saveToSp(userEntity);
    }

    public UserEntity getUserEntity(){
        return userEntity;
    }

    public String getUserId(){
        return userEntity.getUserId();
    }

    public String getNickName(){
        return userEntity.getNickName();
    }

    public String getToken(){
        return userEntity.getToken();
    }

    public String getUserName(){
        return userEntity.getUserName();
    }

    private void saveToSp(UserEntity userEntity){
        SharedPreferences.Editor editor = Application.getPreferences().edit();
        editor.putString(KEY_USER_ID, userEntity.getUserId());
        editor.putString(KEY_TOKEN, userEntity.getToken());
        editor.putString(KEY_NICK_NAME, userEntity.getNickName());
        editor.putString(KEY_USER_NAME, userEntity.getUserName());
        editor.putString(KEY_USER_PORTRAIT, userEntity.getPortraitUri());
        editor.apply();
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }
}
