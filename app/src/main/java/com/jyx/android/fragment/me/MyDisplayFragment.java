package com.jyx.android.fragment.me;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.activity.sign.AutomaticLogon;
import com.jyx.android.base.BaseRecycleViewFragment;
import com.jyx.android.base.RecycleBaseAdapter;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.ImageListEntity;
import com.jyx.android.model.ListEntity;
import com.jyx.android.net.ApiManager;
import com.jyx.android.net.BizException;
import com.jyx.android.rx.CommonExceptionHandler;
import com.jyx.android.widget.empty.EmptyLayout;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 我的-我的动态/我的租借/我喜欢的
 * Author : Tree
 * Date : 2015-11-06
 */
public class MyDisplayFragment extends BaseRecycleViewFragment {
    protected static final String TAG = MyDisplayFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "MyDisplayFragment";
    private static final String FRIEND_SCREEN = "MyDisplayFragment";
    public static final String BUNDLE_KEY_UID = "MyDisplayFragment";
    private static String KEY_POSITION = "key.position";
    private static String KEY_USERID = "key.userid";
    private int mUid;
    private int type;
    private String user_id;
    private int position;

    public static MyDisplayFragment newInstance(int p, String u)
    {
        MyDisplayFragment f = new MyDisplayFragment();
        Bundle b = new Bundle();
        b.putInt(KEY_POSITION, p);
        b.putString(KEY_USERID, u);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            position = args.getInt(KEY_POSITION);
            user_id = args.getString(KEY_USERID);
        }
        else {
            position = 0;
            user_id = "";
        }

        super.onCreate(savedInstanceState);
    }

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


        srl_refresh.setEnabled(false);

        //布局
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
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

        //FIXME

    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mUid;
    }

    @Override
    protected RecycleBaseAdapter getListAdapter() {
        return new MyDisplayAdapter();
    }


    private class MyDisplayAdapter extends RecycleBaseAdapter{

        @Override
        protected View onCreateItemView(ViewGroup parent, int viewType) {
            return getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_my_display, null);
        }

        @Override
        protected ViewHolder onCreateItemViewHolder(View view, int viewType) {
            return new MyDisplayFragment.ViewHolder(viewType, view);
        }

        @Override
        protected void onBindItemViewHolder(ViewHolder holder, int position) {
            MyDisplayFragment.ViewHolder viewHolder = (MyDisplayFragment.ViewHolder) holder;

            String imageUrl = (String) _data.get(position);
            viewHolder.sdvDisplay.setImageURI(Uri.parse(imageUrl));
            viewHolder.sdvDisplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (type){
                        case 1:
                            ActivityHelper.goMyRental(getContext());
                            break;
                        case 2:
                            ActivityHelper.goMyLove(getContext());
                    }
                }
            });
        }
    }


    static class ViewHolder extends RecycleBaseAdapter.ViewHolder{
        public SimpleDraweeView sdvDisplay;


        public ViewHolder(int viewType, View v) {
            super(viewType, v);
            sdvDisplay = (SimpleDraweeView) v.findViewById(R.id.sdv_my_display);
        }
    }

    @Override
    protected void sendRequestData() {
        String xmlString = "";
        type=position;
        //修改sqlcmd查询以满足不同数据
        switch (position)
        {
            case 0://我的动态
                xmlString = "{\"function\":\"getimagelist\",\"userid\":\"" + user_id + "\",\"imagetype\":\"news\"}";
                break;
            case 1://我的租借
                xmlString = "{\"function\":\"getimagelist\",\"userid\":\"" + user_id + "\",\"imagetype\":\"borrow\"}";
                break;
            case 2://我喜欢的
                xmlString = "{\"function\":\"getimagelist\",\"userid\":\"" + user_id + "\",\"imagetype\":\"ilike\"}";
                break;
        }

        ApiManager.getApi()
                .getImageList(xmlString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseEntry<List<ImageListEntity>>, ImageListEntity>() {
                    @Override
                    public ImageListEntity call(BaseEntry<List<ImageListEntity>> listBaseEntry) {
                        if (listBaseEntry == null) {
                            throw new BizException(-1, getString(R.string.load_data_error));
                        }

                        if (listBaseEntry.getResult() != 0) {
                            if (listBaseEntry.getMsg().equals(getString(R.string.session_data_error)))
                                new AutomaticLogon(getContext()).login();
                            else
                            throw new BizException(listBaseEntry.getResult(), listBaseEntry.getMsg());
                        }
                        return listBaseEntry.getData().get(0);
                    }
                })
                .subscribe(new Subscriber<ImageListEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(ImageListEntity imageListEntity) {
                        DoComHandleTask(imageListEntity.getImages(), false);
                    }
                });
    }

    @Override
    protected ListEntity parseComAvoList(List<?> listavos) throws Exception {
        final List<String> imageList = new ArrayList<>();

        Log.d("成功", "查询position:" + position);
        List<String> listobjs = (List<String>) listavos;
        for (String object : listobjs) {
            imageList.add(object);
        }

        return new ListEntity() {
            @Override
            public List<?> getList() {
                return imageList;
            }
        };
    }
}
