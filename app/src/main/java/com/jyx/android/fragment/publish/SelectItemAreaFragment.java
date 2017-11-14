package com.jyx.android.fragment.publish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.jyx.android.base.BaseRecycleViewFragment;
import com.jyx.android.base.RecycleBaseAdapter;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.AddressItemBean;
import com.jyx.android.model.AddressItemList;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.GetAddressParam;
import com.jyx.android.net.ApiManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by zfang on 2015/12/29.
 * 取物方式
 */
public class SelectItemAreaFragment extends BaseRecycleViewFragment {

    private SelectItemAreaAdapter listAdapter = null;
    protected static final String TAG = SelectItemAreaFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "SelectItemAreaFragment";
    private static final String FRIEND_SCREEN = "SelectItemAreaFragment";
    public static final String BUNDLE_KEY_UID = "SelectItemAreaFragment";
    private int mUid;

    private SelectItemAreaAdapter.OnCheckClickListener onCheckClickListener = new SelectItemAreaAdapter.OnCheckClickListener() {
        @Override
        public void onCheckClick(AddressItemBean item) {
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
        super.initView(view);
    }

    @Override
    protected RecycleBaseAdapter getListAdapter() {
        if (null == listAdapter) {
            listAdapter = new SelectItemAreaAdapter(this.getActivity());
            listAdapter.setListener(onCheckClickListener);
        }
        return listAdapter;
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
    protected AddressItemList parseComAvoList(List<?> listavos ) throws Exception {
        AddressItemList addressItemList = new AddressItemList();
        addressItemList.setItemList((List<AddressItemBean>) listavos);
        return addressItemList;
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
        int e=  mCurrentPage ;
        int index=mCurrentPage*20;
        /*AVQuery.doCloudQueryInBackground("select * from UserAddress limit " + Integer.toString(index) + ",20 order by updatedAt desc", new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult result, AVException cqlException) {
                if (cqlException == null) {
                    DoComHandleTask(result.getResults(), false);
                }
            }
        });*/
        //getData();
    }

    private void getData() {
        GetAddressParam xml = new GetAddressParam();
        xml.setFunction("getmyaddress");
        xml.setType("1");
        xml.setUserid(UserRecord.getInstance().getUserId());
        String xmlStr = new Gson().toJson(xml).toString();
        Call<BaseEntry<List<AddressItemBean>>> result = ApiManager.getApi().getMyAddress(xmlStr);
        result.enqueue(new Callback<BaseEntry<List<AddressItemBean>>>() {
            @Override
            public void onResponse(retrofit2.Response<BaseEntry<List<AddressItemBean>>> response) {
                Log.e(TAG, "onResponse");
                if (response.isSuccess()) {
                    BaseEntry<List<AddressItemBean>> body = response.body();
                    if (0 == body.getResult()) {
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
    public void refresh() {
        sendRequestData();
    }


    /**
     * 相应点击事件，也可在
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        AddressItemBean item = (AddressItemBean) listAdapter.getData().get(position);
       doFinish(item);
    }

    private void doFinish(AddressItemBean item) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("code", item.getAddress_id());
        bundle.putString("address", item.getAddress());
        intent.putExtras(bundle);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(FRIEND_SCREEN);
//        MobclickAgent.onResume(getActivity());
        listAdapter.clear();
        getData();
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(FRIEND_SCREEN);
//        MobclickAgent.onPause(getActivity());
    }
}
