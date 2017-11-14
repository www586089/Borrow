package com.jyx.android.fragment.me;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.activity.sign.AutomaticLogon;
import com.jyx.android.adapter.me.MyLoveAdapter;
import com.jyx.android.base.BaseListFragment;
import com.jyx.android.base.ListBaseAdapter;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.ItemBean;
import com.jyx.android.model.ItemListEntity;
import com.jyx.android.model.ListEntity;
import com.jyx.android.model.MyLoveItem;
import com.jyx.android.model.MyLoveList;
import com.jyx.android.net.ApiManager;
import com.jyx.android.net.BizException;
import com.jyx.android.rx.CommonExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/3/11.
 */
public class MyLoveFragment extends BaseListFragment {
    protected static final String TAG = MyLoveFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "MyLoveFragment";
    private static final String FRIEND_SCREEN = "MyLoveFragment";
    public static final String BUNDLE_KEY_UID = "MyLoveFragment";
    private int mUid;
    private MyLoveAdapter myloveadapter = null;
    private String user_id;

    //查看信息
    public void ViewMyLove(String ltype, String id)
    {
        if (ltype.equals("1"))
        {
            //动态详情
        }
        else
        {
            String param = "{\"function\":\"getiteminfor\",\"userid\":\"" + UserRecord.getInstance().getUserId()
                    + "\",\"itemid\":\"" + id + "\"}";
            Call<BaseEntry<List<ItemBean>>> result = ApiManager.getApi().getItemList(param);
            result.enqueue(new Callback<BaseEntry<List<ItemBean>>>() {
                @Override
                public void onResponse(Response<BaseEntry<List<ItemBean>>> response) {
                    Log.e(TAG, "onResponse");
                    if (response.isSuccess()) {
                        BaseEntry<List<ItemBean>> body = response.body();
                        if (0 == body.getResult()) {
                            List<ItemBean> resultList = body.getData();
                            ItemBean itemBean =resultList.get(0);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("item", itemBean);
                            ActivityHelper.goBuyProcedureExt(getContext(), bundle);
                        }

                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, "onFailure");
                    DoComHandleTask(null, false);
                }
            });
        }
    }

    //删除
    public void DeleteItem(String id, final int i)
    {
        String xmlString = "";

        xmlString = "{\"function\":\"loveitem\",\"userid\":\"" + UserRecord.getInstance().getUserId() + "\",\"itemid\":\"" + id + "\"}";
        ApiManager.getApi()
                .getMyItemList(xmlString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseEntry<List<ItemListEntity>>, List<ItemListEntity>>() {
                    @Override
                    public List<ItemListEntity> call(BaseEntry<List<ItemListEntity>> listBaseEntry) {
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
                .subscribe(new Subscriber<List<ItemListEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(List<ItemListEntity> myItemList) {
                        myloveadapter.delete(i);
                        myloveadapter.removeItem(i);
                        myloveadapter.notifyDataSetChanged();
                    }
                });
    }



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
        if (null == myloveadapter) {
            myloveadapter = new MyLoveAdapter(this);
        }
    }

    @Override
    protected ListBaseAdapter getListAdapter() {
        if (null == myloveadapter) {
            myloveadapter = new MyLoveAdapter(this);
        }
        return myloveadapter;
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
        MyLoveList flist = new MyLoveList();
        final List<MyLoveItem> itemsList = new ArrayList<MyLoveItem>();

        if (listavos != null) {
            List<MyLoveItem> listobjs = (List<MyLoveItem>) listavos;
            for (MyLoveItem object : listobjs) {
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

        String xmlString = "";

        xmlString = "{\"function\":\"getmylovelist\",\"userid\":\"" + user_id + "\",\"pagenumber\":\"" + String.valueOf(e) + "\"}";
        ApiManager.getApi()
                .getMyLoveList(xmlString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseEntry<List<MyLoveItem>>, List<MyLoveItem>>() {
                    @Override
                    public List<MyLoveItem> call(BaseEntry<List<MyLoveItem>> listBaseEntry) {
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
                .subscribe(new Subscriber<List<MyLoveItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(List<MyLoveItem> myLoveItemList) {
                        if (myLoveItemList == null) {
                            DoComHandleTask(null, false);
                        } else {
                            DoComHandleTask(myLoveItemList, false);
                        }
                    }
                });
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
