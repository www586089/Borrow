package com.jyx.android.activity.me;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.activity.sign.AutomaticLogon;
import com.jyx.android.base.AskForResult;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
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
 * Created by Administrator on 2016/3/2.
 */
public class BirthdayEditActivity extends BaseActivity {
    @Bind(R.id.tv_birthday_set)
    TextView mTvBirthday;

    private String user_id = "";
    private String birthday = "";

    private SweetAlertDialog mLoadingDialog;

    private Calendar c = null;

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
        return R.layout.activity_birthday_set;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_birthday;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        user_id = getIntent().getStringExtra("userid");
        birthday = getIntent().getStringExtra("birthday");

        mLoadingDialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在保存数据，请稍候.....");
        mLoadingDialog.setCancelable(false);

        if (birthday != null) {
            mTvBirthday.setText(birthday);
        }
        else
        {
            mTvBirthday.setText("");
        }
    }

    private void saveBirthday() {
        SweetAlertDialog sd;
        birthday = mTvBirthday.getText().toString().trim();
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
                            mLoadingDialog.setTitleText("更新生日成功!")
                                    .setConfirmText("确定")
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            mLoadingDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    Intent intent = new Intent();
                                    intent.putExtra("birthday", birthday);
                                    setResult(AskForResult.ASK_RET_OK, intent);
                                    finish();
                                    mLoadingDialog.dismiss();
                                }
                            });
                        }
                    });
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        c = Calendar.getInstance();
        dialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
                        mTvBirthday.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                },
                c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH) // 传入天数
        );
        return dialog;
    }

    @OnClick(R.id.btn_birthday_save)
    void clickSave()
    {
        saveBirthday();
    }

    @OnClick(R.id.btn_birthday_set)
    void clickSet()
    {
        showDialog(0);
    }
}
