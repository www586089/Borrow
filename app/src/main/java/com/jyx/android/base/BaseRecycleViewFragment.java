package com.jyx.android.base;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyx.android.R;
import com.jyx.android.model.ListEntity;
import com.jyx.android.utils.TDevice;
import com.jyx.android.utils.TLog;
import com.jyx.android.widget.decorator.DividerItemDecoration;
import com.jyx.android.widget.empty.EmptyLayout;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 参考开源中国艾嘛，少量修改，通用列表显示
 * 支持，上拉加载更多，下拉刷新，下拉只刷新一次。
 * 以RecyclerView的形式显示列表
 * Created by gaobo on 2015/10/27.
 */
public abstract class  BaseRecycleViewFragment   extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        RecycleBaseAdapter.OnItemClickListener, RecycleBaseAdapter.OnItemLongClickListener, RecycleBaseAdapter.OnSingleViewClickListener{

    public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";
    private static final String TAG = "BaseRecycleViewFragment";

    protected EmptyLayout mErrorLayout;
    protected int mStoreEmptyState = -1;
    protected String mStoreEmptyMessage;
    protected int mCurrentPage = 0;//当前页数
    protected int mCatalog = 1;//类别，参数，查询时，区分不同的页面


    @Bind(R.id.srl_refresh)
    protected SwipeRefreshLayout srl_refresh;

    @Bind(R.id.recycleView)
    protected RecyclerView mRecycleView;

    protected LinearLayoutManager mLayoutManager;
    protected RecycleBaseAdapter mAdapter;

    private ParserComTask mParserTask;

