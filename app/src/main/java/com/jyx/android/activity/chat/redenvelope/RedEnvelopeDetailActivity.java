package com.jyx.android.activity.chat.redenvelope;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jyx.android.R;
import com.jyx.android.adapter.chat.CommonAdapter;
import com.jyx.android.adapter.chat.ViewHolder;
import com.jyx.android.base.BaseActivity;

import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.GroupRedEnvelopResultBean;
import com.jyx.android.model.RedEnvelopResultBean;
import com.jyx.android.model.UserCenterEntity;
import com.jyx.android.net.ApiManager;
import com.jyx.android.rx.CommonExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2/17/2016.
 */
public class RedEnvelopeDetailActivity extends BaseActivity{

    @Bind(R.id.iv_user_avatar)
    SimpleDraweeView mSdvUser;
    @Bind(R.id.tv_nick_name)
    TextView mTvName;
    @Bind(R.id.lv_listview)
    ListView mListView;
    @Bind(R.id.tv_desc)
    TextView mTvDesc;


    String mSendUserId;
    List<GroupRedEnvelopResultBean> mData;
    CommonAdapter<GroupRedEnvelopResultBean> mAdapter;
    private boolean isGroup;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_red_envelope_detail;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.hongbao;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionRightText("");
         isGroup = getIntent().getBooleanExtra("isGroup",false);
        mSendUserId = getIntent().getStringExtra("sendUserId");
        if(mSendUserId.equals(UserRecord.getInstance().getUserId())){
            mSdvUser.setImageURI(Uri.parse(UserRecord.getInstance()
                                                     .getUserEntity().getPortraitUri()));
        }else{
            updateUserInfo(mSendUserId);
        }
        mData = new ArrayList<>();
        if(isGroup){
            ArrayList<GroupRedEnvelopResultBean> list =
                    getIntent().getParcelableArrayListExtra("list");
            mData.addAll(list);
        }else{
            RedEnvelopResultBean bean  = (RedEnvelopResultBean) getIntent().getSerializableExtra
                    ("bean");

            if(bean != null){
                if(bean.getIsreceive().equals("1")){
                    GroupRedEnvelopResultBean groupRedEnvelopResultBean = new
                            GroupRedEnvelopResultBean("",bean.getNickname(),bean
                            .getPortraituri(),bean
                            .getReceivetime(),bean.getSum());
                    mData.add(groupRedEnvelopResultBean);
                }else if(bean.getOvertime().equals("1")){
                    mTvDesc.setText("1个红包，未领取，已过时");
                }else{
                    mTvDesc.setText("1个红包，未领取");
                }
            }

        }


        mAdapter = new CommonAdapter<GroupRedEnvelopResultBean>(this,mData,R.layout.list_cell_red_envelope) {
            @Override
            public void convert(ViewHolder holder, GroupRedEnvelopResultBean
                    bean,int
                    position) {

                holder.setText(R.id.tv_time,bean.getReceivetime());
                holder.setText(R.id.tv_name,bean.getNickname());
                holder.setText(R.id.tv_sum,String.valueOf(bean.getSum())+"元");
                SimpleDraweeView sdv = holder.getView(R.id.iv_avatar);
                sdv.setImageURI(Uri.parse(bean.getPortraituri()));
            }
        };
        mListView.setAdapter(mAdapter);
    }

    /**
     * 查询并更新用户信息
     */
    private void updateUserInfo(final String userId) {
        String xmlString = "";
        xmlString = "{\"function\":\"getusercenter\",\"userid\":\"" +
                userId + "\"}";
        ApiManager.getApi()
                  .getUserCenter(xmlString)
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .map(new Func1<BaseEntry<List<UserCenterEntity>>, UserCenterEntity>() {
                      @Override
                      public UserCenterEntity call(BaseEntry<List<UserCenterEntity>> listBaseEntry) {
                          return listBaseEntry.getData().get(0);
                      }
                  })
                  .subscribe(new Subscriber<UserCenterEntity>() {
                      @Override
                      public void onCompleted() {

                      }

                      @Override
                      public void onError(Throwable e) {
                          CommonExceptionHandler.handleBizException(e);
                      }

                      @Override
                      public void onNext(final UserCenterEntity userCenterEntity) {
                         mSdvUser.setImageURI(Uri.parse(userCenterEntity.getPortraituri()));
                      }
                  });

    }

}
