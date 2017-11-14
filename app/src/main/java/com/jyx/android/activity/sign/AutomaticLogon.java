package com.jyx.android.activity.sign;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.jyx.android.base.Application;
import com.jyx.android.base.Constants;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.GroupInfo;
import com.jyx.android.model.GroupSerializable;
import com.jyx.android.model.UserEntity;
import com.jyx.android.model.param.SignParam;
import com.jyx.android.net.ApiManager;
import com.jyx.android.net.BizException;
import com.jyx.android.rx.CommonExceptionHandler;
import com.jyx.android.utils.ACache;
import com.jyx.android.utils.PreferenceHelper;
import com.sea_monster.common.Md5;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Dell on 2016/4/12.
 */
    public  class AutomaticLogon {
    private Context context;
    private SharedPreferences sp;
    private ACache mCache;
    public static String username;
    public static String pwd;
    public AutomaticLogon(Context context){
        this.context=context;
    }

    public  void login(){
                sp=context.getSharedPreferences("user", 0);
                username = sp.getString("username", "");
                 pwd = sp.getString("pwd", "");
                Log.e("重连", username+pwd);
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd)) {
                    return;
                }
                PreferenceHelper.write(context, Constants.FILE_KEY_PASSWORD, Constants.KEY_PASSWORD,
                        Md5.encode(pwd));
                SignParam signParam = new SignParam();
                signParam.setFunction("login");
                signParam.setUserName(username);
                signParam.setPassword(pwd);
                ApiManager.getApi()
                        .login(new Gson().toJson(signParam))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Func1<BaseEntry<List<UserEntity>>, UserEntity>() {
                            @Override
                            public UserEntity call(BaseEntry<List<UserEntity>> listBaseEntry) {
                                if (listBaseEntry == null) {
                                    throw new BizException(-1, "登陆失败");
                                }

                                if (listBaseEntry.getResult() != 0) {

                                }
                                return listBaseEntry.getData().get(0);
                            }
                        })
                        .subscribe(new Subscriber<UserEntity>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                                CommonExceptionHandler.handleBizException(e);
                            }

                            @Override
                            public void onNext(UserEntity userEntity) {
                                Log.e(getClass().getName(), "登陆融云");
                                connect(userEntity);
                            }
                        });
            }


    private void connect(final UserEntity userEntity) {
        Log.e(getClass().getName(), "登陆融云");
        RongIM.connect(userEntity.getToken(), new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.e(getClass().getName(), "Token错误");
            }

            @Override
            public void onSuccess(String userid) {
                UserRecord.getInstance().saveUser(userEntity);
                //获取好友信息
                updataUserList();
                updateGroupInfoList();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Application.showToast("登陆失败");
                Log.d(getClass().getName(), "连接错误 - " + errorCode);
            }

            @Override
            public void onFail(int errorCode) {
                super.onFail(errorCode);
                Log.e(getClass().getName(), "登陆错误 == " + errorCode);
            }
        });
    }
    private void updataUserList() {
        String xmlString = "";
        xmlString = "{\"function\":\"getmaillist\",\"userid\":\"" +
                UserRecord.getInstance().getUserId() + "\"}";
        ApiManager.getApi()
                .getMailList(xmlString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseEntry<List<UserEntity>>, List<UserEntity>>() {
                    @Override
                    public List<UserEntity>call(BaseEntry<List<UserEntity>> listBaseEntry) {
                        return listBaseEntry.getData();
                    }
                })
                .subscribe(new Subscriber<List<UserEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
//                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(final List<UserEntity> userList) {
                        Log.e("RongClondEvent", " 获得 好友== " + userList.size());
                        ArrayList<UserEntity> arrayList = new ArrayList<UserEntity>();
                        arrayList.addAll(userList);
                        mCache=ACache.get(context);
                        mCache.put(Constants.KEY_USER, arrayList);
                    }
                });
    }

    /**
     * 查询并更新群信息
     */
    private void updateGroupInfoList() {
        String xmlString = "";
        xmlString = "{\"function\":\"getmygrouplist\",\"userid\":\"" +
                UserRecord.getInstance().getUserId() + "\"}";
        ApiManager.getApi()
                .queryGroups(xmlString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseEntry<List<GroupInfo>>, List<GroupInfo>>() {
                    @Override
                    public List<GroupInfo> call(BaseEntry<List<GroupInfo>> listBaseEntry) {
                        return listBaseEntry.getData();
                    }
                }).subscribe(new Subscriber<List<GroupInfo>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
//                CommonExceptionHandler.handleBizException(e);
            }

            @Override
            public void onNext(final List<GroupInfo> groupInfos) {
                Log.e("RongClondEvent"," 获得 群 == "+groupInfos.size());
                ArrayList<GroupSerializable> arrayList  = new ArrayList<GroupSerializable>();
                for(GroupInfo info:groupInfos){
                    arrayList.add(new GroupSerializable(info.getGroupId(),info
                            .getGroupName(), info.getImageJson()));
                }
                mCache.put(Constants.KEY_GROUP,arrayList);
            }
        });
    }

}