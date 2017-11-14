package com.jyx.android.activity.sign;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.base.Application;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.net.ApiManager;
import com.jyx.android.net.BizException;
import com.jyx.android.rx.CommonExceptionHandler;
import com.jyx.android.rx.ResultConvertFunc;
import com.jyx.android.utils.StringUtils;
import com.jyx.android.utils.SystemTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by user on 2015/12/13.
 * 忘记密码
 */
public class MobileResetActivity extends BaseActivity {
    private SweetAlertDialog mLoadingDialog;
    private String mobileString;
    private String smsCode;
    private String newPassword;
    @Bind(R.id.et_phone_number)
    EditText mEtPhoneNumber;
    @Bind(R.id.et_verification_code)
    EditText mEtVerificationCode;
    @Bind(R.id.btn_get_verification_code_sms)
    Button mBtnGetSMS;
    @Bind(R.id.btn_next_step)
    Button mBtnNextStep;


    @Bind(R.id.et_password)
    EditText et_password;  //密码
    @Bind(R.id.et_second_password)
    EditText et_second_password;//确认密码

    int count = 60;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            count--;
            if (count == 0) {
                count = 60;
                mBtnGetSMS.setClickable(true);
                mBtnGetSMS.setText("重新获取!");
            } else {
                mBtnGetSMS.setText("(" + count + "秒后)重新获取!");
                mBtnGetSMS.setClickable(false);
                handler.postDelayed(this, 1000);
            }
        }

    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_mobile_verification;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.phone_verification;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @OnClick(R.id.btn_get_verification_code_sms)
    void clickGetSMS() {
        getcode();
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

    /**
     * 下一步，就关闭此 窗口
     */
    @OnClick(R.id.btn_next_step)
    void clickNextStep() {
        mobileString = mEtPhoneNumber.getText().toString();
        smsCode = mEtVerificationCode.getText().toString();
        newPassword=et_password.getText().toString();
        String secpwd=et_second_password.getText().toString();
        if(StringUtils.isBlank(newPassword)){
            Application.showToast("请输入正确的密码");
            return ;
        }
        if(!newPassword.equals(secpwd))
        {
            Application.showToast("两次密码不一样!");
            return;
        }
        if (!CheckPassword(newPassword))
        {
            return ;
        }
        verifySmsCode();
    }

    private void forgetpassword() {
        Map<String, Object> jm = null;
        String xmlString = "";
        jm = new HashMap<>();
        jm.put("function", "forgetpassword");
        jm.put("username", mobileString);
        jm.put("password", newPassword);
        jm.put("mobile", mobileString);
        xmlString = new Gson().toJson(jm);
        showLoading();
        ApiManager.getApi()
                .updatePassword(xmlString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseEntry<List<String>>, String>() {
                    @Override
                    public String call(BaseEntry<List<String>> listBaseEntry) {
                        if (listBaseEntry == null) {
                            throw new BizException(-1, getString(R.string.save_data_error));
                        }

                        if (listBaseEntry.getResult() != 0) {
                            if (listBaseEntry.getMsg().equals(getString(R.string.session_data_error)))
                                new AutomaticLogon(getBaseContext()).login();
                            else
                                throw new BizException(listBaseEntry.getResult(), listBaseEntry.getMsg());
                        }
                        return "";
                    }
                }
                )
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(String da) {
                        mLoadingDialog.setTitleText("重置密码成功!")
                                .setConfirmText("确定")
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        mLoadingDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                mLoadingDialog.dismiss();
                                finish();
                            }
                        });
                        mLoadingDialog.show();
                        finish();
                    }
                });
    }


    /*
   * 获取验证码
   * */
    void  getcode()
    {
        final  String mobileString = mEtPhoneNumber.getText().toString();

        if(!SystemTool.checkNet(Application.getInstance())){
            Application.showToast("网络连接异常");
            return ;
        }
        if(StringUtils.isBlank(mobileString)){
            Application.showToast("请输入手机号码");
            return ;
        }
        if(!StringUtils.isPhoneNumberValid(mobileString)){
            Application.showToast("手机号码非法");
            return ;
        }


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

    /**
     * 校验验证码
     */
    private void verifySmsCode() {
        if (StringUtils.isBlank(mobileString)) {
            Application.showToast("请输入手机号码");
            return;
        }
        if (StringUtils.isBlank(smsCode)) {
            Application.showToast("请输入验证码");
            return;
        }
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
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(List<Void> voids) {
                        forgetpassword();
                    }
                });
    }

    /**
     * @description:检查输入的密码是否符合规则
     * @user yiyi
     * @param password
     */
    public boolean CheckPassword(String password)
    {
        if (password.length() != 6)
        {
            Application.showToastShort("新密码只能为6位");
            return false;
        }

//        Pattern p = Pattern.compile("^(?![^a-zA-Z]+$)(?!\\D+$).{6,}");
        Pattern p = Pattern.compile("^[0-9]*$");
        Matcher m = p.matcher(password);
        if (!m.matches())
        {
            Application.showToastShort("密码必须是数字");
            return false;
        }

        return true;
    }

    @Override
    public void onDestroy(){
        handler.removeCallbacks(runnable);
        super.onDestroy();

    }

}
