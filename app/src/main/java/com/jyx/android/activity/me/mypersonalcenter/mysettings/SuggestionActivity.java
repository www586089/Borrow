package com.jyx.android.activity.me.mypersonalcenter.mysettings;

import android.os.Bundle;
import android.widget.EditText;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.activity.sign.AutomaticLogon;
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
 * Created by Administrator on 2016/3/4.
 */
public class SuggestionActivity extends BaseActivity {
    @Bind(R.id.et_suggestion)
    EditText mEtSuggestion;

    private String user_id = "";
    private String suggestion = "";

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
        return R.layout.activity_suggestion;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_me_uc_edit_signature;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        user_id = UserRecord.getInstance().getUserId();

        mLoadingDialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在保存数据，请稍候.....");
        mLoadingDialog.setCancelable(false);
    }

    private void saveSuggestion()
    {
        Map<String, Object> jm = null;
        String xmlString = "";

        suggestion = mEtSuggestion.getText().toString().trim();

        if (!user_id.equals("") && !suggestion.equals("")) {
            jm = new HashMap<>();
            jm.put("function", "submitsuggestion");
            jm.put("userid", UserRecord.getInstance().getUserId());
            jm.put("suggestion", suggestion);
            xmlString = new Gson().toJson(jm);

            showLoading();

            ApiManager.getApi()
                    .submitSuggestion(xmlString)
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
                            mLoadingDialog.setTitleText("提交建议成功!")
                                    .setConfirmText("确定")
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            mLoadingDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    finish();
                                    mLoadingDialog.dismiss();
                                }
                            });
                        }
                    });
        }
    }

    @OnClick(R.id.btn_suggestion_save)
    void clickOK()
    {
        saveSuggestion();
    }
}
