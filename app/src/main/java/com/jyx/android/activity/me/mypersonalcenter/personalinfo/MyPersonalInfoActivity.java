package com.jyx.android.activity.me.mypersonalcenter.personalinfo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.activity.me.UserCenterEditGenderActivity;
import com.jyx.android.activity.me.UserCenterEditNicknameActivity;
import com.jyx.android.activity.me.UserCenterEditSignatureActivity;
import com.jyx.android.activity.sign.AutomaticLogon;
import com.jyx.android.base.AskForResult;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.UserEntity;
import com.jyx.android.net.ApiManager;
import com.jyx.android.net.BizException;
import com.jyx.android.rx.CommonExceptionHandler;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * 我的个人信息
 * Created by Even on 2015/11/7.
 */
public class MyPersonalInfoActivity extends BaseActivity {
    @Bind(R.id.iv_portrait)
    SimpleDraweeView ivPortrait;
    @Bind(R.id.iv_my_background)
    SimpleDraweeView ivBackground;
    @Bind(R.id.tv_nick_name)
    TextView tvNickname;
    @Bind(R.id.tv_gender)
    TextView tvGender;
    @Bind(R.id.tv_birthday)
    TextView tvBirthday;
    @Bind(R.id.tv_signature)
    TextView tvSignature;

