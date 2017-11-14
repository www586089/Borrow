package com.jyx.android.activity.sign;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jyx.android.R;
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
import com.jyx.android.rx.ResultConvertFunc;
import com.jyx.android.utils.ACache;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 输入密码页面
 * Created by Even on 2015/10/27.
 */
public class PasswordActivity extends BaseActivity {
    public static final String KEY_MOBILE_NUM = "mobile";
    private ACache mCache;

    @Bind(R.id.tv_set_password)
    TextView mTvSetPassword;
    @Bind(R.id.et_set_password)
    EditText mEtSetPassword;
    @Bind(R.id.tv_verify_password)
    TextView mTvVerifyPassword;
    @Bind(R.id.et_verify_password)
    EditText mEtVerifyPassword;
    @Bind(R.id.btn_next_step)
    Button mBtnNextStep;

    private String mobileNum;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_password;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }


    @Override
    protected int getActionBarTitle() {
        return R.string.input_password;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mCache=ACache.get(this);
        mTvActionRight.setVisibility(View.INVISIBLE);
        mobileNum = getIntent().getStringExtra(KEY_MOBILE_NUM);
    }

    @OnClick(R.id.btn_next_step)
    void clickNextStep() {
        final String pwd = mEtSetPassword.getText().toString();
        String confirmPwd = mEtVerifyPassword.getText().toString();
        if(TextUtils.isEmpty(pwd.trim())){
            Application.showToast("请输入密码");
            return;
        }
        if(!confirmPwd.equals(pwd)){
            Application.showToast("密码不一致");
            return;
        }
        Pattern p = Pattern.compile("^[0-9]*$");
        Matcher m = p.matcher(pwd);
        if (!m.matches())
        {
            Application.showToastShort("密码必须是数字");
            return ;
        }
        if(pwd.length()!=6 &&confirmPwd.length()!=6){
            Application.showToastShort("密码长度限6位");
            return;
        }
        showWaitDialog();
        String param = "{\"function\":\"registeruser\",\"mobilephonenumber\":\"" + mobileNum
                + "\",\"mobilephoneverified\":\"1\",\"password\":\"" + pwd
                + "\",\"username\":\"" + mobileNum + "\",\"namediscrib\":\"\"}";
        ApiManager.getApi()
                .register(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new ResultConvertFunc<List<Void>>())
                .subscribe(new Subscriber<List<Void>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideWaitDialog();
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(List<Void> voids) {
                        hideWaitDialog();
                        login(mobileNum, pwd);
                    }
                });
    }


    private void login(final String strmobile, final String strpwd){
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
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(UserEntity userEntity) {
                        SharedPreferences sp = getSharedPreferences("user", 0);
                        sp.edit().putString("username", strmobile).putString("pwd", strpwd).commit();
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
                //跳转到完善信息界面
                Intent intent=new Intent(getBaseContext(),PerfectInformationActivity.class);
                startActivity(intent);
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
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(final List<UserEntity> userList) {
                        Log.e("RongClondEvent", " 获得 好友== " + userList.size());
                        ArrayList<UserEntity> arrayList = new ArrayList
                                <UserEntity>();
                        arrayList.addAll(userList);
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
                CommonExceptionHandler.handleBizException(e);
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
