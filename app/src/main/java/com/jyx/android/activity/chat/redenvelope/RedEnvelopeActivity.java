package com.jyx.android.activity.chat.redenvelope;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.base.Application;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.RedEnvelopBean;
import com.jyx.android.model.param.RedEnvelopeParam;
import com.jyx.android.net.ApiManager;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yiyi on 2015/10/28.
 * 发红包
 */
public class RedEnvelopeActivity extends BaseActivity {
    @Bind(R.id.et_redenvelope_amount)
    EditText mEtAmout;
    @Bind(R.id.et_redenvelope_msg)
    EditText mEtMsg;
    @Bind(R.id.tv_redenvelope_amount_txt)
    TextView mTvAmoutTxt;
    private String desc;
    private double money;
    private String targetUserId;

    /**
     * @description:设置输入框中小数位数控制，当输入货币时小数位数为2
     * @user yiyi
     */
    private class MoneyTextWatcher implements TextWatcher {
        private boolean isChanged = false;
        EditText moneyedit;

        public MoneyTextWatcher(EditText et) {
            super();
            this.moneyedit = et;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isChanged) {//如果字符未改变则返回
                return;
            }
            String str = s.toString();

            isChanged = true;

            //去除前面的零及数字中的逗号
            String moneyValue = str.replaceFirst("^0+", "");
            moneyValue = moneyValue.replaceAll(",", "");

            //分割小数部分与整数部分
            String Str1 = "",Str2 = "",Temps = "";
            int dot_pos = moneyValue.indexOf('.');
            String int_value;
            String fraction_value;
            if (dot_pos == -1) {
                int_value = moneyValue;
                fraction_value = "00";
                Str1 = int_value.length() == 0 ? "0" : int_value;
                Str2 = (int_value.length() == 0 ? "0" : int_value) + "." + fraction_value;
            } else {
                int_value = moneyValue.substring(0, dot_pos);
                fraction_value = moneyValue.substring(dot_pos, moneyValue.length());
                /* 显示小数点后两位 */
                Temps = fraction_value + "00";
                Str2 = (int_value.length() == 0 ? "0" : int_value) + Temps.substring(0, 3);
                if(fraction_value.length() <= 3){
                    Str1 = (int_value.length() == 0 ? "0" : int_value) + fraction_value;
                }
                else {
                    Str1 = (int_value.length() == 0 ? "0" : int_value) + fraction_value.substring(0, 3);
                }
            }

            moneyedit.setText(Str1);
            moneyedit.setSelection(moneyedit.length());

            mTvAmoutTxt.setText(getCurrencySymbol("CNY") + Str2);

            isChanged = false;
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_redenvelope;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_redenvelope;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }

    /**
     * @description:根据货币代码（及语言环境）获取货币符号
     * @user yiyi
     * @param currencyCode
     */
    public String getCurrencySymbol(String currencyCode)
    {
        String currencySymbol = null;
        currencyCode = currencyCode.toUpperCase();
        Currency currency = Currency.getInstance(currencyCode);
        Locale locale = this.getResources().getConfiguration().locale;
        try
        {
            currencySymbol = currency.getSymbol(locale);
        }
        catch (IllegalArgumentException e)
        {
            currencySymbol = "";
        }
        return currencySymbol;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mTvAmoutTxt.setText(getCurrencySymbol("CNY") + "0.00");
        mEtAmout.addTextChangedListener(new MoneyTextWatcher(mEtAmout));
        targetUserId = getIntent().getStringExtra("targetId");
        setActionRightText("");
    }

    @OnClick(R.id.btn_redenvelope_send)
    void clickSend() {
         desc = mEtMsg.getText().toString();
        if(TextUtils.isEmpty(desc)){
            desc="恭喜发财，大吉大利";
        }
         String sum = mEtAmout.getText().toString();
         money = Double.parseDouble(sum);
        if(TextUtils.isEmpty(sum)){
            showToast("请输入金额");
            return;
        }

        startActivityForResult(new Intent(this,PayThePasswordActivity.class),0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(data.getBooleanExtra("isCancelPay",true)){
                showPayDialog("取消支付");
            }else{
                String password = data.getStringExtra("password");
                int amount = (int) (money * 100);
                sendHongbao(targetUserId,password,amount,desc);
            }
        }
    }

    /**
     * {"function":"personalredpacket","userid":"132512379702379520","password":"tt","targetuser":"153561152041812992","amount":"99","note":"测试"}
     userid-用户ID
     password-用户密码
     targetuser-目标用户ID
     amount-红包金额(分)
     note-信息
     返回:
     {
     "result": "0",
     "msg": "操作成功",
     "datas": [
     {
     "redpackets_id": "162002469570778112"
     }
     ]
     }
     redpackets_id-红包ID
     */

    private void sendHongbao(final String  targetUserId, String password,final
    int amount,String note){

        RedEnvelopeParam xml = new RedEnvelopeParam();
        xml.setFunction("personalredpacket");
        xml.setUserid(UserRecord.getInstance().getUserId());
        xml.setAmount(String.valueOf(amount));
        xml.setTargetuser(targetUserId);
        xml.setPassword(password);
        xml.setNote(note);
        Call<BaseEntry<List<RedEnvelopBean>>> result = ApiManager.getApi().sendRedEnvelop(new
                Gson().toJson(xml));
        result.enqueue(new Callback<BaseEntry<List<RedEnvelopBean>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<RedEnvelopBean>>> response) {
                if (response.isSuccess()) {
                    BaseEntry<List<RedEnvelopBean>> body = response.body();
                    if (0 == body.getResult()) {
                        RedEnvelopBean bean = (RedEnvelopBean) body.getData()
                                .get(0);
                        String redPacketId = bean.getRedpackets_id();
                        setResult(RESULT_OK,new Intent().putExtra("description",desc).putExtra
                                ("sum",money).putExtra("number",1).putExtra
                                ("isGroup",0).putExtra("redPacketsId",
                                redPacketId));
                        finish();
                    } else {
                        showPayDialog(body.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Application.showToast(R.string.buy_right_now_pay_failure);
            }
        });
    }

    private void showPayDialog(String msg) {
        SweetAlertDialog sd;
        sd = new SweetAlertDialog(this);
        sd.setTitleText("提示信息");
        sd.setConfirmText("确定");
        sd.setContentText(msg);
        sd.changeAlertType(SweetAlertDialog.WARNING_TYPE);
        sd.setCancelable(true);
        sd.setCanceledOnTouchOutside(true);
        sd.show();
    }
}
