package com.jyx.android.activity.buy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.activity.buy.utils.PayResult;
import com.jyx.android.activity.buy.utils.SignUtils;
import com.jyx.android.base.Application;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.Constants;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.AnOrderBean;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.ItemBean;
import com.jyx.android.model.WXPrePayBean;
import com.jyx.android.model.param.GetWXPrayPayIdParam;
import com.jyx.android.model.param.PlaceAnOrderParam;
import com.jyx.android.model.param.WXPayNotifyParam;
import com.jyx.android.model.param.WalletPayParam;
import com.jyx.android.net.ApiManager;
import com.jyx.android.utils.StringUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.weibo.sdk.android.component.sso.tools.MD5Tools;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zfang on 2015/10/25.
 */
public class BuyRightNowActivity extends BaseActivity {

    private String TAG = "BuyRightNowActivity";
    @Bind(R.id.buy_right_now_item_img)
    SimpleDraweeView buyRightNowItemImg;
    @Bind(R.id.buy_right_now_item_title)
    TextView buyRightNowItemTitle;
    @Bind(R.id.buy_right_now_item_des)
    TextView buyRightNowItemDes;
    @Bind(R.id.buy_right_now_item_origin_price)
    TextView buyRightNowOriginPice;
    @Bind(R.id.buy_right_now_item_price)
    TextView getBuyRightNowItemPrice;
    @Bind(R.id.buy_right_now_receiver)
    TextView buyRightNowReceiver;
    @Bind(R.id.buy_right_now_phone)
    TextView buyRightNowPhone;
    @Bind(R.id.buy_right_now_address_detail)
    TextView buyRightNowAddressDetail;

    @Bind(R.id.rl_buy_right_now_transfer)
    RelativeLayout rl_buy_right_now_transfer;
    @Bind(R.id.tv_buy_right_now_discount_price)
    TextView tv_buy_right_now_discount_price;
    @Bind(R.id.rl_buy_right_now_gift)
    RelativeLayout rl_buy_right_now_gift;
    @Bind(R.id.tv_transfer_des)
    TextView tv_transfer_des;
    @Bind(R.id.rl_buy_right_now_rent)
    RelativeLayout rl_buy_right_now_rent;
    @Bind(R.id.buy_right_now_deposit)
    TextView buy_right_now_deposit;
    @Bind(R.id.buy_right_now_rent_per_unit)
    TextView buy_right_now_rent_per_unit;
    @Bind(R.id.rg_buy_right_now_select)
    RadioGroup rg_buy_right_now_select;

    @Bind(R.id.buy_right_now_alipay_bkg)
    ImageView mImgAlipayBkg;
    @Bind(R.id.buy_right_now_weixin_bkg)
    ImageView mImgWeiXinBkg;
    @Bind(R.id.submit_order_ll)
    LinearLayout mSubmitOrder;

    @Bind(R.id.pay_order_wallet_ll)
    LinearLayout pay_order_wallet_ll;
    @Bind(R.id.buy_right_now_wallet_pay_bkg)
    ImageView buy_right_now_wallet_pay_bkg;


