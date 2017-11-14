package com.jyx.android.activity.buy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.jyx.android.base.BaseRecycleViewFragment;
import com.jyx.android.base.RecycleBaseAdapter;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.FriendInfo;
import com.jyx.android.model.LoveItemBean;
import com.jyx.android.model.LoveItemList;
import com.jyx.android.model.SysDataItem;
import com.jyx.android.model.param.GetLoveItemUserParam;
import com.jyx.android.net.ApiManager;
import com.jyx.android.rx.ResultConvertFunc;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zfang on 2016-02-29.
 */
public class LoveItemUserFragment extends BaseRecycleViewFragment {

    private LoveItemUserAdapter listAdapter = null;
    protected static final String TAG = LoveItemUserFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "LoveItemUserFragment";
    private static final String FRIEND_SCREEN = "LoveItemUserFragment";
    public static final String BUNDLE_KEY_UID = "LoveItemUserFragment";
    private int mUid;
    private List<FriendInfo> friendInf;

    private LoveItemUserAdapter.OnCheckClistener onCheckClistener = new LoveItemUserAdapter.OnCheckClistener() {
        @Override
        public void onCheckClick(SysDataItem item) {
            doFinish(item);
        }
    };
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
        friendInf=new ArrayList<FriendInfo>();
        initdata();
        super.initView(view);
    }

    private void initdata() {
        String param = "{\"function\":\"getmyattention\",\"userid\":\"" + UserRecord.getInstance().getUserId() + "\"}";
        ApiManager.getApi()
                .queryContact(param)
                .subscribeOn(Schedulers.io())
                .map(new ResultConvertFunc<List<FriendInfo>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<FriendInfo>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        hideWaitDialog();
//                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(List<FriendInfo> friendInfos) {
                        friendInf.addAll(friendInfos);
                    }
                });

    }

    @Override
    protected RecycleBaseAdapter getListAdapter() {
        if (null == listAdapter) {
            listAdapter = new LoveItemUserAdapter(this.getActivity(),friendInf);
            listAdapter.setListener(onCheckClistener);
        }
        return listAdapter;
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mUid;
    }

    protected LoveItemList parseComAvoList(List<?> listavos ) throws Exception {
        LoveItemList itemList = new LoveItemList();
        if (null != listavos) {
            itemList.setItemList((List<LoveItemBean>) listavos);
        }
        return itemList;
    }

    @Override
    protected void sendRequestData() {
        if (0 == mCurrentPage) {
            mCurrentPage = 1;
        }
        getDicData();
    }

    private void getDicData() {
        GetLoveItemUserParam xml = new GetLoveItemUserParam();
        xml.setItemid(((LoveItemUserActivity) getActivity()).getItemId());
        xml.setFunction("getloveitemuser");
        xml.setPagenumber(mCurrentPage + "");
        Call<BaseEntry<List<LoveItemBean>>> result = ApiManager.getApi().getLoveItemUser(new Gson().toJson(xml));
        result.enqueue(new Callback<BaseEntry<List<LoveItemBean>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<LoveItemBean>>> response) {
                if (null != response && response.isSuccess()) {
                    BaseEntry<List<LoveItemBean>> body = response.body();
                    if (null != body) {
                        List<LoveItemBean> listInfo = body.getData();
                        if (null != listInfo && listInfo.size() > 0) {
                            DoComHandleTask(listInfo, false);
                            return;
                        }
                    }
                    DoComHandleTask(null, false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                DoComHandleTask(null, false);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
    }

    private void doFinish(SysDataItem item) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("code", item.getType_id());
        bundle.putString("name", item.getName());
        intent.putExtras(bundle);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
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
