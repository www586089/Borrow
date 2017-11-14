package com.jyx.android.activity.sign;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
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
import com.jyx.android.utils.PreferenceHelper;
import com.sea_monster.common.Md5;
import com.umeng.OauthUserInfo;
import com.umeng.social.controller.UmengSocialController;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Tonlin on 2015/10/22.
 * 登录
 */
public class SignInActivity extends BaseActivity implements UmengSocialController.OauthLoginListener {

    public static String TAG = "SignInActivity";

    @Bind(R.id.et_username)
    EditText mEtUsername;
    @Bind(R.id.et_password)
    EditText mEtPassword;

    private UmengSocialController mController;

    private SweetAlertDialog mLoadingDialog;
    private  ACache mCache;


    private Context mContext;

    private SharedPreferences sp;

    final private int permsRequestCode = 200;
    final private String[] PERMISSIONS_LIST = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private void showPermissionMessage(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SignInActivity.this)
                .setMessage(message)
                .setPositiveButton("确定", okListener)
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void verifyPermissions() {
        for (int i=0; i<PERMISSIONS_LIST.length; i++)
        {
            int hasPermission = checkSelfPermission(PERMISSIONS_LIST[i]);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale(PERMISSIONS_LIST[i])) {
                    showPermissionMessage("你需要同意系统对该软件进行授权",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(PERMISSIONS_LIST, permsRequestCode);
                                }
                            });
                    return;
                }

                requestPermissions(PERMISSIONS_LIST, permsRequestCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case permsRequestCode:
                boolean grantr = true;
                for (int i=0; i<grantResults.length; i++)
                {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                    {
                        grantr = false;
                    }
                }
                Toast.makeText(SignInActivity.this, "请求授权被拒绝", Toast.LENGTH_LONG).show();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_in;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_sign_in;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        hideBackButton();
        mEtUsername.setText(UserRecord.getInstance().getUserName());

        com.umeng.socialize.utils.Log.LOG = true;

        mLoadingDialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在登陆");
        mLoadingDialog.setCancelable(false);

        mController = new UmengSocialController(this);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            verifyPermissions();
        }
        mCache = ACache.get(this);
    }

    private void showLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.show();
        }
    }

    private void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    protected void onActionRightClick(View view) {
        ActivityHelper.goMobileVerification(this);
    }


    //忘记密码了
    @OnClick(R.id.tv_forget_password)
    void clickForgetPassword() {
        ActivityHelper.goForgetPWD(this);
    }


    //直接登录
    @OnClick(R.id.btn_sign_in)
    void clickSignHuanXin() {
        //登陆
        final String strmobile = mEtUsername.getText().toString();
        String strpwd = mEtPassword.getText().toString();
        if (TextUtils.isEmpty(strmobile) || TextUtils.isEmpty(strpwd)) {
            return;
        }
        PreferenceHelper.write(this,Constants.FILE_KEY_PASSWORD,Constants.KEY_PASSWORD,
                Md5.encode(strpwd));

        showLoading();
        SignParam signParam = new SignParam();
        signParam.setFunction("login");
        signParam.setUserName(strmobile);
        signParam.setPassword(strpwd);
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
                            if (listBaseEntry.getMsg().equals(getString(R.string.session_data_error)))
                                new AutomaticLogon(getBaseContext()).login();
                            else
                            throw new BizException(listBaseEntry.getResult(), listBaseEntry.getMsg());
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
                        dismissLoading();
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(UserEntity userEntity) {
                        dismissLoading();
                        final String pwd = mEtPassword.getText().toString();
                        sp = getSharedPreferences("user", 0);
                        sp.edit().putString("username", strmobile).putString("pwd", pwd).commit();
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
                dismissLoading();
                UserRecord.getInstance().saveUser(userEntity);
                //获取好友信息
                updataUserList();
                updateGroupInfoList();
                //直接跳到主界面
                ActivityHelper.goMain(SignInActivity.this);

                finish();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                dismissLoading();
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

    @OnClick(R.id.iv_qq_login)
    void onQQLoginClick() {
        if (mController != null) {
            mController.doOauthVerify(SHARE_MEDIA.QQ, this);
        }

    }

    @OnClick(R.id.iv_wechat_login)
    void onWechatLoginClick() {
        if (mController != null) {
            mController.doOauthVerify(SHARE_MEDIA.WEIXIN, this);
        }
    }

    @OnClick(R.id.iv_weibo_login)
    void onWeiboLoginClick() {
        if (mController != null) {
            mController.doOauthVerify(SHARE_MEDIA.SINA, this);
        }
    }

    @Override
    public void onStart(SHARE_MEDIA platform) {

    }

    /**
     * 第三方登录回调
     * @param platform
     * @param userInfo
     */
    @Override
    public void onComplete(SHARE_MEDIA platform, final OauthUserInfo userInfo) {


        String third_type = null;
        if (platform == SHARE_MEDIA.SINA) {
            third_type = "3";
        } else if (platform == SHARE_MEDIA.QQ) {
            third_type = "1";
        } else if (platform == SHARE_MEDIA.WEIXIN) {
            third_type = "2";
        }
    //OauthUserInfo{expiresTime='7200', uid='o0RWDuOHGROmSHLj913bUVpmQ1WQ',
    // token='OezXcEiiBSKSxW0eoylIeOMlKNY8S8no-U862ucy2mOh8imyLuMJTxPou-XJZE70EHpqtGmi9r_
    // xygqsP0b7uL-pTXGAIQBoUeZL2A_FW41HY3vXBgNwWsLiO2c3Gd0IlV6UdcoLO9zbQlfvWk3z2w'}

        String param = "{\"function\":\"thirdlogin\",\"third_token\":\"" + userInfo.getToken()
                + "\",\"openid\":\""+userInfo.getUid()+"\",\"third_type\":\"" + third_type
                + "\",\"expires_in\":\"" + userInfo.getExpiresTime() + "\"}";
        final String finalThird_type = third_type;

        ApiManager.getApi()
                .login(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseEntry<List<UserEntity>>, UserEntity>() {
                    @Override
                    public UserEntity call(BaseEntry<List<UserEntity>> listBaseEntry) {
                        if (listBaseEntry == null) {
                            throw new BizException(-1, getString(R.string.login_failed));
                        }
                        if (listBaseEntry.getResult() != 0) {

                                throw new BizException(listBaseEntry.getResult(), listBaseEntry.getMsg());
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
//                        CommonExceptionHandler.handleBizException(e);
                        Intent intent = new Intent(SignInActivity.this, MobileVerificationActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("userid",userInfo.getUid());
                        bundle.putSerializable("userInfo",userInfo);
                        bundle.putString("third_type", finalThird_type);
                        intent.putExtra("user", bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onNext(UserEntity userEntity) {

                        sp = getSharedPreferences("user", 0);
                        sp.edit().putString("username", userEntity.getMobilePhoneNumber()).putString("pwd", userEntity.getPassword()).commit();
                        connect(userEntity);
                    }
                });

    }



    @Override
    public void onError(SHARE_MEDIA platform) {

    }

    @Override
    public void onCancel(SHARE_MEDIA platform) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mController != null) {
            mController.onSinaSSOReturn(requestCode, resultCode, data);
        }
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
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(final List<UserEntity> userList) {
                        Log.e("RongClondEvent"," 获得 好友== "+userList.size());
                        ArrayList<UserEntity> arrayList = new ArrayList
                                <UserEntity>();
                        arrayList.addAll(userList);
                        mCache.put(Constants.KEY_USER,arrayList);
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
                CommonExceptionHandler.handleBizException(e);
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
