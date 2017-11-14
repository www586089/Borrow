package com.jyx.android.fragment.chat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.google.gson.Gson;
import com.jyx.android.adapter.chat.AbsDisplayFragment;
import com.jyx.android.adapter.chat.MessgeGroupMomentsAdapter;
import com.jyx.android.base.RecycleBaseAdapter;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.GroupInfoBean;
import com.jyx.android.model.NewsFriendsBean;
import com.jyx.android.model.param.GetGroupInfoParam;
import com.jyx.android.model.param.GetMyGroupListParam;
import com.jyx.android.net.ApiManager;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 消息群动态
 * Author : gaobo
 * Date : 2015-10-30
 */
public class MessgeGroupMomentsFragment extends AbsDisplayFragment {

    protected static final String TAG = RentalFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "GroupMomentsFragment";
    private static final String FRIEND_SCREEN = "GroupMomentsFragment";
    public static final String BUNDLE_KEY_UID = "GroupMomentsFragment";
    private int mUid;
    private List<NewsFriendsBean> groupNewsList = new ArrayList<NewsFriendsBean>();
    private List<NewsFriendsBean> nfbean;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

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
        initCommentDialog();
//        UIHelper.sendBroadcastForNotice(getActivity());
        nfbean=new ArrayList<NewsFriendsBean>();
    }

    @Override
    protected RecycleBaseAdapter getListAdapter() {
        if (null == displayAdapter) {
            displayAdapter = new MessgeGroupMomentsAdapter(getActivity(),this);
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
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void getData() {
        if (0 == mCurrentPage) {
            mCurrentPage = 1;
        }
        threadGetData();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            GetMyGroupListParam xmlGroupList = new GetMyGroupListParam();
            xmlGroupList.setFunction("getmygrouplist");
            xmlGroupList.setUserid(UserRecord.getInstance().getUserId());
            final Call<BaseEntry<List<GroupInfoBean>>> resultGruopList = ApiManager.getApi().getMyGroupList(new Gson().toJson(xmlGroupList));
            try {
                Response<BaseEntry<List<GroupInfoBean>>> response = resultGruopList.execute();
                if (null != response && response.isSuccess()) {
                    BaseEntry<List<GroupInfoBean>> body = response.body();
                    if (null != body && 0 == body.getResult()) {
                        List<GroupInfoBean> groupInfoBeanList = body.getData();
                        for (GroupInfoBean bean : groupInfoBeanList) {
                                getGroupInfoSync(bean,groupInfoBeanList.size());
                        }
                        //做了时间排序处理
//                        final List<NewsFriendsBean> itemBeanList = new ArrayList<NewsFriendsBean>();
//                        itemBeanList.addAll(itemBeansSet);
//                        Collections.sort(itemBeanList, new Comparator<NewsFriendsBean>() {
//                            @Override
//                            public int compare(NewsFriendsBean lhs, NewsFriendsBean rhs) {
//                                Date lhsDate = getFormatDate(lhs.getCreatedat());
//                                Date rhsDate = getFormatDate(rhs.getCreatedat());
//                                if (null == rhsDate && null != lhsDate) {
//                                    return -1;
//                                } else if (null != rhsDate && null == lhsDate) {
//                                    return 1;
//                                } else if (null == rhsDate && null == lhsDate) {
//                                    return 0;
//                                }
//                                return rhsDate.compareTo(lhsDate);
//                            }
//                        });
//                        if (null != getActivity()) {
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    DoComHandleTask(itemBeanList, false);
//                                }
//                            });
//                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                DoComHandleTask(null, false);
            }
        }
    };

    private void threadGetData() {
        new Thread(runnable).start();
    }

    private Date getFormatDate(String dateStr) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 重新获取群列表
     */
    public void getGroupList() {
            displayAdapter.setData(nfbean);
//        GetMyGroupListParam xmlGroupList = new GetMyGroupListParam();
//        xmlGroupList.setFunction("getmygrouplist");
//        xmlGroupList.setUserid(UserRecord.getInstance().getUserId());
//        final Call<BaseEntry<List<GroupInfoBean>>> resultGruopList = ApiManager.getApi().getMyGroupList(new Gson().toJson(xmlGroupList));
//        resultGruopList.enqueue(new Callback<BaseEntry<List<GroupInfoBean>>>() {
//            @Override
//            public void onResponse(Response<BaseEntry<List<GroupInfoBean>>> response) {
//                if (null != response && response.isSuccess()) {
//                    BaseEntry<List<GroupInfoBean>> body = response.body();
//                    if (null != body && 0 == body.getResult()) {
//                        List<GroupInfoBean> item=new ArrayList<GroupInfoBean>();
//                        displayAdapter.setData(item);
//                        List<GroupInfoBean> groupInfoBeanList = body.getData();
//                        for (GroupInfoBean bean : groupInfoBeanList) {
//                                getGroupInfoSync(bean,groupInfoBeanList.size());
//                        }
//                        //DoComHandleTask(groupNewsList, false);
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                DoComHandleTask(null, false);
//            }
//        });
    }

    /**
     * 获取群动态列表
     * @param bean
     * @param
     * @throws IOException
     */
    private void getGroupInfoSync(final GroupInfoBean bean, final int size) {
        GetGroupInfoParam xml = new GetGroupInfoParam();
        xml.setFunction("getgroupnews");
        xml.setUserid(UserRecord.getInstance().getUserId());
        xml.setGroupid(bean.getGroup_id());
        xml.setPagenumber(mCurrentPage);
        Call<BaseEntry<List<NewsFriendsBean>>> result = ApiManager.getApi().getFriendGroupNews(new Gson().toJson(xml));
        result.enqueue(new Callback<BaseEntry<List<NewsFriendsBean>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<NewsFriendsBean>>> response) {
                        if (null != response && response.isSuccess()) {
                            BaseEntry<List<NewsFriendsBean>> body = response.body();
                            if (null != body && 0 == body.getResult()) {
                                for (NewsFriendsBean friendsBean : body.getData()) {
                                    friendsBean.setGroup_id(bean.getGroup_id());
                                    friendsBean.setGroupName(bean.getGroup_name());
                                    friendsBean.setGroupPortraitUri(bean.getImagejson());
                                }
                                nfbean.add(body.getData().get(0));
                                if (nfbean.size()==size){
                                    DoComHandleTask(nfbean, false);
                                }
                            }
                        }
                    }
            @Override
            public void onFailure(Throwable t) {
                DoComHandleTask(null, false);
            }
        });

    }


    public void getGroupInfo(final NewsFriendsBean bean) {
        GetGroupInfoParam xml = new GetGroupInfoParam();
        xml.setFunction("getgroupnews");
        xml.setUserid(UserRecord.getInstance().getUserId());
        xml.setGroupid(bean.getGroup_id());
        xml.setPagenumber(mCurrentPage);
        Call<BaseEntry<List<NewsFriendsBean>>> result = ApiManager.getApi().getFriendGroupNews(new Gson().toJson(xml));
        result.enqueue(new Callback<BaseEntry<List<NewsFriendsBean>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<NewsFriendsBean>>> response) {
                if (null != response && response.isSuccess()) {
                    BaseEntry<List<NewsFriendsBean>> body = response.body();
                    if (null != body && 0 == body.getResult()) {
                        //groupNewsList.addAll(body.getData());
                        for (NewsFriendsBean friendsBean : body.getData()) {
                            friendsBean.setGroup_id(bean.getGroup_id());
                            friendsBean.setGroupName(bean.getGroupName());
                            friendsBean.setGroupPortraitUri(bean.getGroupPortraitUri());
                        }
                        displayAdapter.setData(body.getData());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }
}