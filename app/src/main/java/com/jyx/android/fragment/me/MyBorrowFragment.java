package com.jyx.android.fragment.me;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.activity.sign.AutomaticLogon;
import com.jyx.android.adapter.me.MyBorrowAdpter;
import com.jyx.android.base.Application;
import com.jyx.android.base.BaseListFragment;
import com.jyx.android.base.ListBaseAdapter;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.AnOrderBean;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.BorrowInfo;
import com.jyx.android.model.BorrowInfoList;
import com.jyx.android.model.ListEntity;
import com.jyx.android.model.param.WalletPayParam;
import com.jyx.android.net.ApiManager;
import com.jyx.android.net.BizException;
import com.jyx.android.rx.CommonExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.rong.imkit.RongIM;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yiyi on 2015/12/25.
 */
public class MyBorrowFragment extends BaseListFragment {
    protected static final String TAG = MyBorrowFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "MyBorrowFragment";
    private static final String FRIEND_SCREEN = "MyBorrowFragment";
    public static final String BUNDLE_KEY_UID = "MyBorrowFragment";

    public static final int TYPE_PENDING_PAYMENT = 0;
    public static final int TYPE_BORROWED = 1;
    public static final int TYPE_RETURNED = 2;

    public static final String PAYMENT_MSG = "BORROWPAYMENT";
    public static final String BORROWED_MSG = "BORROWBORROWED";
    public static final String RETURNED_MSG = "BORROWRETURN";

    private static final String KEY_TYPE = "key_type_MyBorrowFragment";

    private int mUid;
    private MyBorrowAdpter mAdapter;
    private String orderID;
    private int mType;

    private SweetAlertDialog mLoadingDialog;

