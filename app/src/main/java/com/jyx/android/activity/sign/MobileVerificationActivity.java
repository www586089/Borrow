package com.jyx.android.activity.sign;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.jyx.android.R;
import com.jyx.android.base.Application;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.net.ApiManager;
import com.jyx.android.rx.CommonExceptionHandler;
import com.jyx.android.rx.ResultConvertFunc;
import com.jyx.android.rx.RxUtils;
import com.jyx.android.utils.StringUtils;
import com.jyx.android.utils.SystemTool;
import com.umeng.OauthUserInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 手机验证
 * Created by Even on 2015/10/26.
 */
public class MobileVerificationActivity extends BaseActivity {
    private String openid;
    private OauthUserInfo userInfo;
    private String Third_type="0";
    @Bind(R.id.et_phone_number)
    EditText mEtPhoneNumber;
    @Bind(R.id.et_verification_code)
    EditText mEtVerificationCode;
    @Bind(R.id.btn_get_verification_code_sms)
    Button mBtnGetSMS;
    @Bind(R.id.btn_next_step)
    Button mBtnNextStep;



    int count = 60;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            count--;
            if (count == 0) {
                count = 60;
                mBtnGetSMS.setEnabled(true);
                mBtnGetSMS.setText("重新获取!");
            } else {
                mBtnGetSMS.setText("(" + count + "秒后)重新获取!");
                mBtnGetSMS.setEnabled(false);
                handler.postDelayed(this, 1000);
            }
        }

    };
    @Bind(R.id.rl_pwd)
    RelativeLayout mRlPwd;
    @Bind(R.id.rl_confirm_pwd)
    RelativeLayout mRlConfirmPwd;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_mobile_verification;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.phone_verification;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mTvActionRight.setVisibility(View.INVISIBLE);
        mRlConfirmPwd.setVisibility(View.GONE);
        mRlPwd.setVisibility(View.GONE);

        Intent intent =getIntent();
        if (intent!=null) {
            Bundle user = intent.getBundleExtra("user");
            if(user!=null){
                openid=user.getString("userid");
                userInfo= (OauthUserInfo) user.getSerializable("userInfo");
                Third_type=user.getString("third_type");
            }
        }
    }

    @OnClick(R.id.btn_get_verification_code_sms)
    void clickGetSMS() {
        getCode();
    }

    /**
     * 下一步，就关闭此 窗口
     */
    @OnClick(R.id.btn_next_step)
    void clickNextStep() {
//        bindthirdlogin();
        verifySmsCode();
    }


    /*
   * 获取验证码
   * */
    private void getCode() {
        final String mobileString = mEtPhoneNumber.getText().toString();

        if (!SystemTool.checkNet(Application.getInstance())) {
            Application.showToast("网络连接异常");
            return;
        }
        if (StringUtils.isBlank(mobileString)) {
            Application.showToast("请输入手机号码");
            return;
        }
        if (!StringUtils.isPhoneNumberValid(mobileString)) {
            Application.showToast("手机号码非法");
            return;
        }
        //注册
        if (Third_type.equals(0)) {
            showWaitDialog();
            String accountParam = "{\"function\":\"usernamevalid\",\"username\":\"" + mobileString + "\"}";
            ApiManager.getApi()
                    .checkMobileExist(accountParam)
                    .subscribeOn(Schedulers.io())
                    .flatMap(new Func1<BaseEntry<List<Void>>, Observable<BaseEntry<List<Void>>>>() {
                        @Override
                        public Observable<BaseEntry<List<Void>>> call(BaseEntry<List<Void>> listBaseEntry) {
                            RxUtils.checkResult(listBaseEntry);
                            String param = "{\"function\":\"getidentifynum\",\"username\":\"" + mobileString
                                    + "\",\"mobile\":\"" + mobileString + "\"}";
                            return ApiManager.getApi().getMobileVerifyCode(param);
                        }
                    })
                    .map(new ResultConvertFunc<List<Void>>())
                    .observeOn(AndroidSchedulers.mainThread())
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
                            Application.showToast("验证码已发送");
                            count = 60;
                            handler.post(runnable);
                        }
                    });
        }else {
            //第三方绑定
            showWaitDialog();
            String param = "{\"function\":\"getidentifynum\",\"username\":\"" + mobileString
                    + "\",\"mobile\":\"" + mobileString + "\"}";
            ApiManager.getApi()
                    .checkMobileExist(param)
                    .subscribeOn(Schedulers.io())
                    .map(new ResultConvertFunc<List<Void>>())
                    .observeOn(AndroidSchedulers.mainThread())
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
                            Application.showToast("验证码已发送");
                            count = 60;
                            handler.post(runnable);
                        }
                    });
        }
    }


    private void verifySmsCode() {
        final String mobileString = mEtPhoneNumber.getText().toString();
        final String smsCode = mEtVerificationCode.getText().toString();
        if (StringUtils.isBlank(mobileString)) {
            Application.showToast("请输入手机号码");
            return;
        }
        if (StringUtils.isBlank(smsCode)) {
            Application.showToast("请输入验证码");
            return;
        }
        showWaitDialog();
        String param = "{\"function\":\"mobilevalid\",\"username\":\"" + mobileString
                + "\",\"mobile\":\"" + mobileString + "\",\"identify\":\"" + smsCode + "\"}";
        ApiManager.getApi()
                .verifyMobileCode(param)
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
                        if (Third_type.equals("0")) {
                            hideWaitDialog();
                            Intent intent = new Intent(MobileVerificationActivity.this, PasswordActivity.class);
                            intent.putExtra(PasswordActivity.KEY_MOBILE_NUM, mobileString);
                            startActivityForResult(intent, 0);
                            finish();
                        }else {
                            //是第三方登录跳转至绑定手机
                            bindthirdlogin();
                        }
                    }
                });
    }

    /**
     * 绑定第三方
     */
    private void bindthirdlogin() {
        showWaitDialog();
        String param = "{\"function\":\"bindthirdlogin\",\"username\":\"" + mEtPhoneNumber.getText().toString()
                + "\",\"openid\":\"" + openid + "\",\"third_type\":\"" + Third_type + "\"}";
        ApiManager.getApi()
                .verifyMobileCode(param)
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
//                        SHARE_MEDIA platform = null;
//                        if (Third_type.equals(1)){
//                            platform=SHARE_MEDIA.QQ;
//                        }else if (Third_type.equals(2)){
//                            platform=SHARE_MEDIA.WEIXIN;
//                        }else if (Third_type.equals(3)){
//                            platform=SHARE_MEDIA.SINA;
//                        }
//                        new SignInActivity().onComplete(platform,userInfo);
                        showToast("验证成功请重新登录");
                        finish();
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        }
    }
}
