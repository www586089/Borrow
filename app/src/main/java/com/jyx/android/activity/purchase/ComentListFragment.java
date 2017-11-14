package com.jyx.android.activity.purchase;

import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.jyx.android.base.BaseRecycleViewFragment;
import com.jyx.android.base.RecycleBaseAdapter;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.ItemCommentDetailBean;
import com.jyx.android.model.ItemCommentDetailBeanList;
import com.jyx.android.model.ItemList;
import com.jyx.android.model.param.GetItemCommentDetailParam;
import com.jyx.android.net.ApiManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zfang on 2016-02-03
 */
public class ComentListFragment extends BaseRecycleViewFragment {
    private CommentListAdapter listAdapter = null;
    protected static final String TAG = ComentListFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "ComentListFragment";
    private static final String FRIEND_SCREEN = "ComentListFragment";
    public static final String BUNDLE_KEY_UID = "ComentListFragment";
    private int mUid;
    private  CommentListsActivity mActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mUid = args.getInt(BUNDLE_KEY_UID);
        }
        super.onCreate(savedInstanceState);
        mActivity = (CommentListsActivity) getActivity();
    }

    @Override
    public void initView(View view) {
        super.initView(view);
    }

    @Override
    protected RecycleBaseAdapter getListAdapter() {
        if (null == listAdapter) {
            listAdapter = new CommentListAdapter(this.getActivity());
        }
        return listAdapter;
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mUid;
    }

    protected ItemCommentDetailBeanList parseComAvoList(List<?> listavos ) throws Exception {
        ItemList d;
        ItemCommentDetailBeanList itemList = new ItemCommentDetailBeanList();
        itemList.setItemList((List<ItemCommentDetailBean>) listavos);
        return itemList;
    }

    @Override
    protected void sendRequestData() {
        int e=  mCurrentPage ;
        int index=mCurrentPage*20;
        getCommentList();
    }

    public void refreshData() {
        getCommentList();
    }

    private void getCommentList() {
        if (0 == mCurrentPage) {
            mCurrentPage = 1;
        }
        GetItemCommentDetailParam xml = new GetItemCommentDetailParam();
        xml.setFunction("getitemcomment");
        xml.setItemid(mActivity.getItemId());
        xml.setPagenumber(mCurrentPage);
        Call<BaseEntry<List<ItemCommentDetailBean>>> result = ApiManager.getApi().getItemComment(new Gson().toJson(xml));
        result.enqueue(new Callback<BaseEntry<List<ItemCommentDetailBean>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<ItemCommentDetailBean>>> response) {
                if (null != response && response.isSuccess()) {
                    BaseEntry<List<ItemCommentDetailBean>> body = response.body();
                    DoComHandleTask(body.getData(), false);
                } else {
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
