package com.jyx.android.adapter.chat;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jyx.android.R;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.ConversationInfo;
import com.jyx.android.net.ApiManager;
import com.jyx.android.rx.CommonExceptionHandler;
import com.jyx.android.rx.ResultConvertFunc;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 1/26/2016.
 */
public class NotifitationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<ConversationInfo> conversationInfoList ;
    Context context;
    public NotifitationAdapter(List<ConversationInfo> list,Context context) {
        this.context = context;
        conversationInfoList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout
                .list_cell_system_notifitation,null);
        return new NotifitationHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final ConversationInfo info = conversationInfoList.get(position);
            NotifitationHolder notifitationHolder = (NotifitationHolder)holder;
            notifitationHolder.tvLastMsg.setText(info.getContent());
            notifitationHolder.tvConversationName.setText(info.getConversationName());
            notifitationHolder.ivAvatar.setImageURI(Uri.parse(info.getConversationAvatar()));
            notifitationHolder.tvOk.setVisibility(View.INVISIBLE);
            notifitationHolder.tvCancel.setVisibility(View.INVISIBLE);
            notifitationHolder.tvState.setVisibility(View.VISIBLE);
            switch (info.getState()){
                case ConversationInfo.STATE_OK://已同意
                    notifitationHolder.tvState.setText("已同意");
                    break;
                case ConversationInfo.STATE_REFUSE://已拒绝
                    notifitationHolder.tvState.setText("已拒绝");
                    break;
                case ConversationInfo.STATE_WAIT://等待处理
                    notifitationHolder.tvState.setVisibility(View.INVISIBLE);
                    notifitationHolder.tvOk.setVisibility(View.VISIBLE);
                    notifitationHolder.tvCancel.setVisibility(View.VISIBLE);
                    notifitationHolder.tvOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //同意
                            if (info.getApplytype().equals("1")){
                                    handleWaitFriendApply(conversationInfoList.get(position)
                                            ,2,position,1);
                            }
                            else{
                                handleWaitFriendApply(conversationInfoList.get(position)
                                        ,2,position,2);
                            }
                        }
                    });
                    notifitationHolder.tvCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //拒绝
                            if (info.getApplytype().equals("1")){
                                    handleWaitFriendApply(conversationInfoList.get(position)
                                            ,2,position,1);
                            }else
                            handleWaitFriendApply(conversationInfoList.get(position)
                                    ,3,position,1);
                        }
                    });
                    break;
                case ConversationInfo.STATE_WAIT_BY://等待对方处理
                    notifitationHolder.tvState.setText("等待审核");
                    break;

            }
    }

    @Override
    public int getItemCount() {
        return conversationInfoList.size();
    }

    static class NotifitationHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.iv_avatar)
        SimpleDraweeView ivAvatar;
        @Bind(R.id.tv_conversation_name)
        TextView tvConversationName;
        @Bind(R.id.tv_last_msg)
        TextView tvLastMsg;

        @Bind(R.id.tv_ok)
        TextView tvOk;
        @Bind(R.id.tv_cancel)
        TextView tvCancel;
        @Bind(R.id.tv_state)
        TextView tvState;
        public NotifitationHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

    /**
     处理好友请求
     msgtype   1为群申请  2为好友
     */
    private void handleWaitFriendApply(final ConversationInfo info,final int type,final
                                       int position,int msgtype){
        String xmlString = "";
        if (msgtype==1){
            xmlString = "{\"function\":\"managegroupapply\",\"userid\":\"" +
                    UserRecord.getInstance().getUserId() + "\",\"applyid\":\""+info.getConversationId()+
                    "\",\"applytype\":\""+type+"\"}";
        }else {
            xmlString = "{\"function\":\"managefriendapply\",\"applyid\":\"" +
                    info.getConversationId() + "\",\"applytype\":\""+type+"\"}";
        }

        ApiManager.getApi().applyFriend(xmlString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new ResultConvertFunc<List<Void>>())
                .subscribe(new Subscriber<List<Void>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(List<Void> voidBaseEntry) {
                        info.setState(type ==2?ConversationInfo
                                .STATE_OK:ConversationInfo.STATE_REFUSE);
                        notifyItemChanged(position);
                    }
                });
    }

}
