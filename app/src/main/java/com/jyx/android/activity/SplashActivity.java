package com.jyx.android.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.activity.sign.AutomaticLogon;
import com.jyx.android.base.Application;
import com.jyx.android.base.BaseActivity;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Tonlin on 2015/10/22.
 * 启动欢迎页
 */
public class SplashActivity extends BaseActivity {
    @Bind(R.id.activity_splash_lv)
    LinearLayout mLinearLayout;
    private ACache mCache;
    String username;
    String pwd;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        final SharedPreferences sp=getSharedPreferences("user",0);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(2000);
        mLinearLayout.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                username = sp.getString("username", "");
                pwd = sp.getString("pwd", "");
                //判断储存账号密码是否为空
                if (!username.equals("") && !pwd.equals("")) {
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
                                        throw new BizException(-1, getString(R.string.login_failed));
                                    }

                                    if (listBaseEntry.getResult() != 0) {
                                        if (listBaseEntry.getMsg().equals(getString(R.string.session_data_error))){
                                            new AutomaticLogon(getBaseContext()).login();
                                        }
                                        else {
                                            throw new BizException(listBaseEntry.getResult(), listBaseEntry.getMsg());
                                        }

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
                                    ActivityHelper.goSignIn(SplashActivity.this);
                                    finish();
                                    CommonExceptionHandler.handleBizException(e);
                                }

                                @Override
                                public void onNext(UserEntity userEntity) {

                                    connect(userEntity);
                                }
                            });
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 跳到 登陆页面
                if (pwd.equals("")&&username.equals("")){
                    ActivityHelper.goSignIn(SplashActivity.this);
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

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
                //直接跳到主界面
                ActivityHelper.goMain(SplashActivity.this);
                finish();
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
                        ArrayList<UserEntity> arrayList = new ArrayList
                                <UserEntity>();
                        arrayList.addAll(userList);
                        mCache.put(Constants.KEY_USER, arrayList);
                        Log.e("sas",arrayList.toString());
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
                Log.e("RongClondEvent", " 获得 群 == " + groupInfos.size());
                ArrayList<GroupSerializable> arrayList = new ArrayList<GroupSerializable>();
                for (GroupInfo info : groupInfos) {
                    arrayList.add(new GroupSerializable(info.getGroupId(), info
                            .getGroupName(), info.getImageJson()));
                }
                mCache.put(Constants.KEY_GROUP, arrayList);
            }
        });
    }

}