    private String itemDescrible = null;
    private String itemName = null;
    private String itemPrice = null;
    private int operateType = 100;
    private List<String> listImages = new ArrayList<String>();
    private Bundle bundle = null;
    private ItemBean item = null;
    private boolean isSelectedWallet = false;
    private String addressId = null;
    private String orderId = null;
    private String wxPrepayId = null;
    private int monthNum = -1;
    //商户PID
    public  String PARTNER = "2088012830969655";
    //商户收款账号
    public String SELLER = "2088012830969655";
    //商户私钥，pkcs8格式
    public String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKfyzK405MSpoVaPc8LBDDCMM625YnKAv9+rBFLglIshQkzZJ4Nj6wtTrei1hzJSPI3wzhCnYlmJ22HNTNYuBHb/L6cJznVG4JbqmxSVh5wSwU0z+qOYdDVOCaA9FGnp0IZ9uQSFUc7wjC2bLmT5U6C+CVxecOL2LPUvJAyu0WtVAgMBAAECgYBcyoAdy+mNYq9aNN3ff2ttszimRiPHYa0hNcqow54ulIyeCuiS5nSpiG0wgoMU4WzFe4NOnjxvfgQwsA5A06Hw+/kNUtkg1SYlfAjO6BE1UjqaHmJxmJj32fn5gYumxccpR+S0tYpoUmPfSTE4//98aH65k3n5RNe2/QOZIPWx7QJBANMhno31yjxzbOlYl88P1E9ugDuozlAAQABr+l5Tjf0P0cO7o1aNx99ymWEB0Qs1KMf1NLQvzTKAMMdGxigVaP8CQQDLo9wge9j1dr1Slaj6avziZZyVIwW6EJ7hDJLnj8FWnQI/2O+tmEWd+gcQ3DJpQxFEyCFJU6q9L0ZVkBj/irerAkB/L+mMK11BPWeGys/o9Og5UZ/+UJq4pu3nUyToXf1TTYxYFVk4cwDNIwQy5B1CWyvOCXFOet1qZr77zS7iRsORAkBi6cete5qpuDBDofA/GqKU8Vg6VJWeiB40ICBnG7uEb6U9EAlzX6hJEL5H4T0XRf1oVHFepaYkWIcoXmLWFTIbAkEAnlwUhX79Ydxd5uZy4lKCoCYTz+ylgyjx1I2o4PBzz8WQjtYOQKgwJwTINNAtVwt9YssEa+9mBN6pVFtoipPtDg==";
    //支付宝公钥
    public String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                        String resultInfo = payResult.getResult();

