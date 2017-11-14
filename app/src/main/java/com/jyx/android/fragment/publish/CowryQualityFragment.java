package com.jyx.android.fragment.publish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.base.BaseRecycleViewFragment;
import com.jyx.android.base.RecycleBaseAdapter;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.DicDataParam;
import com.jyx.android.model.SysDataItem;
import com.jyx.android.model.SysDataItemList;
import com.jyx.android.model.SystemDicData;
import com.jyx.android.net.ApiManager;
import com.jyx.android.net.BizException;
import com.jyx.android.rx.CommonExceptionHandler;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zfang on 2015/12/29.
 */
public class CowryQualityFragment extends BaseRecycleViewFragment {

    private CrowyQualityAdapter listAdapter = null;
    protected static final String TAG = CowryQualityFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "CowryQualityFragment";
    private static final String FRIEND_SCREEN = "CowryQualityFragment";
    public static final String BUNDLE_KEY_UID = "CowryQualityFragment";
    private int mUid;

    private CrowyQualityAdapter.OnCheckClistener onCheckClistener = new CrowyQualityAdapter.OnCheckClistener() {
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
        super.initView(view);
    }

    @Override
    protected RecycleBaseAdapter getListAdapter() {
        if (null == listAdapter) {
            listAdapter = new CrowyQualityAdapter(this.getActivity());
            listAdapter.setListener(onCheckClistener);
        }
        return listAdapter;
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mUid;
    }


    protected SysDataItemList parseComAvoList(List<?> listavos ) throws Exception {
        SysDataItemList itemList = new SysDataItemList();
        if (null != listavos && listavos.size() > 0) {
            itemList.setItemList((List<SysDataItem>) listavos);
        }
        return itemList;
    }

    @Override
    protected void sendRequestData() {
        int e=  mCurrentPage ;
        int index=mCurrentPage*20;
        getDicData();
    }
    private void getDicData() {
        DicDataParam params = new DicDataParam();
        params.setFunction("getdictdata");
        ApiManager.getApi()
                .getDicDatta(new Gson().toJson(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseEntry<SystemDicData>, SystemDicData>() {
                    @Override
                    public SystemDicData call(BaseEntry<SystemDicData> baseData) {
                        Log.e("zfang", "call");
                        if (baseData == null) {
                            throw new BizException(-1, getString(R.string.tip_load_data_error));
                        }

                        if (baseData.getResult() != 0) {
                            throw new BizException(baseData.getResult(), baseData.getMsg());
                        }
                        DoComHandleTask(baseData.getData().getFeature(), false);
                        return baseData.getData();
                    }
                })
                .subscribe(new Subscriber<SystemDicData>() {
                    @Override
                    public void onCompleted() {
                        Log.e("zfang", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("zfang", "onError");
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(SystemDicData data) {
                        Log.e("zfang", "onNext");
                    }
                });
    }

    @Override
    public void onItemClick(View view, int position) {
        SysDataItem item = (SysDataItem) listAdapter.getData().get(position);
       doFinish(item);
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
        Log.e(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
