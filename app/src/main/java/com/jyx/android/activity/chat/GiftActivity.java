package com.jyx.android.activity.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jyx.android.R;
import com.jyx.android.adapter.chat.CommonAdapter;
import com.jyx.android.adapter.chat.ViewHolder;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.Constants;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.GiftEntity;
import com.jyx.android.model.OrderEntity;
import com.jyx.android.net.ApiManager;
import com.jyx.android.rx.CommonExceptionHandler;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.weibo.sdk.android.component.sso.tools.MD5Tools;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2/18/2016.
 */

public class GiftActivity extends BaseActivity  {

    @Bind(R.id.lv_listview)
    ListView mListView;

    List<GiftInfo> mData;

    CommonAdapter<GiftInfo> mAdapter;

    String desc;
    String imageUrl;
    private IWXAPI api;
    private final String tag = "GiftActivity";

    private String mTargetId;
    private String type;//gift 礼物  flower 鲜花

    private BroadcastReceiver receiver;

    private String giftOrFlowerId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gift_choose;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.gift;
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
        setActionRightText("");
        mData = new ArrayList<>();
        try {
            api = WXAPIFactory.createWXAPI(this,Constants.WX_APP_ID);
            api.registerApp(Constants.WX_APP_ID);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(tag,"gift new wx api exception == "+e.toString());
        }
        mTargetId = getIntent().getStringExtra("targetUserId");
        type = getIntent().getStringExtra("type");
        if(type.equals("gift")){
            setActionBarTitle(getString(R.string.gift));
        }else{
            setActionBarTitle(getString(R.string.flower));
        }
        mAdapter = new CommonAdapter<GiftInfo>(this,mData,R.layout.list_cell_gift) {
            @Override
            public void convert(ViewHolder holder, final GiftInfo giftInfo,int position) {
                holder.setText(R.id.tv_description,giftInfo.description);
                holder.setText(R.id.tv_sum,"￥"+String.valueOf(giftInfo.sum/100));
                ((SimpleDraweeView)holder.getView(R.id.iv_avatar)).setImageURI(Uri
                        .parse(giftInfo.avatarUrl));
                holder.setOnClickListener(R.id.tv_sure, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        desc = giftInfo.description;
                        imageUrl = giftInfo.avatarUrl;
                        showToast("开始支付");
                        //1.先用礼物id获取订单id，然后用订单id获取微信预支付id，然后调用微信支付
                        giftOrFlowerId = giftInfo.id;
                        if(type.equals("gift")){
                            getGiftOrderId(giftInfo.id,mTargetId,"1");
                        }else{
                            getFlowerOrderId(giftInfo.id,mTargetId,"1");
                        }
                    }
                });
            }
        };
        mListView.setAdapter(mAdapter);
        if(type.equals("gift")){
            getGiftList();
        }else{
            getFlowerList();
        }
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("giftActivity","on receive"+intent.getBooleanExtra("success",
                        false));
                if(intent.getBooleanExtra("success",false)){
                    //再向后台查询是否成功

                    //支付成功
                    GiftActivity.this.setResult(RESULT_OK,new Intent().putExtra
                            ("description",desc).putExtra("imageUrl",imageUrl).putExtra
                            ("id",giftOrFlowerId));
                    finish();
                }else{
                    finish();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("WXPayEntityActivity");
        registerReceiver(receiver,intentFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    /**
     * 获取礼物列表
     {"function":"getgiftlist","userid":"132512379702379520"}
     返回:
     {
     "result": "0",
     "msg": "操作成功",
     "datas": [
     {
     "imagejson": "",
     "price": "9900",
     "gifts_id": "1",
     "theme": "test"
     }
     ]
     }

     */
    private void getGiftList(){
        String xmlString = "";
        xmlString = "{\"function\":\"getgiftlist\",\"userid\":\"" +
                UserRecord.getInstance().getUserId() + "\"}";
        ApiManager.getApi()
                .getGiftList(xmlString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseEntry<List<GiftEntity>>, List<GiftEntity>>() {
                    @Override
                    public List<GiftEntity>call(BaseEntry<List<GiftEntity>> listBaseEntry) {
                        return listBaseEntry.getData();
                    }
                })
                .subscribe(new Subscriber<List<GiftEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(final List<GiftEntity> giftEntityList) {
                        for(GiftEntity entity:giftEntityList){
                            mData.add(new GiftInfo(entity.getImagejson(),entity
                                    .getTheme(),entity.getPrice(),entity.getGifts_id()));
                            Log.e(tag,"get gift list = "+entity.getImagejson()+" " +
                                    ""+entity.getTheme()+" "+entity.getGifts_id());
                        }
                        mAdapter.notifyDataSetChanged();

                    }
                });
    }

    /**
     获取鲜花列表
     {"function":"getflowerlist","userid":"132512379702379520"}
     返回:
     {
     "result": "0",
     "msg": "操作成功",
     "datas": [
     {
     "imagejson": "",
     "price": "99",
     "theme": "flower",
     "flowers_id": "2"
     }
     ]
     }

     */
    private void getFlowerList(){
        String xmlString = "";
        xmlString = "{\"function\":\"getflowerlist\",\"userid\":\"" +
                UserRecord.getInstance().getUserId() + "\"}";
        ApiManager.getApi()
                .getGiftList(xmlString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseEntry<List<GiftEntity>>, List<GiftEntity>>() {
                    @Override
                    public List<GiftEntity>call(BaseEntry<List<GiftEntity>> listBaseEntry) {
                        return listBaseEntry.getData();
                    }
                })
                .subscribe(new Subscriber<List<GiftEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(final List<GiftEntity> giftEntityList) {
                        for(GiftEntity entity:giftEntityList){
                            mData.add(new GiftInfo(entity.getImagejson(),entity
                                    .getTheme(),entity.getPrice(),entity.getFlowers_id()));
                            Log.e(tag,"get flower list = "+entity.getImagejson()+" " +
                                    ""+entity.getTheme()+" "+entity.getFlowers_id());
                        }
                        mAdapter.notifyDataSetChanged();

                    }
                });
    }

    /**
     * 根据礼物id获取订单id
     */
    private void getGiftOrderId(final  String giftId, String targetUserId, String
            number) {
        String xmlString = "";
        xmlString = "{\"function\":\"giftsgiven\",\"userid\":\"" +
                UserRecord.getInstance().getUserId() + "\"," +
                "\"targetuser\":\""+targetUserId+"\",\"giftid\":\""+giftId+"\"," +
                "\"num\":\""+number+"\"}";
        ApiManager.getApi()
                .getOrderList(xmlString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseEntry<List<OrderEntity>>, List<OrderEntity>>() {
                    @Override
                    public List<OrderEntity>call(BaseEntry<List<OrderEntity>> listBaseEntry) {
                        return listBaseEntry.getData();
                    }
                })
                .subscribe(new Subscriber<List<OrderEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(final List<OrderEntity> orderEntityList) {
                        Log.e(tag,"get OrderEntityList = "+orderEntityList.get(0)
                                .getOrderid()+" "+orderEntityList.get(0).getAmount());
                        //获取预支付订单 3为礼物
                        getWXPrepayId(orderEntityList.get(0).getOrderid(),3);
                    }
                });
    }

    /**
     * 根据礼物id获取订单id
     * userid-购买用户ID
     targetuser-目标用户ID
     flowerid-赠送的鲜花ID
     num-赠送礼物的数量(必须大于0)
     */
    private void getFlowerOrderId(final  String flowerId, String targetUserId, String
            number) {
        String xmlString = "";
        xmlString = "{\"function\":\"flowergiven\",\"userid\":\"" +
                UserRecord.getInstance().getUserId() + "\"," +
                "\"targetuser\":\""+targetUserId+"\",\"flowerid\":\""+flowerId+"\"," +
                "\"num\":\""+number+"\"}";
        ApiManager.getApi()
                .getOrderList(xmlString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseEntry<List<OrderEntity>>, List<OrderEntity>>() {
                    @Override
                    public List<OrderEntity>call(BaseEntry<List<OrderEntity>> listBaseEntry) {
                        return listBaseEntry.getData();
                    }
                })
                .subscribe(new Subscriber<List<OrderEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(final List<OrderEntity> orderEntityList) {
                        Log.e(tag,"get flower = "+orderEntityList.get(0)
                                .getOrderid()+" "+orderEntityList.get(0).getAmount());
                        //获取预支付 2是鲜花
                        getWXPrepayId(orderEntityList.get(0).getOrderid(),2);
                    }
                });
    }

    /**
     * 根据订单id获取微信预支付id
     */
    private void getWXPrepayId(final  String orderId, int orderType) {
        String xmlString = "";
        xmlString = "{\"function\":\"wxprepay\",\"userid\":\"" +
                UserRecord.getInstance().getUserId() + "\"," +
                "\"orderid\":\""+orderId+"\",\"ordertype\":\""+orderType+"\"}";
        ApiManager.getApi()
                .getWXPrepayId(xmlString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseEntry<List<OrderEntity>>, List<OrderEntity>>() {
                    @Override
                    public List<OrderEntity>call(BaseEntry<List<OrderEntity>> listBaseEntry) {
                        return listBaseEntry.getData();
                    }
                })
                .subscribe(new Subscriber<List<OrderEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(final List<OrderEntity> orderEntityList) {
                        Log.e(tag,"get wxPrepayid = "+orderEntityList.get(0)
                                .getPrepayid());
                        //可以开始调用微信支付了
                        //先进行环境判断
                        if(!api.isWXAppInstalled()){
                            showToast("请安装微信");
                            return;
                        }
                        if(!api.isWXAppSupportAPI()){
                            showToast("微信版本太低，请升级微信");
                            return;
                        }
                        wxPay(orderEntityList.get(0).getPrepayid());
                    }
                });
    }


    private void wxPay(String prepayId){
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
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        try {
            api.sendReq(req);
        }catch (Exception e){
            showToast("调用微信出现问题，请确保已正确安装微信");
            Log.e(tag,"Gift wx pay exception "+e.toString());
        }
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
        sb.append("appid="+params.get("appid")+"&");
        sb.append("noncestr="+params.get("noncestr")+"&");
        sb.append("package="+params.get("package")+"&");
        sb.append("partnerid="+params.get("partnerid")+"&");
        sb.append("prepayid="+params.get("prepayid")+"&");
        sb.append("timestamp="+params.get("timestamp")+"&");
        sb.append("key=");
        sb.append(Constants.WX_PAY_KEY);
        String appsign = MD5Tools.getMD5String(sb.toString().getBytes()).toUpperCase();
        return appsign;
    }


}

class GiftInfo {
    String avatarUrl;
    String description;
    double sum;
    String id;

    public GiftInfo(String avatarUrl, String description, double sum,String id) {
        this.avatarUrl = avatarUrl;
        this.description = description;
        this.sum = sum;
        this.id = id;
    }
}