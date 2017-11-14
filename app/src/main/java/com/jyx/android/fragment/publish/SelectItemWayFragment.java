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
import com.jyx.android.model.ItemList;
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
 * 取物方式
 */
public class SelectItemWayFragment extends BaseRecycleViewFragment {

    private SelectItemWayAdapter listAdapter = null;
    protected static final String TAG = SelectItemWayFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "SelectItemWayFragment";
    private static final String FRIEND_SCREEN = "SelectItemWayFragment";
    public static final String BUNDLE_KEY_UID = "SelectItemWayFragment";
    private int mUid;

    private SelectItemWayAdapter.OnCheckClistener onCheckClistener = new SelectItemWayAdapter.OnCheckClistener() {
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
            listAdapter = new SelectItemWayAdapter(this.getActivity());
            listAdapter.setListener(onCheckClistener);
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
    protected SysDataItemList parseComAvoList(List<?> listavos ) throws Exception {
        ItemList d;
        SysDataItemList itemList = new SysDataItemList();
        itemList.setItemList((List<SysDataItem>) listavos);
        return itemList;
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
        /*AVQuery.doCloudQueryInBackground("select * from FetchObjWay limit " + Integer.toString(index) + ",20 order by updatedAt desc", new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult result, AVException cqlException) {
                if (cqlException == null) {
                    DoComHandleTask(result.getResults(), false);
                }
            }
        });*/
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
                        DoComHandleTask(baseData.getData().getGettype(), false);
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


    /**
     * 相应点击事件，也可在
     * @param view
     * @param position
     */
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
//        MobclickAgent.onPageStart(FRIEND_SCREEN);
//        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(FRIEND_SCREEN);
//        MobclickAgent.onPause(getActivity());
    }
}
