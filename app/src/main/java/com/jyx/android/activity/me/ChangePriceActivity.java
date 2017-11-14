package com.jyx.android.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ChangePriceActivity extends BaseActivity {

    private SweetAlertDialog mLoadingDialog;
    private double money=0.00;
    private double deposit=0.00;
    private String orderid;

    @Bind(R.id.tv_money)
    EditText mMoney;
    @Bind(R.id.tv_price)
    Button mPrice;


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
        return R.layout.activity_change_price;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.amount;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_whith_reght_text2;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        mLoadingDialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在提交请求，请稍候.....");
        mLoadingDialog.setCancelable(false);
        setActionBarTitle(getActionBarTitle());
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        orderid=(bundle.getString("orderid"));
        String a = bundle.getString("deposit");
        a=a.substring(1,a.length());
        deposit= Double.parseDouble(a) * 100;
        mPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double b = Double.parseDouble(mMoney.getText().toString().trim());
                b = b * 100.00;
                if (b<0.00){
                    showToast("金额不能为空");
                }else {
                    money=b;
                    changprice();
                }

            }
        });
    }
    private void changprice(){
        Map<String, Object> jm = null;
        String xmlString = "";

        jm = new HashMap<>();
        jm.put("function", "changeprice");
        jm.put("userid", UserRecord.getInstance().getUserId());
        jm.put("orderid", orderid);
        jm.put("rent",(int)money);
        jm.put("deposit",(int)deposit);
        xmlString = new Gson().toJson(jm);
        showLoading();
        ApiManager.getApi()
                .manageOrder(xmlString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseEntry<List<String>>, List<String>>() {
                    @Override
                    public List<String> call(BaseEntry<List<String>> listBaseEntry) {
                        if (listBaseEntry == null) {
                            throw new BizException(-1, getString(R.string.load_data_error));
                        }

                        if (listBaseEntry.getResult() != 0) {
                            if (listBaseEntry.getMsg().equals(getString(R.string.session_data_error)))
                                new AutomaticLogon(getBaseContext()).login();
                            else
                                throw new BizException(listBaseEntry.getResult(), listBaseEntry.getMsg());
                        }

                        if (listBaseEntry.getData().size()>0) {
                            return listBaseEntry.getData();
                        }
                        else
                        {
                            return null;
                        }
                    }
                })
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(List<String> result) {
                        dismissLoading();
                        finish();
//                        refresh();
//                        Intent intent = new Intent(RETURNED_MSG);
//                        intent.putExtra("mtype", String.valueOf(TYPE_RETURNED));
//                        //发送本地广播
//                        localBroadcastManager.sendBroadcast(intent);
                    }
                });
    }


}
