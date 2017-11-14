package com.jyx.android.activity.me.bankcard;

import android.os.Bundle;
import android.widget.EditText;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.activity.sign.AutomaticLogon;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BankCardEntity;
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
 * Created by yiyi on 2015/10/27.
 * 添加银行卡
 */
public class AddCardActivity extends BaseActivity {
    @Bind(R.id.et_addcard_username)
    EditText mEtUsername;
    @Bind(R.id.et_addcard_cardno)
    EditText mEtCardno;

    private String user_id = "";
    private String username = "";
    private String bankNo = "";

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
        return R.layout.activity_addcard;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_addcard;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        user_id = getIntent().getStringExtra("user_Id");
        mLoadingDialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在加载数据，请稍候.....");
        mLoadingDialog.setCancelable(false);
        queryBankcard();
    }

    private void queryBankcard()
    {
        String xmlString = "";
        if (!user_id.equals(""))
        {
            showLoading();

            xmlString = "{\"function\":\"getbankcard\",\"userid\":\"" + user_id + "\"}";
            ApiManager.getApi()
                    .getBankCard(xmlString)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Func1<BaseEntry<List<BankCardEntity>>, List<BankCardEntity>>() {
                        @Override
                        public List<BankCardEntity> call(BaseEntry<List<BankCardEntity>> listBaseEntry) {
                            if (listBaseEntry == null) {
                                throw new BizException(-1, getString(R.string.load_data_error));
                            }

                            if (listBaseEntry.getResult() != 0) {
                                if (listBaseEntry.getMsg().equals(getString(R.string.session_data_error)))
                                    new AutomaticLogon(getBaseContext()).login();
                                else
                                throw new BizException(listBaseEntry.getResult(), listBaseEntry.getMsg());
//                                new AutomaticLogon(getBaseContext()).login();
                            }
                            return listBaseEntry.getData();
                        }
                    })
                    .subscribe(new Subscriber<List<BankCardEntity>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissLoading();
//                            CommonExceptionHandler.handleBizException(e);
                        }

                        @Override
                        public void onNext(List<BankCardEntity> bankCardEntity) {
                            dismissLoading();
                            if (bankCardEntity != null)
                            {
                                BankCardEntity bank = bankCardEntity.get(0);
                                mEtCardno.setText(bank.getCardno());
                                mEtUsername.setText(bank.getAccount());
                            }
                            else
                            {
                                mEtCardno.setText("");
                                mEtUsername.setText("");
                            }
                        }
                    });
        }
    }

    private void saveBankcard()
    {
        SweetAlertDialog sd;
        Map<String, Object> jm = null;

        username = mEtUsername.getText().toString().trim();
        bankNo = mEtCardno.getText().toString().trim();

        if (username.equals(""))
        {
            sd = new SweetAlertDialog(this);
            sd.setTitleText("提示信息");
            sd.setConfirmText("确定");
            sd.setContentText("请输入持卡人姓名");
            sd.changeAlertType(SweetAlertDialog.WARNING_TYPE);
            sd.setCancelable(true);
            sd.setCanceledOnTouchOutside(true);
            sd.show();
            return;
        }
        if (bankNo.equals(""))
        {
            sd = new SweetAlertDialog(this);
            sd.setTitleText("提示信息");
            sd.setConfirmText("确定");
            sd.setContentText("请输入卡号");
            sd.changeAlertType(SweetAlertDialog.WARNING_TYPE);
            sd.setCancelable(true);
            sd.setCanceledOnTouchOutside(true);
            sd.show();
            return;
        }

        String xmlString = "";

        if (!user_id.equals("")) {
            jm = new HashMap<>();
            jm.put("function", "addbankcard");
            jm.put("userid", UserRecord.getInstance().getUserId());
            jm.put("account", username);
            jm.put("cardno", bankNo);
            xmlString = new Gson().toJson(jm);

            mLoadingDialog.setTitleText("正在保存数据，请稍候.....")
                    .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
            mLoadingDialog.setCancelable(false);

            showLoading();

            ApiManager.getApi()
                    .addBankCard(xmlString)
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
                            mLoadingDialog.setTitleText("绑定银行卡成功!")
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

    @OnClick(R.id.btn_addcard_adddnow)
    void clickBindNow()
    {
        saveBankcard();
    }
}
