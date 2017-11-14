package com.jyx.android.fragment.discovery;

import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.jyx.android.adapter.event.EventAdapter;
import com.jyx.android.base.BaseRecycleViewFragment;
import com.jyx.android.base.RecycleBaseAdapter;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.ActivityItemBean;
import com.jyx.android.model.ActivityItemList;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.ListEntity;
import com.jyx.android.model.param.GetActivityParam;
import com.jyx.android.net.ApiManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 活动列表显示
 * Created by gaobo on 2015/10/31.
 */
public class EventFragment extends BaseRecycleViewFragment {

    protected static final String TAG = EventFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "EventFragment";
    private static final String FRIEND_SCREEN = "EventFragment";
    public static final String BUNDLE_KEY_UID = "EventFragment";
    private int mUid;

    public static final int ITEM_TYPE_SPECIAL = 0;
    public static final int ITEM_TYPE_NORMAL = ITEM_TYPE_SPECIAL + 1;

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
    }


    /**
     * 必填项目
     * 需要自定义 实现RecycleBaseAdapter的子类，也可以共用
     * @return
     */
    @Override
    protected RecycleBaseAdapter getListAdapter() {
        return new EventAdapter(getActivity());
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mUid;
    }


    protected ListEntity parseComAvoList(List<?> listavos ) throws Exception {
        ActivityItemList itemList = new ActivityItemList();
        if (null != listavos) {
            itemList.setItemList((List<ActivityItemBean>) listavos);
        }
        return itemList;
    }


    @Override
    protected void sendRequestData() {
        int e=  mCurrentPage ;
        int index=mCurrentPage*20;

        if (0 == mCurrentPage) {
            mCurrentPage = 1;
        }
        getData();
    }

    private void getData() {
        GetActivityParam xml = new GetActivityParam();
        xml.setFunction("getactivitylist");
        xml.setUserid(UserRecord.getInstance().getUserId());
        xml.setPagenumber(mCurrentPage + "");
        Call<BaseEntry<List<ActivityItemBean>>> result = ApiManager.getApi().getActivity(new Gson().toJson(xml).toString());
        result.enqueue(new Callback<BaseEntry<List<ActivityItemBean>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<ActivityItemBean>>> response) {
                if (response.isSuccess() && null != getActivity()) {
                    BaseEntry<List<ActivityItemBean>> body = response.body();
                    if (null != body && 0 == body.getResult()) {
                        DoComHandleTask(addEnrollEventItem(body.getData()), false);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                DoComHandleTask(addEnrollEventItem(null), false);
            }
        });
    }

    private List<ActivityItemBean> addEnrollEventItem(List<ActivityItemBean> itemList) {
        if (null == itemList) {
            itemList = new ArrayList<ActivityItemBean>();
        }
        ActivityItemBean itemBean = new ActivityItemBean();
        itemBean.setItemType(ITEM_TYPE_SPECIAL);
        itemList.add(0, itemBean);
        return itemList;
    }

    /**
     * 相应点击事件，也可在
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}