    private String user_id = "";
    private String birthday="";
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
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_personal_info;
    }

    @Override
    protected int getActionBarCustomView() {
        //return R.layout.toolbar_simple_title_whith_reght_text;
        return R.layout.toolbar_simple_title_whith_reght_text2;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.personal_info;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        user_id = UserRecord.getInstance().getUserId();
        mLoadingDialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在加载数据，请稍候.....");
        mLoadingDialog.setCancelable(false);
        queryUserInfor();
    }

    /**
     * 点击头像
     */
    @OnClick(R.id.rl_portrait)
    void onClickPortrait() {
        Bundle bundle = new Bundle();
        bundle.putInt("flag", 4);
        ActivityHelper.goSelectPicExt(this, bundle);
    }

    /**
     * 点击昵称
     */
    @OnClick(R.id.rl_nickname)
    void onClickNickName() {
        Intent intent = new Intent(this, UserCenterEditNicknameActivity.class);
        intent.putExtra("userid", UserRecord.getInstance().getUserId());
        intent.putExtra("Nickname", tvNickname.getText().toString().trim());
        startActivityForResult(intent, AskForResult.ASK_EDITNICKNAME);
    }

    /**
     * 点击性别
     */
    @OnClick(R.id.rl_gender)
    void onClickGender() {
        Intent intent = new Intent(this, UserCenterEditGenderActivity.class);
        intent.putExtra("userid", UserRecord.getInstance().getUserId());
        intent.putExtra("gender", tvGender.getText().toString().trim());
        startActivityForResult(intent, AskForResult.ASK_EDITGENDER);
    }

    /**
     * 点击生日
     */
    @OnClick(R.id.rl_birthday)
    void onClickBirthday() {
//        Intent intent = new Intent(this, BirthdayEditActivity.class);
//        intent.putExtra("userid", UserRecord.getInstance().getUserId());
//        intent.putExtra("birthday", tvBirthday.getText().toString().trim());
//        startActivityForResult(intent, AskForResult.ASK_BIRTHDAY);
        Dialog dialog = null;
        Calendar  c = Calendar.getInstance();
        dialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
                        tvBirthday.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        saveBirthday();
                    }
                },
                c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH) // 传入天数
        );
        dialog.show();
    }

    /**
     * 点击个性签名
     */
    @OnClick(R.id.rl_signature)
    void onClickSignature() {
        Intent intent = new Intent(this, UserCenterEditSignatureActivity.class);
        intent.putExtra("userid", UserRecord.getInstance().getUserId());
        intent.putExtra("signature", tvSignature.getText().toString().trim());
        startActivityForResult(intent, AskForResult.ASK_EDITSIGNTURE);
    }

    /**
     * 点击更换背景
     */
    @OnClick(R.id.rl_my_background)
    void onClickChangeBackground() {
        Bundle bundle = new Bundle();
        bundle.putInt("flag", 3);
        ActivityHelper.goSelectPicExt(this, bundle);
        //ActivityHelper.goBgSet(this);
    }

    private void queryUserInfor()
    {
        String xmlString = "";
        if (!user_id.equals(""))
        {
            showLoading();

            xmlString = "{\"function\":\"getuserinfor\",\"userid\":\"" + UserRecord.getInstance().getUserId() + "\"}";
            ApiManager.getApi()
                    .getUserInfor(xmlString)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Func1<BaseEntry<List<UserEntity>>, UserEntity>() {
                        @Override
                        public UserEntity call(BaseEntry<List<UserEntity>> listBaseEntry) {
                            if (listBaseEntry == null) {
                                throw new BizException(-1, getString(R.string.load_data_error));
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
                            tvNickname.setText(userEntity.getNickName());
                            tvGender.setText(userEntity.getGender());
                            tvBirthday.setText(userEntity.getBirthday());
                            tvSignature.setText(userEntity.getSignature());
                            String Portrait = userEntity.getPortraitUri();

                            if (Portrait != null) {
                                if (!Portrait.equals("")) {
                                    Uri imageUri = Uri.parse(Portrait);
                                    ivPortrait.setImageURI(imageUri);
                                }
                            }

                            String Backgroud = userEntity.getBackgroundUri();
                            if (Backgroud != null) {
                                if (!Backgroud.equals("")) {
                                    Uri backimageUri = Uri.parse(Backgroud);
                                    ivBackground.setImageURI(backimageUri);
                                }
                            }
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String ret_string = "";

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case AskForResult.ASK_EDITNICKNAME:
                if (resultCode == AskForResult.ASK_RET_OK)
                {
                    if (data != null)
                    {
                        ret_string = data.getStringExtra("Nickname");
                        if (ret_string != null) {
                            tvNickname.setText(ret_string);
                        }
                    }
                }
                break;
            case AskForResult.ASK_EDITGENDER:
                if (resultCode == AskForResult.ASK_RET_OK)
                {
                    if (data != null)
                    {
                        ret_string = data.getStringExtra("gender");
                        if (ret_string != null) {
                            tvGender.setText(ret_string);
                        }
                    }
                }
                break;
            case AskForResult.ASK_EDITSIGNTURE:
                if (resultCode == AskForResult.ASK_RET_OK)
                {
                    if (data != null)
                    {
                        ret_string = data.getStringExtra("signature");
                        if (ret_string != null) {
                            tvSignature.setText(ret_string);
                        }
                    }
                }
                break;
            case AskForResult.ASK_BIRTHDAY:
                if (resultCode == AskForResult.ASK_RET_OK)
                {
                    if (data != null)
                    {
                        ret_string = data.getStringExtra("birthday");
                        if (ret_string != null) {
                            tvBirthday.setText(ret_string);
                        }
                    }
                }
                break;
        }

    }
    public void saveBirthday() {
        SweetAlertDialog sd;
        birthday = tvBirthday.getText().toString().trim();
        Map<String, Object> jm = null;

        if (birthday.equals("")) {
            sd = new SweetAlertDialog(this);
            sd.setTitleText("提示信息");
            sd.setConfirmText("确定");
            sd.setContentText("请输入生日");
            sd.changeAlertType(SweetAlertDialog.WARNING_TYPE);
            sd.setCancelable(true);
            sd.setCanceledOnTouchOutside(true);
            sd.show();
            return;
        }

        String xmlString = "";

        if (!user_id.equals("")) {
            jm = new HashMap<>();
            jm.put("function", "updatebirthday");
            jm.put("userid", UserRecord.getInstance().getUserId());
            jm.put("birthday", birthday);
            xmlString = new Gson().toJson(jm);

            showLoading();

            ApiManager.getApi()
                    .updateNickname(xmlString)
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
                            mLoadingDialog.dismiss();
                            Intent intent = new Intent();
                            intent.putExtra("birthday", birthday);
                            setResult(AskForResult.ASK_RET_OK, intent);
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryUserInfor();
    }
}
