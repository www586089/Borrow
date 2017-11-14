package com.jyx.android.fragment.me;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.activity.sign.AutomaticLogon;
import com.jyx.android.adapter.me.BabyManageAdater;
import com.jyx.android.base.BaseListFragment;
import com.jyx.android.base.ListBaseAdapter;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BabyManageList;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.ItemBean;
import com.jyx.android.model.ItemListEntity;
import com.jyx.android.model.ListEntity;
import com.jyx.android.model.PicDataInfo;
import com.jyx.android.net.ApiManager;
import com.jyx.android.net.BizException;
import com.jyx.android.rx.CommonExceptionHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
 * 宝贝管理
 * Created by gaobo on 2015/11/9.
 */
public class BabyManageFragment  extends BaseListFragment {

    protected static final String TAG = BabyManageFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "BabyManageFragment";
    private static final String FRIEND_SCREEN = "BabyManageFragment";
    public static final String BUNDLE_KEY_UID = "key_uid";
    private int mUid;

    private String user_id;
    private BabyManageAdater babyadpater = null;
    private int flag=1;

    //下载图片
    public void MyThread(final String imgurl, final ItemListEntity infor, final int i){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    URL url = new URL(imgurl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5 * 1000);
                    conn.setRequestMethod("GET");
                    if(conn.getResponseCode() == 200){
                        Bitmap mBitmap = BitmapFactory.decodeStream(conn.getInputStream());
                        File myCaptureFile = new File(getActivity().getCacheDir().getCanonicalPath()+infor.getItem_id()+"_"+i+".png");
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                        mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                        bos.flush();
                        bos.close();
                        mBitmap.recycle();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private ArrayList<PicDataInfo> dataInfo(ItemListEntity infor) throws JSONException {
        ArrayList<PicDataInfo> picDatainfo=new ArrayList<PicDataInfo>();
        JSONArray jsonArray = new JSONArray(infor.getImagejson());
        if (null != jsonArray && jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    PicDataInfo info=new PicDataInfo();
                    info.setDataPath(getActivity().getCacheDir().getCanonicalPath()+infor.getItem_id()+"_"+i+".png");
                    info.setIsSelected(1);
                    info.setIsSelected(200 + i);
                    picDatainfo.add(info);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return picDatainfo;
    }
    //跳转详情页
    public void xiangqItem(String id){
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


    //编辑
    public void  EditItem(String id, final ItemListEntity infor)
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
                        try {
                            bundle.putParcelableArrayList("selectedItem", dataInfo(infor));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        bundle.putInt("flag", flag);
                        bundle.putInt("type",1);
                        ActivityHelper.goPublishItemExt(getContext(), bundle);
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

    //删除
    public void DeleteItem(String id, final int i)
    {
        String xmlString = "";

        xmlString = "{\"function\":\"delmyitem\",\"userid\":\"" + UserRecord.getInstance().getUserId() + "\",\"itemid\":\"" + id + "\"}";
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
                        babyadpater.delete(i);
                        babyadpater.removeItem(i);
                        babyadpater.notifyDataSetChanged();
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
        if (null == babyadpater) {
            babyadpater = new BabyManageAdater(this);
        }
    }

    @Override
    protected ListBaseAdapter getListAdapter() {
        if (null == babyadpater) {
            babyadpater = new BabyManageAdater(this);
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
        BabyManageList flist = new BabyManageList();
        final List<ItemListEntity> itemsList = new ArrayList<ItemListEntity>();

        if (listavos != null) {
            List<ItemListEntity> listobjs = (List<ItemListEntity>) listavos;
            for (ItemListEntity object : listobjs) {
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

        xmlString = "{\"function\":\"getmyitemlist\",\"userid\":\"" + user_id + "\",\"pagenumber\":\"" + String.valueOf(e) + "\"}";
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
