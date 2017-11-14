package com.jyx.android.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

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
 * Created by Tonlin on 2015/10/27.
 */
public class UserCenterEditGenderActivity extends BaseActivity {
    @Bind(R.id.cb_editgender_male)
    CheckBox mCbMale;
    @Bind(R.id.cb_editgender_female)
    CheckBox mCbFemale;

    private String user_id = "";
    private String gender = "";
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
        return R.layout.activity_me_uc_edit_gender;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_me_uc_edit_gender;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        user_id = getIntent().getStringExtra("userid");
        gender = getIntent().getStringExtra("gender").trim();

        mLoadingDialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在保存数据，请稍候.....");
        mLoadingDialog.setCancelable(false);

        if (gender == null) {
            gender = "男";
            mCbMale.setChecked(true);
            mCbFemale.setChecked(false);
        }
        else
        {
            if (gender.equals("男"))
            {
                mCbMale.setChecked(true);
                mCbFemale.setChecked(false);
            }
            else
            {
                mCbMale.setChecked(false);
                mCbFemale.setChecked(true);
            }
        }

        mCbMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    gender = "男";
                    mCbFemale.setChecked(false);
                }
                else
                {
                    gender = "女";
                    mCbFemale.setChecked(true);
                }
                saveGender();
            }
        });
        mCbFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    gender = "女";
                    mCbMale.setChecked(false);
                }
                else
                {
                    gender = "男";
                    mCbMale.setChecked(true);
                }
                saveGender();
            }
        });
    }

    private void saveGender()
    {
        Map<String, String> jm = null;

        String xmlString = "";

        if (!user_id.equals("")) {
            jm = new HashMap<>();
            jm.put("function", "updategender");
            jm.put("userid", UserRecord.getInstance().getUserId());
            jm.put("gender", gender);
            xmlString = new Gson().toJson(jm);

            showLoading();

            ApiManager.getApi()
                    .updateGender(xmlString)
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
                            Intent intent = new Intent();
                            intent.putExtra("gender", gender);
                            setResult(AskForResult.ASK_RET_OK, intent);
                            finish();
                            mLoadingDialog.dismiss();
                        }
                    });
        }
    }

    @OnClick(R.id.btn_editgender_ok)
    void clickOK()
    {
        saveGender();
    }
}
