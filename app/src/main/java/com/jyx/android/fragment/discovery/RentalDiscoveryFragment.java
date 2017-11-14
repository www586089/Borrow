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
import com.jyx.android.adapter.chat.RentalMessageAdapter;
import com.jyx.android.base.UserRecord;
import com.jyx.android.fragment.chat.AbsBaseRentalFragment;
import com.jyx.android.fragment.chat.RentalFragment;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.GetProListParam;
import com.jyx.android.model.ItemBean;
import com.jyx.android.net.ApiManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 陌生人租借列表
 * Created by gaobo on 2015/10/31.
 */
public class RentalDiscoveryFragment extends AbsBaseRentalFragment {

    protected static final String TAG = RentalFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "RentalDiscoveryFragment";
    private static final String FRIEND_SCREEN = "RentalDiscoveryFragment";
    public static final String BUNDLE_KEY_UID = "RentalDiscoveryFragment";
    private int mUid;
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
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();// 创建IntentFilter对象
        filter.addAction("android.intent.action.test");
        getContext().registerReceiver(receiver, filter);
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mUid;
    }

    @Override
    protected void initAdapter() {
        if (null == rentalAdapter) {
            rentalAdapter = new RentalMessageAdapter(getActivity());
            rentalAdapter.setItemOpListner(opListener);
        }
    }

    @Override
    protected void getQueryType(GetProListParam param) {
        param.setQuerytype("stranger");
    }

    @Override
    protected void getData() {
        GetProListParam xml = new GetProListParam();
        xml.setFunction("getitemlist");
        xml.setUserid(UserRecord.getInstance().getUserId());
        getQueryType(xml);
        xml.setPagenumber(mCurrentPage + "");
        Call<BaseEntry<List<ItemBean>>> result = ApiManager.getApi().getItemList(new Gson().toJson(xml));
        result.enqueue(new Callback<BaseEntry<List<ItemBean>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<ItemBean>>> response) {
                Log.e(TAG, "onResponse");
                if (response.isSuccess()) {
                    BaseEntry<List<ItemBean>> body = response.body();
                    if (0 == body.getResult()) {
                        DoComHandleTask(body.getData(), false);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onFailure");
                DoComHandleTask(null, false);
            }
        });
    }
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("title").equals(SearchActivity.RENTALDISCOVERY)) {
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