    protected int getLayoutRes() {
        return R.layout.fragment_pull_refresh_recyclerview;
    }

//    protected int getLayoutRes() {
//        return R.layout.fragment_base_swipe_recyclerview;
//    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mCatalog = args.getInt(BUNDLE_KEY_CATALOG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initView(view);
    }

    /**
     * 初始数据，都放在这里
     * @param view
     */
    @Override
    public void initView(View view) {
        mErrorLayout = (EmptyLayout) view.findViewById(R.id.error_layout);
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCurrentPage = 0;
                mState = STATE_REFRESH;
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                requestData(true);
            }
        });
        //下拉刷新
        srl_refresh.setOnRefreshListener(this);
        //装载滑动事件
        mRecycleView.addOnScrollListener(mScrollListener);
        //增加分割线
        mRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        //布局
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setHasFixedSize(true);

        if (mAdapter != null) {
            mRecycleView.setAdapter(mAdapter);
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        } else {
            mAdapter = getListAdapter();
            mAdapter.setOnItemClickListener(this);
            mAdapter.setOnItemLongClickListener(this);
            mAdapter.setOnSingleViewClickListener(this);
            mRecycleView.setAdapter(mAdapter);

            if (requestDataIfViewCreated()) {
                mCurrentPage = 0;
                mState = STATE_REFRESH;
                if(useSingleState()){
                    mAdapter.setState(RecycleBaseAdapter.STATE_SINGLE_LOADING);
                } else {
                    mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                }
                requestData(requestDataFromNetWork());
            } else {
                mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            }
        }

    }

    /**
     * 创建就请求数据
     * @return
     */
    protected boolean requestDataIfViewCreated() {
        return true;
    }

    @Deprecated
    protected boolean requestDataFromNetWork() {
        return false;
    }
    /**
     * 滑动加载事件，加载更多
     */
    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            Application.showToastShort("加载更多");
            super.onScrolled(recyclerView, dx, dy);

            int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            int totalItemCount = mLayoutManager.getItemCount();
            TLog.log(TAG, "加载更多lastvisble"+Integer.toString(lastVisibleItem)+"total"+Integer.toString(totalItemCount)+"state"+
                    Integer.toString(mState)+"" );


            //如果 检查是否包含页尾，则根据相关状态，确定是否加载
     if(lastVisibleItem >= totalItemCount - 1)
     {
         //是否有页眉
         if (mState == STATE_NONE && mAdapter != null
                 && mAdapter.getDataSize() > 0&& mAdapter.getState()==RecycleBaseAdapter.STATE_LOAD_MORE) {
             //看是否，需要
//             TLog.log(TAG,"加载更多页面");
//             mCurrentPage++;
             loadMore();
         }

     }
        }
    };


    /** 设置顶部正在加载的状态 */
    protected void setSwipeRefreshLoadingState() {
        if (srl_refresh != null) {
            srl_refresh.setRefreshing(true);
            // 防止多次重复刷新
            srl_refresh.setEnabled(false);
        }
    }

    /**
     * 加载更多 分页
     */
    public void loadMore() {
        if (mState == STATE_NONE) {
            if (mAdapter.getState() == RecycleBaseAdapter.STATE_LOAD_MORE
                    || mAdapter.getState() == RecycleBaseAdapter.STATE_NETWORK_ERROR) {
                TLog.log(TAG, "begin to load more data.");
                mCurrentPage++;
                mState = STATE_LOADMORE;
                requestData(false);
            }
        }
    }

    /**
     * 缓存数据，这个地方，可能不好整，需要放到子类
     * @return
     */
    protected String getCacheKey() {
        return new StringBuffer(getCacheKeyPrefix()).append(mCatalog)
                .append("_").append(mCurrentPage).append("_")
                .append(TDevice.getPageSize()).toString();
    }
    protected String getCacheKeyPrefix() {
        return null;
    }

    @Override
    public void onDestroyView() {
        mStoreEmptyState = mErrorLayout.getErrorState();
        mStoreEmptyMessage = mErrorLayout.getMessage();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        //cancelReadCacheTask();
        cancelParserTask();
        super.onDestroy();
    }

    @Override
    public void onItemClick(View view) {
        onItemClick(view, mRecycleView.getChildPosition(view));
    }

    protected void onItemClick(View view, int position) {
    }

    @Override
    public boolean onItemLongClick(View view) {
        return onItemLongClick(view, mRecycleView.getChildPosition(view));
    }

    protected boolean onItemLongClick(View view, int position) {
        return false;
    }

    public void refresh() {
        mCurrentPage = 0;
        mState = STATE_REFRESH;
        setSwipeRefreshLoadingState();//防止多次刷新
        requestData(true);
    }


    public void onRefresh()
    {
        refresh();
    }

    @Override
    public void onSingleViewClick(View view) {
        mCurrentPage = 0;
        mState = STATE_REFRESH;
        mAdapter.setState(RecycleBaseAdapter.STATE_SINGLE_LOADING);
        mAdapter.notifyDataSetChanged();
        requestData(true);
    }

    protected abstract RecycleBaseAdapter getListAdapter();

    /**
     * 请求数据
     * @param refresh
     */
    protected void requestData(boolean refresh) {
        sendRequestData();
    }

    protected void sendRequestData() {
    }

    protected boolean useSingleState() {
        return false;
    }

    public long getCacheExpire() {
        return Constants.CACHE_EXPIRE_OND_DAY;
    }

    /**
     * 是否需要分割线
     * @return
     */
    protected boolean isNeedListDivider() {
        return true;
    }

    /**
     * 通知，数据更新
     */
    protected void notifyDataSetChanged() {
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
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
        mParserTask= new ParserComTask(this,avolist,fromCache);
        mParserTask.execute();
    }

    // Parse model when request data success.
    private static class ParserComTask extends AsyncTask<Void, Void, String> {
        private WeakReference<BaseRecycleViewFragment> mInstance;
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


        public ParserComTask(BaseRecycleViewFragment instance,  List<?> listobjs, boolean fromCache) {
            this.mInstance = new WeakReference<>(instance);
            this.listcomavos = listobjs;
            this.fromCache = fromCache;
            deType=4;
        }



        @Override
        protected String doInBackground(Void... params) {
            BaseRecycleViewFragment instance = mInstance.get();
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
            BaseRecycleViewFragment instance = mInstance.get();
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
                    TLog.log(TAG, "key:" + instance.getCacheKey()
                            + ",set cache data finish ,begin to load network data.");
//                    instance.requestData(true);
                    instance.refresh();
                }
            }
        }
    }

    protected void executeOnLoadDataSuccess(List<?> data) {
        if (mState == STATE_REFRESH)
            mAdapter.clear();
        mAdapter.addData(data);
        mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        if (data.size() == 0 && mState == STATE_REFRESH) {
            if(useSingleState()){
                mAdapter.setState(RecycleBaseAdapter.STATE_SINGLE_EMPTY);
                String emptyTip = getEmptyTip();
                if (!TextUtils.isEmpty(emptyTip))
                    mAdapter.setEmptyText(emptyTip);
            } else {
                mErrorLayout.setErrorType(EmptyLayout.NODATA);
                String emptyTip = getEmptyTip();
                if (!TextUtils.isEmpty(emptyTip))
                    mErrorLayout.setErrorMessage(emptyTip);
            }
        } else if (data.size() < TDevice.getPageSize()) {
            if (mState == STATE_REFRESH)
                mAdapter.setState(RecycleBaseAdapter.STATE_LESS_ONE_PAGE);
            else
                mAdapter.setState(RecycleBaseAdapter.STATE_NO_MORE);
        } else {
            mAdapter.setState(RecycleBaseAdapter.STATE_LOAD_MORE);
        }
    }

    protected String getEmptyTip() {
        return null;
    }

    protected void onRefreshNetworkSuccess() {
        // TODO do nothing
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
            mAdapter.setState(RecycleBaseAdapter.STATE_NETWORK_ERROR);
        }
        mAdapter.notifyDataSetChanged();
    }

    protected void executeOnLoadFinish() {
        srl_refresh.setRefreshing(false);//完成后，需要隐藏图标
        mState = STATE_NONE;
    }


    private void cancelParserTask() {
        if (mParserTask != null) {
            mParserTask.cancel(true);
            mParserTask = null;
        }
    }
}
