package com.jyx.android.fragment.chat;

import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.jyx.android.adapter.chat.AbsDisplayFragment;
import com.jyx.android.adapter.chat.DisplayTreasureAdapter;
import com.jyx.android.base.RecycleBaseAdapter;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.NewsFriendsBean;
import com.jyx.android.model.param.GetFriendNewsParam;
import com.jyx.android.net.ApiManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 消息-动态界面
 * Author : Tree
 * Date : 2015-10-30
 * Author : cooldemo
 * Date : 2015-11-03
 * <p/>
 * http://219.159.88.144:801/index.html#p=%E6%99%92%E5%8A%A8%E6%80%81
 */
public class DisplayTreasureFragment extends AbsDisplayFragment {

    protected static final String TAG = DisplayTreasureFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "DisplayTreasureFragment";
    private static final String FRIEND_SCREEN = "DisplayTreasureFragment";
    public static final String BUNDLE_KEY_UID = "DisplayTreasureFragment";
    private int mUid;


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
//        UIHelper.sendBroadcastForNotice(getActivity());
    }



    @Override
    protected RecycleBaseAdapter getListAdapter() {
        if (null == displayAdapter) {
            displayAdapter = new DisplayTreasureAdapter(getActivity());
            displayAdapter.setCommentClickListener(commentClickListener);
        }
        return displayAdapter;
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mUid;
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

    @Override
    protected void getData() {
        if (0 == mCurrentPage) {
            mCurrentPage = 1;
        }
        GetFriendNewsParam xml = new GetFriendNewsParam();
        xml.setFunction("getfriendgroupnews");
        xml.setPagenumber(mCurrentPage);
        xml.setUserid(UserRecord.getInstance().getUserId());
        Call<BaseEntry<List<NewsFriendsBean>>> result = ApiManager.getApi().getFriendGroupNews(new Gson().toJson(xml));
        result.enqueue(new Callback<BaseEntry<List<NewsFriendsBean>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<NewsFriendsBean>>> response) {
                if (null != response && response.isSuccess()) {
                    BaseEntry<List<NewsFriendsBean>> body = response.body();
                    if (null != body && 0 == body.getResult()) {
                        DoComHandleTask(body.getData(), false);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                DoComHandleTask(null, false);
            }
        });
    }
}
