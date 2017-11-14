package com.jyx.android.base;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jyx.android.R;
import com.jyx.android.model.ListEntity;
import com.jyx.android.utils.TDevice;
import com.jyx.android.widget.empty.EmptyLayout;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 从开源中国 艾嘛 复制过来，进行了简单修改
 * 以ListView的形式显示列表
 * Created by gaobo on 2015/10/28.
 */
public abstract class BaseAMListFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener{

    private static final String TAG = "BaseAMListFragment";
    public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";
    @Bind(R.id.swiperefreshlayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.listview)
    protected ListView mListView;
    protected ListBaseAdapter  mAdapter;
    @Bind(R.id.error_layout)
    protected EmptyLayout mErrorLayout;
    protected int mStoreEmptyState = -1;
    protected int mCurrentPage = 0;
    protected int mCatalog = 1;


    private ParserComTask mParserTask;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pull_refresh_listview;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view  );
        initView(view);
    }

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mCatalog = args.getInt(BUNDLE_KEY_CATALOG, 0);
        }
    }
    @Override
    public void initView(View view) {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);

        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCurrentPage = 0;
                mState = STATE_REFRESH;
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                requestData(true);
            }
        });

        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(mListScrollListener);

        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        } else {
            mAdapter = getListAdapter();
            mListView.setAdapter(mAdapter);

            if (requestDataIfViewCreated()) {
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                mState = STATE_NONE;
                requestData(false);
            } else {
                mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            }

        }
        if (mStoreEmptyState != -1) {
            mErrorLayout.setErrorType(mStoreEmptyState);
        }
    }


    @Override
    public void onDestroyView() {
        mStoreEmptyState = mErrorLayout.getErrorState();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
//        cancelReadCacheTask();
        cancelParserTask();
        super.onDestroy();
    }

    protected abstract ListBaseAdapter getListAdapter();





    // 下拉刷新数据
    @Override
    public void onRefresh() {
        if (mState == STATE_REFRESH) {
            return;
        }
        // 设置顶部正在刷新
        mListView.setSelection(0);
        setSwipeRefreshLoadingState();
        mCurrentPage = 0;
        mState = STATE_REFRESH;
        requestData(true);
    }


    /** 设置顶部正在加载的状态 */
    protected void setSwipeRefreshLoadingState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
            // 防止多次重复刷新
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    /** 设置顶部加载完毕的状态 */
    protected void setSwipeRefreshLoadedState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
    }


    protected boolean requestDataIfViewCreated() {
        return true;
    }

    protected String getCacheKeyPrefix() {
        return null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {}

    private String getCacheKey() {
        return new StringBuilder(getCacheKeyPrefix()).append("_")
                .append(mCurrentPage).toString();
    }

    // 是否需要自动刷新
    protected boolean needAutoRefresh() {
        return true;
    }

    /***
     * 获取列表数据
     *
     *
     * @author 火蚁 2015-2-9 下午3:16:12
     *
     * @return void
     * @param refresh
     */
    protected void requestData(boolean refresh) {
//        String key = getCacheKey();
//        if (isReadCacheData(refresh)) {
//            readCacheData(key);
//        } else {
//            // 取新的数据
        sendRequestData();
//        }
    }

    protected void sendRequestData() {
    }

    /*
        * 通用转换,在之类中实现
        * */
    protected ListEntity parseComAvoList(List<?> listavos ) throws Exception {
        return null;
    }

    public  void DoComHandleTask( List<?> avolist,boolean fromCache)
    {
        cancelParserTask();
        Log.d("DoComHandleTask", "进入任务1");
        mParserTask= new ParserComTask(this,avolist,fromCache);

        mParserTask.execute();

    }

    // Parse model when request data success.
    private static class ParserComTask extends AsyncTask<Void, Void, String> {
        private WeakReference<BaseAMListFragment> mInstance;
        //        private byte[] responseData;
        private boolean parserError;
        private boolean fromCache;
        List<?> listcomavos;

        private List<?> list;
        int deType=0;//0 为 avo对象；1 为转换  4  为通用

//        public ParserComTask(BaseComRecycleViewFragment instance,  List<AVObject> listobjs, boolean fromCache) {
//            this.mInstance = new WeakReference<>(instance);
//            this.listavos = listobjs;
//            this.fromCache = fromCache;
//            deType=0;
//        }


        public ParserComTask(BaseAMListFragment instance,  List<?> listobjs, boolean fromCache) {
            this.mInstance = new WeakReference<>(instance);
            this.listcomavos = listobjs;
            this.fromCache = fromCache;
            deType=4;
        }



        @Override
        protected String doInBackground(Void... params) {
            BaseAMListFragment instance = mInstance.get();
            if (instance == null) return null;
            try {
                Log.d("DoComHandleTask", "进入耗时任务2");
//                ListEntity data = instance.parseAvoList(listavos);
                ListEntity data = instance.parseComAvoList(listcomavos);
                if (!fromCache) {
                    //缓存，怎么考虑了，有待研究实现，应该是传语句，和项目条数过来，自动缓存数据
                    //第一行都需要缓存
//                    UIManager.sendNoticeBroadcast(instance.getActivity(), data);
                }
//              //从缓存里面读
//                if (!fromCache && instance.mCurrentPage == 0 && !TextUtils.isEmpty(instance.getCacheKey())) {
//                    CacheManager.setCache(instance.getCacheKey(), responseData,
//                            instance.getCacheExpire(), CacheManager.TYPE_INTERNAL);
//                }
                list = data.getList();
            } catch (Exception e) {
                e.printStackTrace();
                parserError = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.v(" 55","55");
            BaseAMListFragment instance = mInstance.get();
            if (instance != null) {
                if (parserError) {
                    //instance.readCacheData(instance.getCacheKey());
                    instance.executeOnLoadDataError(null);
                } else {
                    instance.executeOnLoadDataSuccess(list);
                    if (!fromCache) {
                        if (instance.mState == STATE_REFRESH) {
                            instance.onRefreshNetworkSuccess();
                        }
                    }
                    instance.executeOnLoadFinish();
                }
                if (fromCache) {
//                    TLog.log(TAG, "key:" + instance.getCacheKey()
//                            + ",set cache data finish ,begin to load network data.");
//                    instance.requestData(true);
                    instance.refresh();
                }
            }
        }
    }

    protected void executeOnLoadDataSuccess(List<?> data) {
        if (mCurrentPage == 0) {
            mAdapter.clear();
        }
        if (mState == STATE_REFRESH)//更新状态
            mAdapter.clear();
        mAdapter.addData(data);//直接增加数据，即可
        mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        if (data.size() == 0 && mState == STATE_REFRESH) {
            mErrorLayout.setErrorType(EmptyLayout.NODATA);
        } else if (data.size() < TDevice.getPageSize()) {
            if (mState == STATE_REFRESH)
                mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
            else
                mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
        } else {
            mAdapter.setState(ListBaseAdapter.STATE_LOAD_MORE);
        }
    }

    /**
     * 是否需要隐藏listview，显示无数据状态
     *
     * @author 火蚁 2015-1-27 下午6:18:59
     *
     */
    protected boolean needShowEmptyNoData() {
        return true;
    }

    public void refresh() {
        mCurrentPage = 0;
        mState = STATE_REFRESH;
        requestData(true);
    }



    protected String getEmptyTip() {
        return null;
    }

    protected void onRefreshNetworkSuccess() {
        // TODO do nothing
    }

    protected boolean useSingleState() {
        return false;
    }

    protected void executeOnLoadDataError(String error) {
        if (mCurrentPage == 0) {
            if (mAdapter.getDataSize() == 0) {
                if(useSingleState()){
                    mAdapter.setState(RecycleBaseAdapter.STATE_SINGLE_ERROR);
                } else {
                    mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                }
            } else {
                mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                String message = error;
                if (TextUtils.isEmpty(error)) {
                    if (TDevice.hasInternet()) {
                        message = getString(R.string.tip_load_data_error);
                    } else {
                        message = getString(R.string.tip_network_error);
                    }
                }
                BaseApplication.showToastShort(message);
            }
        } else {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mAdapter.setState(ListBaseAdapter.STATE_NETWORK_ERROR);
        }
        mAdapter.notifyDataSetChanged();
    }

    protected void executeOnLoadFinish() {
        mSwipeRefreshLayout.setRefreshing(false);//完成后，需要隐藏图标
        mState = STATE_NONE;
    }


    private void cancelParserTask() {
        if (mParserTask != null) {
            mParserTask.cancel(true);
            mParserTask = null;
        }
    }


    public void onLastItemVisible() {
        if (mState == STATE_NONE) {
            if (mAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE) {
                mCurrentPage++;
                mState = STATE_LOADMORE;
                requestData(false);
            }
        }
    }

    private AbsListView.OnScrollListener mListScrollListener = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (mState== STATE_NONE && mAdapter != null
                    && mAdapter.getDataSize() > 0
                    && mListView.getLastVisiblePosition() == (mListView
                    .getCount() - 1)) {
                onLastItemVisible();
            }
        }
    };

}
