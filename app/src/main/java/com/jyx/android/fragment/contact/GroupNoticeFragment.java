package com.jyx.android.fragment.contact;

import android.os.Bundle;
import android.view.View;

import com.jyx.android.R;
import com.jyx.android.activity.contact.GroupActivity;
import com.jyx.android.activity.sign.AutomaticLogon;
import com.jyx.android.adapter.chat.GrouNoticepAdapter;
import com.jyx.android.base.BaseListFragment;
import com.jyx.android.base.ListBaseAdapter;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.GroupNoticeItemBean;
import com.jyx.android.model.GroupNoticeList;
import com.jyx.android.model.ListEntity;
import com.jyx.android.net.ApiManager;
import com.jyx.android.net.BizException;
import com.jyx.android.rx.CommonExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 群通知
 * Created by gaobo on 2015/10/31.
 */
public class GroupNoticeFragment extends BaseListFragment {
    private static final String CACHE_KEY_PREFIX = "GroupNoticeFragment";
    private static final String FRIEND_SCREEN = "GroupNoticeFragment";
    public static final String BUNDLE_KEY_UID = "key_uid";
    private int mUid;

    private String user_id;
    private GrouNoticepAdapter babyadpater = null;



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
        user_id = UserRecord.getInstance().getUserId();
        super.initView(view);
        if (null == babyadpater) {
            babyadpater = new GrouNoticepAdapter(this);
        }
    }

    @Override
    protected ListBaseAdapter getListAdapter() {
        if (null == babyadpater) {
            babyadpater = new GrouNoticepAdapter(this);
        }

        return babyadpater;
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
    protected ListEntity parseComAvoList(List<?> listavos ) throws Exception {
        GroupNoticeList flist = new GroupNoticeList();
        final List<GroupNoticeItemBean> itemsList = new ArrayList<GroupNoticeItemBean>();

        if (listavos != null) {
            List<GroupNoticeItemBean> listobjs = (List<GroupNoticeItemBean>) listavos;
            for (GroupNoticeItemBean object : listobjs) {
                itemsList.add(object);
            }
        }

        flist.setItemlist(itemsList);
        return flist;
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
        int e =  mCurrentPage + 1;
       String param = "{\"function\":\"getgroupnotice\",\"userid\":\"" + UserRecord.getInstance().getUserId()
                + "\",\"groupid\":\"" + getArguments().getString(GroupActivity.KEY_GROUP_ID) + "\",\"pagenumber\":\""+"1"+"\"}";
        ApiManager.getApi()
                .getGroupnotice(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseEntry<List<GroupNoticeItemBean>>, List<GroupNoticeItemBean>>() {
                    @Override
                    public List<GroupNoticeItemBean> call(BaseEntry<List<GroupNoticeItemBean>> listBaseEntry) {
                        if (listBaseEntry == null) {
                            throw new BizException(-1, getString(R.string.load_data_error));
                        }

                        if (listBaseEntry.getResult() != 0) {
                            if (listBaseEntry.getMsg().equals(getString(R.string.session_data_error)))
                                new AutomaticLogon(getContext()).login();
                            else
                                throw new BizException(listBaseEntry.getResult(), listBaseEntry.getMsg());
                        }

                        if (listBaseEntry.getData().size()>0) {
                            return listBaseEntry.getData();
                        }
                        else
                        {
                            return null;
                        }
                    }
                })
                .subscribe(new Subscriber<List<GroupNoticeItemBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(List<GroupNoticeItemBean> myItemList) {
                        if (myItemList == null) {
                            DoComHandleTask(null, false);
                        } else {
                            DoComHandleTask(myItemList, false);
                        }
                    }
                });

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