                        String resultStatus = payResult.getResultStatus();

                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        if (TextUtils.equals(resultStatus, "9000")) {
                            Toast.makeText(BuyRightNowActivity.this, "支付成功",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(BuyRightNowActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            Toast.makeText(BuyRightNowActivity.this, "用户取消支付",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(BuyRightNowActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(BuyRightNowActivity.this, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        };
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getBooleanExtra("success",false)) {
                ActivityHelper.goBuyPaySuccess(BuyRightNowActivity.this);
                doWXPayNotify(orderId, wxPrepayId);
                finish();
            } else {
                Log.e(TAG, "Pay failure.");
                Application.showToast(R.string.buy_right_now_pay_failure);
            }
        }
    };
    @Override
    protected int getLayoutId() {
        return R.layout.activity_buy_right_now;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.buy_right_now_title_center;
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
        setActionBarTitle(getActionBarTitle());
        setActionRightText("");
        getBundleData();
        initWidget();
        registReceiver();
        setRadioGroupListener();
        address();
    }
    private void address(){
        SharedPreferences sp=getSharedPreferences("address",0);
        buyRightNowReceiver.setText(sp.getString("name","").toString().trim());
        buyRightNowPhone.setText("手机号："+sp.getString("phone","").toString().trim());
        buyRightNowAddressDetail.setText(sp.getString("address","").toString().trim());
        addressId = bundle.getString(sp.getString("address_id","").toString().trim());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void registReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("WXPayEntityActivity");
        registerReceiver(receiver, intentFilter);
    }

    private void setRadioGroupListener() {
        rg_buy_right_now_select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_buy_right_now_1:
                        monthNum = 1;
                        break;
                    case R.id.rb_buy_right_now_2:
                        monthNum = 3;
                        break;
                    case R.id.rb_buy_right_now_3:
                        monthNum = 6;
                        break;
                    case R.id.rb_buy_right_now_4:
                        monthNum = 12;
                        break;
                    default:
                        Log.e(TAG, "unknow id");
                }

                setPrice(getBuyRightNowItemPrice);
            }
        });
    }

    private void setPrice(TextView priceTv) {
        double rentNum = 0;
        double depositNum = 0;
        try {
            rentNum = Double.valueOf(item.getRent());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        try {
            depositNum = Double.valueOf(item.getDeposit());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String price = String.format("¥%1$.2f", Double.valueOf(StringUtils.toMoneyYuan(depositNum + rentNum * monthNum)));
        priceTv.setText(price);
    }

    private void initWidget() {
        if (this.listImages.size() > 0) {
            buyRightNowItemImg.setImageURI(Uri.parse(this.listImages.get(0)));
        } else {
            buyRightNowItemImg.setImageURI(Uri.parse(""));
        }
        if (!isSelectedWallet) {
            buy_right_now_wallet_pay_bkg.setBackgroundResource(R.mipmap.icon_selected_normal);
            mImgWeiXinBkg.setBackgroundResource(R.mipmap.icon_selected_press);
        } else {
            buy_right_now_wallet_pay_bkg.setBackgroundResource(R.mipmap.icon_selected_press);
            mImgWeiXinBkg.setBackgroundResource(R.mipmap.icon_selected_normal);
        }
        buyRightNowItemTitle.setText(itemName);
        buyRightNowItemDes.setText(itemDescrible);
        buyRightNowOriginPice.setText("原价：￥" + StringUtils.toMoneyYuan(itemPrice));
        switch (operateType) {
            case 1:
                buy_right_now_deposit.setText(Html.fromHtml(String.format(getResources().getString(R.string.buy_right_now_deposit), StringUtils.toMoneyYuan(item.getDeposit()))));
                buy_right_now_rent_per_unit.setText(Html.fromHtml(String.format(getResources().getString(R.string.buy_right_now_rent), StringUtils.toMoneyYuan(item.getRent()))));
                monthNum = 1;
                setPrice(getBuyRightNowItemPrice);
                rl_buy_right_now_transfer.setVisibility(View.GONE);
                rl_buy_right_now_gift.setVisibility(View.GONE);
                rl_buy_right_now_rent.setVisibility(View.VISIBLE);
                break;

            case 2:
                tv_buy_right_now_discount_price.setText(Html.fromHtml(String.format(getResources().getString(R.string.buy_right_now_discount_price), StringUtils.toMoneyYuan(item.getDiscountprice()))));
                getBuyRightNowItemPrice.setText("￥ " + StringUtils.toMoneyYuan(item.getDiscountprice()));
                rl_buy_right_now_transfer.setVisibility(View.VISIBLE);
                rl_buy_right_now_gift.setVisibility(View.GONE);
                rl_buy_right_now_rent.setVisibility(View.GONE);
                break;

            case 3:
                getBuyRightNowItemPrice.setText("￥ " + StringUtils.toMoneyYuan(0));
                rl_buy_right_now_transfer.setVisibility(View.GONE);
                rl_buy_right_now_gift.setVisibility(View.VISIBLE);
                rl_buy_right_now_rent.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    private void setClickListener() {
        buy_right_now_wallet_pay_bkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
    private void getBundleData() {
        bundle = getIntent().getExtras();
        if (null != bundle) {
            item = bundle.getParcelable("item");
            String imageJson = item.getImagejson();;
            if (null != imageJson) {
                try {
                    JSONArray jsonArray = new JSONArray(imageJson);
                    if (null != jsonArray && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            this.listImages.add(jsonArray.optString(i));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            itemPrice = item.getPrice() + "";
            itemName = item.getName();
            itemDescrible = item.getDiscribe();
            operateType = item.getOperatype();
        }
    }
    @OnClick(R.id.buy_right_now_edit_address_ll)
    void clickEditAddress() {
        ActivityHelper.goSelectBuyAddressActivity(this, 1);
    }

    @OnClick(R.id.pay_order_weixin_ll)
    void clickWeiXinPay() {
        if (isSelectedWallet) {
            buy_right_now_wallet_pay_bkg.setBackgroundResource(R.mipmap.icon_selected_normal);
            mImgWeiXinBkg.setBackgroundResource(R.mipmap.icon_selected_press);
        } else {
            return;
        }
        isSelectedWallet = !isSelectedWallet;
    }

    @OnClick(R.id.pay_order_wallet_ll)
    void clickWalletPay() {
        if (isSelectedWallet) {
            return;
        } else {
            buy_right_now_wallet_pay_bkg.setBackgroundResource(R.mipmap.icon_selected_press);
            mImgWeiXinBkg.setBackgroundResource(R.mipmap.icon_selected_normal);
        }
        isSelectedWallet = !isSelectedWallet;
    }

    @OnClick(R.id.pay_order_alipay_ll)
    void clickAlipay() {
        Application.showToastShort(R.string.buy_right_now_alipay);
        if (isSelectedWallet) {
            mImgAlipayBkg.setBackgroundResource(R.mipmap.icon_selected_normal);
        } else {
            mImgAlipayBkg.setBackgroundResource(R.mipmap.icon_selected_press);
        }
        isSelectedWallet = !isSelectedWallet;
    }

    @OnClick(R.id.submit_order_ll)
    void clickSubmitOrder() {
        //pay();
        getOrderId();
    }

    private void getOrderId() {
        PlaceAnOrderParam xml = new PlaceAnOrderParam();
        xml.setFunction("placeanorder");
        xml.setUserid(UserRecord.getInstance().getUserId());
        xml.setItemid(item.getItem_id());
        xml.setAddress(buyRightNowAddressDetail.getText().toString());
        xml.setPaytype("1");
        xml.setNum(monthNum + "");
        Call<BaseEntry<List<AnOrderBean>>> result = ApiManager.getApi().placeAnOrder(new Gson().toJson(xml));
        result.enqueue(new Callback<BaseEntry<List<AnOrderBean>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<AnOrderBean>>> response) {
                Log.e(TAG, "onResponse");
                if (response.isSuccess()) {
                    BaseEntry<List<AnOrderBean>> body = response.body();
                    if (0 == body.getResult()) {
                        orderId = body.getData().get(0).getOrderid();
                        if (isSelectedWallet) {
                            ActivityHelper.goPayThePasswordForResult(BuyRightNowActivity.this, 10);
                        } else {
                            doWeiXinPay(orderId);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onFailure");
            }
        });
    }
    private void doWalletPay(String orderId, String pwd) {
        WalletPayParam xml = new WalletPayParam();
        xml.setFunction("balancepay");
        xml.setUserid(UserRecord.getInstance().getUserId());
        xml.setOrderid(orderId);
        xml.setOrdertype("1");
        xml.setPassword(pwd);
        Call<BaseEntry<List<AnOrderBean>>> result = ApiManager.getApi().WalletPay(new Gson().toJson(xml));
        result.enqueue(new Callback<BaseEntry<List<AnOrderBean>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<AnOrderBean>>> response) {
                Log.e(TAG, "onResponse");
                if (response.isSuccess()) {
                    BaseEntry<List<AnOrderBean>> body = response.body();
                    if (0 == body.getResult()) {
                        //Application.showToast(R.string.pay_success_title_center);
                        finish();
                        ActivityHelper.goBuyPaySuccess(BuyRightNowActivity.this);
                    } else {
                        showPayDialog(body.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onFailure");
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

    private void doWeiXinPay(String orderId) {
        GetWXPrayPayIdParam xml = new GetWXPrayPayIdParam();
        xml.setFunction("wxprepay");
        xml.setUserid(UserRecord.getInstance().getUserId());
        xml.setOrderid(orderId);
        xml.setOrdertype("1");
        Call<BaseEntry<List<WXPrePayBean>>> result = ApiManager.getApi().getWXPrePayId(new Gson().toJson(xml));
        result.enqueue(new Callback<BaseEntry<List<WXPrePayBean>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<WXPrePayBean>>> response) {
                Log.e(TAG, "onResponse");
                if (response.isSuccess()) {
                    BaseEntry<List<WXPrePayBean>> body = response.body();
                    if (0 == body.getResult()) {
                        wxPrepayId = body.getData().get(0).getPrepayid();
                        wxPay(wxPrepayId);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onFailure");
            }
        });
    }

    private void doWXPayNotify(String orderId, String wxPrepayId) {
        WXPayNotifyParam xml = new WXPayNotifyParam();
        xml.setFunction("wxpay");
        xml.setUserid(UserRecord.getInstance().getUserId());
        xml.setOrderid(orderId);
        xml.setPayjson("");
        xml.setPrepayid(wxPrepayId);
        Call<BaseEntry<List<Void>>> result = ApiManager.getApi().wxPayNotify(new Gson().toJson(xml));
        result.enqueue(new Callback<BaseEntry<List<Void>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<Void>>> response) {
                Log.e(TAG, "onResponse");
                if (response.isSuccess()) {
                    BaseEntry<List<Void>> body = response.body();
                    if (0 == body.getResult()) {
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onFailure");
            }
        });
    }

    private void wxPay(String prepayId) {
        PayReq req = new PayReq();
        req.appId = Constants.WX_APP_ID;
        req.partnerId = Constants.WX_PARTNER_ID;
        req.prepayId = prepayId;
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());
        req.packageValue = Constants.WX_PACKAGE_NAME;

        Map params = new HashMap<String,String>();
        params.put("appid",req.appId);
        params.put("noncestr",req.nonceStr);
        params.put("package",req.packageValue);
        params.put("partnerid",req.partnerId);
        params.put("prepayid",req.prepayId);
        params.put("timestamp",req.timeStamp);

        req.sign = genSign(params);
        Application.iwxapi.sendReq(req);
    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5Tools.getMD5String(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }


    private String genSign(Map params){
        StringBuilder sb = new StringBuilder();
        sb.append("appid=" + params.get("appid") + "&");
        sb.append("noncestr=" + params.get("noncestr") + "&");
        sb.append("package=" + params.get("package") + "&");
        sb.append("partnerid=" + params.get("partnerid") + "&");
        sb.append("prepayid="+params.get("prepayid")+"&");
        sb.append("timestamp="+params.get("timestamp")+"&");
        sb.append("key=");
        sb.append(Constants.WX_PAY_KEY);
        String appsign = MD5Tools.getMD5String(sb.toString().getBytes()).toUpperCase();
        return appsign;
    }
    /**
     * call alipay sdk pay. 调用SDK支付
     *
     */
    public void pay() {
        // 订单
        String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(BuyRightNowActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     *
     */
    public void check(View v) {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(BuyRightNowActivity.this);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }

    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * create the order info. 创建订单信息
     *
     */
    public String getOrderInfo(String subject, String body, String price) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";//

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        Log.e("12321",orderInfo);
        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            Bundle bundle = data.getExtras();
            switch (requestCode) {
                case 1:
                    if (null != bundle) {
                        buyRightNowReceiver.setText(bundle.getString("name"));
                        buyRightNowPhone.setText(bundle.getString("phone"));
                        buyRightNowAddressDetail.setText(bundle.getString("address"));
                        addressId = bundle.getString("address_id");
                    }
                    break;

                case 10:
                    if (null != bundle) {
                        if (!bundle.getBoolean("isCancelPay")) {
                            String pwd = bundle.getString("password");
                            doWalletPay(orderId, pwd);
                        } else {
                            showPayDialog(getResources().getString(R.string.buy_right_now_pay_cancel));
                        }
                    }
                    break;
                default:
                    break;
            }
        } else {
            Log.e(TAG, "pay cancel");
        }
    }
}