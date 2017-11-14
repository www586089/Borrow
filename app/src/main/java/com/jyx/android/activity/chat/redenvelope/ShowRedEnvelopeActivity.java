package com.jyx.android.activity.chat.redenvelope;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.jyx.android.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.jyx.android.activity.chat.message.HongbaoMessage;
import com.jyx.android.base.Application;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.GroupRedEnvelopResultBean;
import com.jyx.android.model.RedEnvelopResultBean;
import com.jyx.android.model.param.QueryRedEnvelopParam;
import com.jyx.android.net.ApiManager;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2/17/2016.
 */
public class ShowRedEnvelopeActivity extends Activity{

    @Bind(R.id.tv_open)
    TextView mTvOpen;
    @Bind(R.id.iv_close)
    ImageView mIvClose;
    @Bind(R.id.tv_show_all)
    TextView mTvShowAll;

    HongbaoMessage mHongbaoMessage;
    RedEnvelopResultBean mRedEnvelopResultBean;
    ArrayList<GroupRedEnvelopResultBean> mGroupRedEnvelopResultBeanList = new ArrayList<>();
    String mSendUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_red_envelope);
        ButterKnife.bind(this);
        mHongbaoMessage = getIntent().getParcelableExtra("hongbao");
        mSendUserId = getIntent().getStringExtra("sendUserId");
        //查询状态
        if(mHongbaoMessage.getIsGroup() == 1){
            queryGroupHongbao(mHongbaoMessage,false);
        }else{
            query(mHongbaoMessage,false);
        }
    }

    @OnClick(R.id.tv_open)
    void onOpen(){
        if(mHongbaoMessage.getIsGroup() == 1){
            //是否还有红包未领
            if(mGroupRedEnvelopResultBeanList.size() == mHongbaoMessage.getNumber()){
                startActivity(new Intent(this,RedEnvelopeDetailActivity.class)
                        .putParcelableArrayListExtra("list",
                                mGroupRedEnvelopResultBeanList).putExtra
                                ("isGroup",true).putExtra("sendUserId",mSendUserId));
            }else{
                //领取群红包
                Log.e("tag","领取群红包");
                getGroupPacket(mHongbaoMessage);
            }

        }else{
            //红包未领，判断是否可以领取
            if(mSendUserId.equals(UserRecord.getInstance().getUserId())){
                startActivity(new Intent(this,RedEnvelopeDetailActivity.class)
                        .putExtra("bean",mRedEnvelopResultBean).putExtra
                                ("isgroup",false).putExtra("sendUserId",mSendUserId));
            }else{
                //可以领取
                Log.e("tag","领取个人红包");
                getPersonPacket(mHongbaoMessage);
            }

        }
        finish();
    }

    @OnClick(R.id.tv_show_all)
    void onShowAll(){
        if(mHongbaoMessage.getIsGroup() == 1){
            startActivity(new Intent(this,RedEnvelopeDetailActivity.class)
                    .putParcelableArrayListExtra("list",
                            mGroupRedEnvelopResultBeanList).putExtra
                            ("isgroup",true).putExtra("sendUserId",mSendUserId));
        }else{
            startActivity(new Intent(this,RedEnvelopeDetailActivity.class)
                    .putExtra("bean",mRedEnvelopResultBean).putExtra
                            ("isgroup",false).putExtra("sendUserId",mSendUserId));
        }
        finish();
    }

    @OnClick(R.id.iv_close)
    void onClose(){
        finish();
    }


    void query(HongbaoMessage hongbaoMessage, final boolean gotoNext){
        QueryRedEnvelopParam xml = new QueryRedEnvelopParam();
        xml.setFunction("getpersonalredpacket");
        xml.setUserid(UserRecord.getInstance().getUserId());
        xml.setRedpacketid(hongbaoMessage.getRedPacketsId());

        Call<BaseEntry<List<RedEnvelopResultBean>>> result = ApiManager
                .getApi().queryRedEnvelop
                (new Gson().toJson(xml));
        result.enqueue(new Callback<BaseEntry<List<RedEnvelopResultBean>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<RedEnvelopResultBean>>> response) {
                if (response.isSuccess()) {
                    BaseEntry<List<RedEnvelopResultBean>> body = response.body();
                    if (0 == body.getResult()) {
                         mRedEnvelopResultBean = body.getData().get(0);
                         mRedEnvelopResultBean.setSum(mHongbaoMessage.getSum());
                        if(mRedEnvelopResultBean != null &&
                                mRedEnvelopResultBean.getIsreceive().equals("1")){
                            if(gotoNext){
                                startActivity(new Intent
                                        (ShowRedEnvelopeActivity.this,
                                        RedEnvelopeDetailActivity.class)
                                        .putExtra("bean",mRedEnvelopResultBean).putExtra
                                                ("isgroup",false).putExtra("sendUserId",mSendUserId));
                                finish();
                                return;
                            }
                            setOpenState(true);
                        }else{
                            setOpenState(false);
                        }

                    } else {
                        mTvShowAll.setVisibility(View.INVISIBLE);
                        mTvOpen.setVisibility(View.INVISIBLE);
                        showToast(body.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Application.showToast(R.string.buy_right_now_pay_failure);
            }
        });
    }

    void setOpenState(boolean openState){
        if(openState){
            mTvShowAll.setVisibility(View.INVISIBLE);
            mTvOpen.setEnabled(false);
            mTvOpen.setText("手慢了,红包派完了");
            mIvClose.setVisibility(View.GONE);
        }else{
            mTvShowAll.setVisibility(View.INVISIBLE);
            mIvClose.setVisibility(View.VISIBLE);
            mTvOpen.setEnabled(true);
            mTvOpen.setText("拆红包");
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();

    }

    void queryGroupHongbao(final HongbaoMessage hongbaoMessage, final boolean
            gotoNext){
        QueryRedEnvelopParam xml = new QueryRedEnvelopParam();
        xml.setFunction("getgroupredpacket");
        xml.setUserid(UserRecord.getInstance().getUserId());
        xml.setRedpacketid(hongbaoMessage.getRedPacketsId());

        Call<BaseEntry<List<GroupRedEnvelopResultBean>>> result = ApiManager
                .getApi().queryGroupRedEnvelop
                        (new Gson().toJson(xml));
        result.enqueue(new Callback<BaseEntry<List<GroupRedEnvelopResultBean>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<GroupRedEnvelopResultBean>>> response) {
                if (response.isSuccess()) {
                    BaseEntry<List<GroupRedEnvelopResultBean>> body = response.body();
                    if (0 == body.getResult()) {
                        List<GroupRedEnvelopResultBean> list =  body.getData();
                        mGroupRedEnvelopResultBeanList.clear();
                        for(GroupRedEnvelopResultBean bean:list){
                            bean.setSum(mHongbaoMessage.getSum());
                        }
                        mGroupRedEnvelopResultBeanList.addAll(list);
                        if(gotoNext){
                            startActivity(new Intent(ShowRedEnvelopeActivity
                                    .this,
                                    RedEnvelopeDetailActivity.class)
                                    .putParcelableArrayListExtra("list",
                                            mGroupRedEnvelopResultBeanList).putExtra
                                            ("isGroup",true).putExtra("sendUserId",mSendUserId));
                            finish();
                        }
                        if(mGroupRedEnvelopResultBeanList.size()== hongbaoMessage
                                .getNumber()){
                            //红包领完
                            setOpenState(true);
                        }else{
                            setOpenState(false);
                        }

                    } else {
                        showToast(body.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Application.showToast(R.string.buy_right_now_pay_failure);
            }
        });
    }



    void getPersonPacket(HongbaoMessage hongbaoMessage){
        QueryRedEnvelopParam xml = new QueryRedEnvelopParam();
        xml.setFunction("recvpersonalredpacket");
        xml.setUserid(UserRecord.getInstance().getUserId());
        xml.setRedpacketid(hongbaoMessage.getRedPacketsId());

        Call<BaseEntry<List<RedEnvelopResultBean>>> result = ApiManager
                .getApi().queryRedEnvelop
                        (new Gson().toJson(xml));
        result.enqueue(new Callback<BaseEntry<List<RedEnvelopResultBean>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<RedEnvelopResultBean>>> response) {
                if (response.isSuccess()) {
                    BaseEntry<List<RedEnvelopResultBean>> body = response.body();
                    if (0 == body.getResult()) {
                        //领取成功，查询然后直接跳到详情
                        query(mHongbaoMessage,true);

                    } else {
                        showToast(body.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Application.showToast(R.string.buy_right_now_pay_failure);
            }
        });
    }




    void getGroupPacket(HongbaoMessage hongbaoMessage){
        QueryRedEnvelopParam xml = new QueryRedEnvelopParam();
        xml.setFunction("recvgroupredpacket");
        xml.setUserid(UserRecord.getInstance().getUserId());
        xml.setRedpacketid(hongbaoMessage.getRedPacketsId());

        Call<BaseEntry<List<RedEnvelopResultBean>>> result = ApiManager
                .getApi().queryRedEnvelop
                        (new Gson().toJson(xml));
        result.enqueue(new Callback<BaseEntry<List<RedEnvelopResultBean>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<RedEnvelopResultBean>>> response) {
                if (response.isSuccess()) {
                    BaseEntry<List<RedEnvelopResultBean>> body = response.body();
                    if (0 == body.getResult() || 81 == body.getResult()) {
                        //领取成功，查询然后直接跳到详情
                        queryGroupHongbao(mHongbaoMessage,true);
                    } else {
                        showToast(body.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Application.showToast(R.string.buy_right_now_pay_failure);
            }
        });
    }

}
