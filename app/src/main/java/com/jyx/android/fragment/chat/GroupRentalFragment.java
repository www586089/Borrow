package com.jyx.android.fragment.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.jyx.android.activity.SearchActivity;
import com.jyx.android.adapter.chat.GroupRentalMessageAdapter;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.GetProListParam;
import com.jyx.android.model.GroupInfoBean;
import com.jyx.android.model.ItemBean;
import com.jyx.android.model.param.GetGroupRentalParam;
import com.jyx.android.model.param.GetMyGroupListParam;
import com.jyx.android.net.ApiManager;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 群消息群租借界面
 * Created by zfang on 2015/11/08.
 */
public class GroupRentalFragment  extends AbsBaseRentalFragment {

    protected static final String TAG = GroupRentalFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "GroupRentalFragment";
    private static final String FRIEND_SCREEN = "GroupRentalFragment";
    public static final String BUNDLE_KEY_UID = "GroupRentalFragment";
    private int mUid;
    private String groupid;
    private MyReceiver receiver;
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
        getActivity().registerReceiver(receiver, filter);
        SharedPreferences sp=getActivity().getSharedPreferences("user", 0);
        groupid = sp.getString("groupid", "");

    }
    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mUid;
    }
    @Override
    protected void initAdapter() {
        if (null == rentalAdapter) {
            rentalAdapter = new GroupRentalMessageAdapter(getActivity());
            rentalAdapter.setItemOpListner(opListener);
        }
    }

    @Override
    protected void getQueryType(GetProListParam param) {
        param.setQuerytype("group");
    }

    @Override
    protected void getData() {
        threadGetData();
    }

    private void threadGetData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetMyGroupListParam xmlGroupList = new GetMyGroupListParam();
                xmlGroupList.setFunction("getmygrouplist");
                xmlGroupList.setUserid(UserRecord.getInstance().getUserId());
                final Call<BaseEntry<List<GroupInfoBean>>> resultGruopList = ApiManager.getApi().getMyGroupList(new Gson().toJson(xmlGroupList));
                try {
                    Response<BaseEntry<List<GroupInfoBean>>> response = resultGruopList.execute();
                    if (null != response && response.isSuccess()) {
                        BaseEntry<List<GroupInfoBean>> body = response.body();
                        if (null != body && 0 == body.getResult()) {
                            final HashSet<ItemBean> itemBeansSet = new HashSet<ItemBean>();
                            List<GroupInfoBean> groupInfoBeanList = body.getData();
                            for (GroupInfoBean bean : groupInfoBeanList) {
                                if (bean.getGroup_id().equals(groupid)){
                                    getGroupRentalSync(bean, itemBeansSet);
                                }
                            }
                            final List<ItemBean> itemBeanList = new ArrayList<ItemBean>();
                            itemBeanList.addAll(itemBeansSet);
                            Collections.sort(itemBeanList, new Comparator<ItemBean>() {
                                @Override
                                public int compare(ItemBean lhs, ItemBean rhs) {
                                    Date lhsDate = getFormatDate(lhs.getCreatedat());
                                    Date rhsDate = getFormatDate(rhs.getCreatedat());
                                    if (null == rhsDate && null != lhsDate) {
                                        return -1;
                                    } else if (null != rhsDate && null == lhsDate) {
                                        return 1;
                                    } else if (null == rhsDate && null == lhsDate) {
                                        return 0;
                                    }
                                    return rhsDate.compareTo(lhsDate);
                                }
                            });
                            if (null != getActivity()) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DoComHandleTask(itemBeanList, false);
                                    }
                                });
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Date getFormatDate(String dateStr) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    void getGroupRentalSync(final GroupInfoBean bean, HashSet<ItemBean> itemBeansSet) throws IOException{
        GetGroupRentalParam xml = new GetGroupRentalParam();
        xml.setFunction("getgroupitemlist");
        xml.setUserid(UserRecord.getInstance().getUserId());
        xml.setQuerytype("group");
        xml.setGroupid(bean.getGroup_id());
        xml.setPagenumber(mCurrentPage + "");
        Call<BaseEntry<List<ItemBean>>> result = ApiManager.getApi().getItemList(new Gson().toJson(xml));
        Response<BaseEntry<List<ItemBean>>> response = result.execute();
        if (response.isSuccess()) {
            BaseEntry<List<ItemBean>> body = response.body();
            if (0 == body.getResult()) {
                for (ItemBean itemBean : body.getData()) {
                    itemBean.setGroup_id(bean.getGroup_id());
                    itemBean.setGroupName(bean.getGroup_name());
                    itemBean.setGroupPortraitUri(bean.getImagejson());
                    if (!itemBeansSet.contains(itemBean)) {
                        itemBeansSet.add(itemBean);
                    }
                }
            }
        }
    }
    private void getGroupList() {
        GetMyGroupListParam xmlGroupList = new GetMyGroupListParam();
        xmlGroupList.setFunction("getmygrouplist");
        xmlGroupList.setUserid(UserRecord.getInstance().getUserId());
        final Call<BaseEntry<List<GroupInfoBean>>> resultGruopList = ApiManager.getApi().getMyGroupList(new Gson().toJson(xmlGroupList));
        resultGruopList.enqueue(new Callback<BaseEntry<List<GroupInfoBean>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<GroupInfoBean>>> response) {
                if (null != response && response.isSuccess()) {
                    BaseEntry<List<GroupInfoBean>> body = response.body();
                    if (null != body && 0 == body.getResult()) {
                        List<GroupInfoBean> groupInfoBeanList = body.getData();
                        for (GroupInfoBean bean : groupInfoBeanList) {
                            getGroupRental(bean);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                DoComHandleTask(null, false);
            }
        });
    }
    void getGroupRental(final GroupInfoBean bean) {
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
                        DoComHandleTask(body.getData(), false);
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

