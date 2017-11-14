package com.jyx.android.activity.me.mypersonalcenter.mysettings;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.activity.sign.AutomaticLogon;
import com.jyx.android.base.Application;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.net.ApiManager;
import com.jyx.android.net.BizException;
import com.jyx.android.rx.CommonExceptionHandler;

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
 * Created by yiyi on 2015/10/29.
 * 密码修改
 */
public class ChangePasswordActivity extends BaseActivity {
    @Bind(R.id.et_changepassword_old)
    EditText mEtPasswordOld;
    @Bind(R.id.et_changepassword_new1)
    EditText mEtPasswordNew1;
    @Bind(R.id.et_changepassword_new2)
    EditText mEtPasswordNew2;

    String Password_Old = "",Password_New1 = "", Password_New2 = "";

    private String user_id = "";

    private SweetAlertDialog mLoadingDialog;

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
    protected int getLayoutId() {
        return R.layout.activity_changepassword;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        user_id = UserRecord.getInstance().getUserId();
        TextView textView= (TextView) findViewById(R.id.tv_right);
        textView.setVisibility(View.GONE);
        mLoadingDialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在保存数据，请稍候.....");
        mLoadingDialog.setCancelable(false);
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_changepassword;
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

    /**
     * @description:检查用户输入是否正确
     * @user yiyi
     */
    public boolean CheckInput()
    {
        if (Password_Old.length() <= 0)
        {
            Application.showToastShort("请输入旧密码");
            return false;
        }

        if (!CheckPassword(Password_New1))
        {
            return false;
        }

        if (!Password_New1.equals(Password_New2))
        {
            Application.showToastShort("两次输入的密码不一致");
            return false;
        }

        return true;
    }

    public void SavePassword()
    {
        Map<String, Object> jm = null;
        String xmlString = "";

        if (!user_id.equals("")) {
            jm = new HashMap<>();
            jm.put("function", "updatepassword");
            jm.put("userid", UserRecord.getInstance().getUserId());
            jm.put("password", Password_Old);
            jm.put("newpassword", Password_New2);
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
                    })
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
                            mLoadingDialog.setTitleText("修改登录密码成功!")
                                    .setConfirmText("确定")
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            mLoadingDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    mLoadingDialog.dismiss();
                                    finish();
                                }
                            });
                        }
                    });
        }
    }

    @OnClick(R.id.btn_changepassword_save)
    void clickSave()
    {
        Password_Old = mEtPasswordOld.getText().toString().trim();
        Password_New1 = mEtPasswordNew1.getText().toString().trim();
        Password_New2 = mEtPasswordNew2.getText().toString().trim();

        if (!CheckInput())
        {
            return;
        }

        SavePassword();
    }
}
