package com.jyx.android.activity.me.mypersonalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.activity.sign.AutomaticLogon;
import com.jyx.android.base.AskForResult;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BankCardEntity;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.WithdrawEntity;
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
 * Created by Administrator on 2016/3/8.
 */
public class WithdrawActivity extends BaseActivity {
    @Bind(R.id.tv_withdraw_username)
    TextView mTvUsername;
    @Bind(R.id.tv_withdraw_cardno)
    TextView mTvCardno;
    @Bind(R.id.tv_withdraw_balance)
    TextView mTvBalance;
    @Bind(R.id.et_withdraw_amount)
    TextView mEtAmount;
    @Bind(R.id.ll_withdraw_error)
    LinearLayout mLlMsg;
    @Bind(R.id.btn_withdraw)
    Button mBtnWithdraw;


    private String Balance;
    private String user_id;

    private double amount = 0.00;
    private double balance = 0.00;

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

    private void RefreshBank() {
        Map<String, Object> jm = null;

        String xmlString = "";

        user_id = UserRecord.getInstance().getUserId();
        if (!user_id.equals("")) {
            jm = new HashMap<>();
            jm.put("function", "getbankcard");
            jm.put("userid", user_id);
            xmlString = new Gson().toJson(jm);

            showLoading();

            ApiManager.getApi()
                    .getBankCard(xmlString)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Func1<BaseEntry<List<BankCardEntity>>, List<BankCardEntity>>() {
                        @Override
                        public List<BankCardEntity> call(BaseEntry<List<BankCardEntity>> listBaseEntry) {
                            if (listBaseEntry == null) {
                                throw new BizException(-1, getString(R.string.save_data_error));
                            }

                            if (listBaseEntry.getResult() != 0) {
                                if (listBaseEntry.getMsg().equals(getString(R.string.session_data_error)))
                                    new AutomaticLogon(getBaseContext()).login();
                                else
                                throw new BizException(listBaseEntry.getResult(), listBaseEntry.getMsg());
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
                        public void onNext(List<BankCardEntity> mybank) {
                            dismissLoading();
                            if (mybank != null) {
                                BankCardEntity bank =mybank.get(0);
                                mTvUsername.setText(bank.getAccount());
                                mTvCardno.setText(bank.getCardno());
                                mLlMsg.setVisibility(View.GONE);
                                mBtnWithdraw.setVisibility(View.VISIBLE);
                            } else {
                                mTvUsername.setText("");
                                mTvCardno.setText("");
                                mLlMsg.setVisibility(View.VISIBLE);
                                mBtnWithdraw.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }

    private void Withdraw(String password) {

        Map<String, Object> jm = null;

        String xmlString = "";

        user_id = UserRecord.getInstance().getUserId();
        if (!user_id.equals("")) {
            jm = new HashMap<>();
            jm.put("function", "withdraw");
            jm.put("userid", user_id);
            jm.put("password",password);
            jm.put("amount",(int)amount);
            xmlString = new Gson().toJson(jm);

            showLoading();

            ApiManager.getApi()
                    .Withdraw(xmlString)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Func1<BaseEntry<List<WithdrawEntity>>, List<WithdrawEntity>>() {
                        @Override
                        public List<WithdrawEntity> call(BaseEntry<List<WithdrawEntity>> listBaseEntry) {
                            if (listBaseEntry == null) {
                                throw new BizException(-1, getString(R.string.save_data_error));
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
                    .subscribe(new Subscriber<List<WithdrawEntity>>() {
                        @Override
                        public void onNext(final List<WithdrawEntity> withdrawEntityList) {
                            mLoadingDialog.setTitleText("提现成功!")
                                    .setConfirmText("确定")
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            mLoadingDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    if (withdrawEntityList != null) {
                                        WithdrawEntity w = withdrawEntityList.get(0);
                                        Intent intent = new Intent();
                                        intent.putExtra("balance", w.getBalance());
                                        setResult(AskForResult.ASK_RET_OK, intent);
                                    }
                                    finish();
                                    mLoadingDialog.dismiss();
                                }
                            });
                        }
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissLoading();
                            CommonExceptionHandler.handleBizException(e);
                        }


                    });
        }
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdraw;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_withdraw;
    }

    @OnClick(R.id.btn_withdraw)
    void OnWithdrawClick()
    {
        double b = Double.parseDouble(mEtAmount.getText().toString().trim());
        b = b * 100.00;
        SweetAlertDialog sd;

        if (b<=0.00) {
            sd = new SweetAlertDialog(this);
            sd.setTitleText("提示信息");
            sd.setConfirmText("确定");
            sd.setContentText("提现金额必须大于0！");
            sd.changeAlertType(SweetAlertDialog.WARNING_TYPE);
            sd.setCancelable(true);
            sd.setCanceledOnTouchOutside(true);
            sd.show();
            return;
        }

        if (b > balance) {
            sd = new SweetAlertDialog(this);
            sd.setTitleText("提示信息");
            sd.setConfirmText("确定");
            sd.setContentText("提现金额不能超过账户余额！");
            sd.changeAlertType(SweetAlertDialog.WARNING_TYPE);
            sd.setCancelable(true);
            sd.setCanceledOnTouchOutside(true);
            sd.show();
            return;
        }

        amount = b;

        ActivityHelper.goPayThePasswordForResult(WithdrawActivity.this, 10);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Balance = getIntent().getStringExtra("balance");
        mLoadingDialog=new SweetAlertDialog(this);
        balance = Double.parseDouble(Balance) * 100.00;
        mTvBalance.setText(Balance);
        user_id = UserRecord.getInstance().getUserId();
        mEtAmount.setText("0.00");

        RefreshBank();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            Bundle bundle = data.getExtras();
            switch (requestCode) {
                case 1:
                    if (null != bundle) {

                    }
                    break;

                case 10:
                    if (null != bundle) {
                        if (!bundle.getBoolean("isCancelPay")) {
                            String pwd = bundle.getString("password");
                            Log.e("pwd",pwd);
                            Withdraw(pwd);
                        } else {
                            Toast.makeText(getBaseContext(),R.string.buy_right_now_pay_cancel,Toast.LENGTH_SHORT).show();
//                            showPayDialog(getResources().getString(R.string.buy_right_now_pay_cancel));
                        }
                    }
                    break;
                default:
                    break;
            }
        } else {
//            Log.e(TAG, "pay cancel");
        }
    }
}