    private IntentFilter intentFilter;
    private LocalBroadcastManager localBroadcastManager;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String refreshtype = intent.getStringExtra("mtype");
            if(Integer.parseInt(refreshtype) == mType){
                refresh();
            }
        }

    };

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

    //聊天
    public void Chat(String target_id)
    {
        if (RongIM.getInstance() != null){
                //如果为私聊 跳转至私聊
                RongIM.getInstance().startPrivateChat(getActivity(), target_id, UserRecord.getInstance().getUserId());
            }
    }

    //查看他人信息
    public void ViewOtherUser(String target_id)
    {
        Bundle bundle = new Bundle();
        bundle.putString("userid", target_id);
        ActivityHelper.goOtherInfoExt(this.getContext(), bundle);
    }

    //查看订单信息
    public void ViewOrder(String order_id)
    {

    }

    //订单支付
    public void PayOrder(String order_id)
    {
        orderID=order_id;
        //余额支付
        ActivityHelper.goPayThePasswordForResult(getActivity(), 10);

    }

    //取消订单
    public void StopOrder(String orderid)
    {
        Map<String, Object> jm = null;
        String xmlString = "";

        jm = new HashMap<>();
        jm.put("function", "manageorder");
        jm.put("userid", UserRecord.getInstance().getUserId());
        jm.put("orderid", orderid);
        jm.put("status", "11");

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
                            new AutomaticLogon(getContext()).login();
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
                        refresh();
                        Intent intent = new Intent(RETURNED_MSG);
                        intent.putExtra("mtype", String.valueOf(TYPE_RETURNED));
                        //发送本地广播
                        localBroadcastManager.sendBroadcast(intent);
                    }
                });
    }

    //确认收货
    public void ReceiveOrder(String orderid)
    {
        Map<String, Object> jm = null;
        String xmlString = "";

        jm = new HashMap<>();
        jm.put("function", "manageorder");
        jm.put("userid", UserRecord.getInstance().getUserId());
        jm.put("orderid", orderid);
        jm.put("status", "5");

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
                                new AutomaticLogon(getContext()).login();
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
                        refresh();
                    }
                });
    }

    //归还
    public void ReturnOrder(String orderid)
    {
        Map<String, Object> jm = null;
        String xmlString = "";

        jm = new HashMap<>();
        jm.put("function", "manageorder");
        jm.put("userid", UserRecord.getInstance().getUserId());
        jm.put("orderid", orderid);
        jm.put("status", "6");

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
                                new AutomaticLogon(getContext()).login();
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
                        refresh();
                    }
                });
    }

    public static MyBorrowFragment newInstance(int type) {
        MyBorrowFragment f = new MyBorrowFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mType = args.getInt(KEY_TYPE);
        }
        else {
            mType = 0;
        }

        super.onCreate(savedInstanceState);

        mLoadingDialog = new SweetAlertDialog(this.getContext(),SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在提交请求，请稍候.....");
        mLoadingDialog.setCancelable(false);

        switch (mType)
        {
            case TYPE_PENDING_PAYMENT:
                intentFilter = new IntentFilter(PAYMENT_MSG);
                break;
            case TYPE_BORROWED:
                intentFilter = new IntentFilter(BORROWED_MSG);
                break;
            case TYPE_RETURNED:
                intentFilter = new IntentFilter(RETURNED_MSG);
                break;
        }

        localBroadcastManager=LocalBroadcastManager.getInstance(this.getContext());
        //注册本地广播监听器
        localBroadcastManager.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    protected ListBaseAdapter getListAdapter() {
        if (null == mAdapter) {
            mAdapter = new MyBorrowAdpter(this, mType);
        }
        return mAdapter;
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mUid;
    }

    /**
     * 必填项，此项在异步线程中，进行执行，将查询的数据，转换为ListBaseAdapter
     * 需要的数据类型，实现ListEntity接口
     * （因为此处在异步线程执行，可以考虑直接在这里进行网络访问，数据库查询等）
     * @return
     */
    protected ListEntity parseComAvoList(List<?> listavos ) throws Exception {
        BorrowInfoList flist = new BorrowInfoList();
        final List<BorrowInfo> itemsList = new ArrayList<BorrowInfo>();

        if (listavos != null) {
            List<BorrowInfo> listobjs = (List<BorrowInfo>) listavos;
            for (BorrowInfo object : listobjs) {
                itemsList.add(object);
            }
        }

        flist.setItemlist(itemsList);
        return flist;
    }


    /**
     * 必填项，为数请求参数发送，在UI线程中执行，分页也在此处封装
     * 可采用多种数据查询，支持数据库查询，LeanCloud数据查询，查询后调用父类
     * DoComHandleTask方法，执行异步线程，异步线程在调用上面的parseComAvoList方法，最后现象
     * 页码 mCurrentPage  在父类中 递增
     * @return
     */
    @Override
    protected void sendRequestData() {
        int e =  mCurrentPage ;
        Map<String, Object> jm = null;
        String xmlString = "";

        jm = new HashMap<>();
        jm.put("function", "getborrowlist");
        jm.put("userid", UserRecord.getInstance().getUserId());
        jm.put("pagenumber", e + 1);
        switch (mType)
        {
            case 0:
                jm.put("querytype", "nopay");
                break;
            case 1:
                jm.put("querytype", "ontheway");
                break;
            case 2:
                jm.put("querytype", "finish");
                break;
        }

        Log.d("成功", "发送数据" + Integer.toString(e));

        xmlString = new Gson().toJson(jm);

        ApiManager.getApi()
                .getMyBorrowList(xmlString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseEntry<List<BorrowInfo>>, List<BorrowInfo>>() {
                    @Override
                    public List<BorrowInfo> call(BaseEntry<List<BorrowInfo>> listBaseEntry) {
                        if (listBaseEntry == null) {
                            throw new BizException(-1, getString(R.string.load_data_error));
                        }

                        if (listBaseEntry.getResult() != 0) {
                            if (listBaseEntry.getMsg().equals(getString(R.string.session_data_error)))
                                new AutomaticLogon(getContext()).login();
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
                .subscribe(new Subscriber<List<BorrowInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(List<BorrowInfo> borrowInfoList) {
                        if (borrowInfoList == null) {
                            DoComHandleTask(null, false);
                        } else {
                            DoComHandleTask(borrowInfoList, false);
                        }
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
                if (response.isSuccess()) {
                    BaseEntry<List<AnOrderBean>> body = response.body();
                    if (0 == body.getResult()) {
                        Application.showToast(R.string.pay_success_title_center);
                    } else {
//                        showPayDialog(body.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Application.showToast(R.string.buy_right_now_pay_failure);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (-1 == resultCode) {
            Bundle bundle = data.getExtras();
            switch (requestCode) {
                case 1:
                    if (null != bundle) {
//                        buyRightNowReceiver.setText(bundle.getString("name"));
//                        buyRightNowPhone.setText(bundle.getString("phone"));
//                        buyRightNowAddressDetail.setText(bundle.getString("address"));
//                        addressId = bundle.getString("address_id");
                    }
                    break;

                case 10:
                    if (null != bundle) {
                        if (!bundle.getBoolean("isCancelPay")) {
                            String pwd = bundle.getString("password");
                            doWalletPay(orderID, pwd);
                        } else {
//                            showPayDialog(getResources().getString(R.string.buy_right_now_pay_cancel));
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


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void initView(View view) {
        super.initView(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(mBroadcastReceiver);
    }


}
