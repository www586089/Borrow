package com.jyx.android.fragment.discovery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.jyx.android.activity.SearchActivity;
import com.jyx.android.adapter.chat.DiscoveryGroupRentalMessageAdapter;
import com.jyx.android.base.UserRecord;
import com.jyx.android.fragment.chat.AbsBaseRentalFragment;
import com.jyx.android.fragment.chat.GroupRentalFragment;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.GetProListParam;
import com.jyx.android.model.GroupInfoBean;
import com.jyx.android.model.ItemBean;
import com.jyx.android.model.param.GetGroupRentalParam;
import com.jyx.android.model.param.GetNearByGroupListParam;
import com.jyx.android.net.ApiManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 陌生群租借列表
 * Created by gaobo on 2015/10/31.
 */
public class DiscoveryRentalGroupFragment extends AbsBaseRentalFragment {

    protected static final String TAG = GroupRentalFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "RentalGroupFragment";
    private static final String FRIEND_SCREEN = "RentalGroupFragment";
    public static final String BUNDLE_KEY_UID = "RentalGroupFragment";
    private int mUid;
    private List<ItemBean> mItem;
    private MyReceiver receiver;
    private List<ItemBean> item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mUid = args.getInt(BUNDLE_KEY_UID);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        super.initHeaderView(2);
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();// 创建IntentFilter对象
        filter.addAction("android.intent.action.test");
        getContext().registerReceiver(receiver, filter);
        mItem=new ArrayList<ItemBean>();
        item=new ArrayList<ItemBean>();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mUid;
    }
    @Override
    protected void initAdapter() {
        if (null == rentalAdapter) {
            rentalAdapter = new DiscoveryGroupRentalMessageAdapter(getActivity(),this);
            rentalAdapter.setItemOpListner(opListener);
        }
    }

    @Override
    protected void getQueryType(GetProListParam param) {
        param.setQuerytype("group");
    }

    @Override
    protected void getData() {
        GetNearByGroupListParam xmlGroupList = new GetNearByGroupListParam();
        xmlGroupList.setFunction("getmygrouplist");
        xmlGroupList.setUserid(UserRecord.getInstance().getUserId());
        xmlGroupList.setQuerytype("1");
        final Call<BaseEntry<List<GroupInfoBean>>> resultGruopList = ApiManager.getApi().getMyGroupList(new Gson().toJson(xmlGroupList));
        resultGruopList.enqueue(new Callback<BaseEntry<List<GroupInfoBean>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<GroupInfoBean>>> response) {
                if (null != response && response.isSuccess()) {
                    BaseEntry<List<GroupInfoBean>> body = response.body();
                    if (null != body && 0 == body.getResult()) {
                        List<GroupInfoBean> groupInfoBeanList = body.getData();
                        for (GroupInfoBean bean : groupInfoBeanList) {
                            getGroupRental(bean,groupInfoBeanList.size());
                        }
//                        DoComHandleTask(mItem, false);

                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                DoComHandleTask(null, false);
            }
        });
    }

   public void getGroupRental(final GroupInfoBean bean, final int size) {
        GetGroupRentalParam xml = new GetGroupRentalParam();
        xml.setFunction("getgroupitemlist");
        xml.setUserid(UserRecord.getInstance().getUserId());
        xml.setQuerytype("group");
        xml.setGroupid(bean.getGroup_id());
        xml.setPagenumber(mCurrentPage + "");
        Call<BaseEntry<List<ItemBean>>> result = ApiManager.getApi().getItemList(new Gson().toJson(xml));
        result.enqueue(new Callback<BaseEntry<List<ItemBean>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<ItemBean>>> response) {
                Log.e(TAG, "onResponse");
                if (response.isSuccess()) {
                    BaseEntry<List<ItemBean>> body = response.body();
                    if (0 == body.getResult()) {
                        for (ItemBean itemBean : body.getData()) {
                            itemBean.setGroup_id(bean.getGroup_id());
                            itemBean.setGroupName(bean.getGroup_name());
                            itemBean.setGroupPortraitUri(bean.getImagejson());
                        }
                            if (body.getData().size()>0){
                                item.add(body.getData().get(0));
                            }
                        if (item.size()==size){
                            DoComHandleTask(item, false);
                        }
//                            mItem.add(body.getData().get(i));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onFailure");
            }
        });
    }
    public void upData() {
        rentalAdapter.setData(item);
//        GetNearByGroupListParam xmlGroupList = new GetNearByGroupListParam();
//        xmlGroupList.setFunction("getmygrouplist");
//        xmlGroupList.setUserid(UserRecord.getInstance().getUserId());
//        xmlGroupList.setQuerytype("1");
//        final Call<BaseEntry<List<GroupInfoBean>>> resultGruopList = ApiManager.getApi().getMyGroupList(new Gson().toJson(xmlGroupList));
//        resultGruopList.enqueue(new Callback<BaseEntry<List<GroupInfoBean>>>() {
//            @Override
//            public void onResponse(Response<BaseEntry<List<GroupInfoBean>>> response) {
//                if (null != response && response.isSuccess()) {
//                    BaseEntry<List<GroupInfoBean>> body = response.body();
//                    if (null != body && 0 == body.getResult()) {
//                        rentalAdapter.setData(mItem);
//                        List<GroupInfoBean> groupInfoBeanList = body.getData();
//                        for (GroupInfoBean bean : groupInfoBeanList) {
//                            getGroupRental(bean,groupInfoBeanList.size());
//                        }
////                        DoComHandleTask(mItem, false);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                DoComHandleTask(null, false);
//            }
//        });
    }

    public void getGroupIdRental(final ItemBean bean) {
        GetGroupRentalParam xml = new GetGroupRentalParam();
        xml.setFunction("getgroupitemlist");
        xml.setUserid(UserRecord.getInstance().getUserId());
        xml.setQuerytype("group");
        xml.setGroupid(bean.getGroup_id());
        xml.setPagenumber(mCurrentPage + "");
        Call<BaseEntry<List<ItemBean>>> result = ApiManager.getApi().getItemList(new Gson().toJson(xml));
        result.enqueue(new Callback<BaseEntry<List<ItemBean>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<ItemBean>>> response) {
                Log.e(TAG, "onResponse");
                if (response.isSuccess()) {
                    BaseEntry<List<ItemBean>> body = response.body();
                    if (0 == body.getResult()) {
                        for (ItemBean itemBean : body.getData()) {
                            itemBean.setGroup_id(bean.getGroup_id());
                            itemBean.setGroupName(bean.getGroupName());
                            itemBean.setGroupPortraitUri(bean.getGroupPortraitUri());
                        }
                        rentalAdapter.setData(body.getData());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onFailure");
            }
        });
    }
    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("title").equals(SearchActivity.RENTALGROUP)) {
                Bundle bundle = intent.getExtras();
                boolean search = bundle.getBoolean("sear");
                ArrayList<ItemBean> tmpList = new ArrayList<ItemBean>();
                tmpList = bundle.getParcelableArrayList("item");
                if (search) {
                    rentalAdapter.setData(tmpList);
                } else {
                    DoComHandleTask(null, false);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(receiver);
        super.onDestroy();
    }
}

