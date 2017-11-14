package com.jyx.android.activity.discovery.event;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.base.Application;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.param.EnrollEventParam;
import com.jyx.android.net.ApiManager;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zfang on 2016-03-12
 */

//这里是中文部分
public class EnrollEventActivity extends BaseActivity {

    private String TAG = "EnrollEventActivity";
    @Bind(R.id.et_event_enroll_mobile)
    EditText et_event_enroll_mobile;
    @Bind(R.id.et_event_enroll_name)
    EditText et_event_enroll_name;
    @Bind(R.id.et_event_enroll_address)
    EditText et_event_enroll_address;
    @Bind(R.id.rl_event_enroll)
    RelativeLayout rl_event_enroll;
    private boolean isAdd = true;
    private String addressId = null;
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
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title_with_right_text;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_enroll_event;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.event_detail_enroll_title;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionBarTitle(getActionBarTitle());
        setActionRightText("");
        setClickListener();
        initLoadingDialog();
    }

    private void initLoadingDialog() {
        mLoadingDialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE).setTitleText(getString(R.string.event_enroll_submiting_str));
        mLoadingDialog.setCancelable(false);
    }
    private void setClickListener() {
        rl_event_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSubmit();
            }
        });
    }

    @OnClick(R.id.btn_event_enroll)
    void clickSubmit() {
        if (!checkData()) {
            return;
        }
        showLoading();
        EnrollEventParam xml = new EnrollEventParam();
        xml.setFunction("activityenroll");
        xml.setUserid(UserRecord.getInstance().getUserId());
        xml.setMobile(et_event_enroll_mobile.getText().toString());
        xml.setName(et_event_enroll_name.getText().toString());
        xml.setAddress(et_event_enroll_address.getText().toString());
        Call<BaseEntry<List<Void>>> result = ApiManager.getApi().activityEnroll(new Gson().toJson(xml).toString());
        result.enqueue(new Callback<BaseEntry<List<Void>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<Void>>> response) {
                Log.e(TAG, "onResponse");
                if (response.isSuccess()) {
                    BaseEntry<List<Void>> body = response.body();
                    if (0 == body.getResult()) {
                        ActivityHelper.goEnrollSuccessActivity(EnrollEventActivity.this);
                        finish();
                        return;
                    }
                }
                dismissLoading();
                showMessageDialog(false, getResources().getString(R.string.event_enroll_failure));
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onFailure");
                dismissLoading();
                showMessageDialog(false, getResources().getString(R.string.event_enroll_failure));
            }
        });
    }

    private boolean checkData() {
        if (TextUtils.isEmpty(et_event_enroll_mobile.getText().toString())) {
            Application.showToast(R.string.event_enroll_check_mobile);
            return false;
        }
        if (TextUtils.isEmpty(et_event_enroll_name.getText().toString())) {
            Application.showToast(R.string.event_enroll_check_name);
            return false;
        }
        if (TextUtils.isEmpty(et_event_enroll_address.getText().toString())) {
            Application.showToast(R.string.event_enroll_check_address);
            return false;
        }
        return true;
    }

    private void showMessageDialog(final boolean success, String msg) {
        SweetAlertDialog sd;
        sd = new SweetAlertDialog(this);
        sd.setTitleText("提示信息");
        sd.setConfirmText("确定");
        sd.setContentText(msg);
        sd.changeAlertType(SweetAlertDialog.WARNING_TYPE);
        sd.setCancelable(true);
        sd.setCanceledOnTouchOutside(true);
        sd.show();
        sd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (success) {
                    finish();
                }
            }
        });
    }
}
