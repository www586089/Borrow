package com.jyx.android.adapter.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jyx.android.R;
import com.jyx.android.activity.chat.message.HongbaoMessage;
import com.jyx.android.activity.chat.message.TransferMessage;
import com.jyx.android.base.Constants;
import com.jyx.android.model.ConversationInfo;
import com.jyx.android.model.UserEntity;
import com.jyx.android.utils.ACache;
import com.jyx.android.utils.StringUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;

/**
 * Created by Administrator on 3/14/2016.
 */
public class ConversationListAdapterEx extends ConversationListAdapter{


    private String tag = "ConversationListAdapterEx";

    LayoutInflater mInflater;
    Context mContext;

    public ConversationListAdapterEx(Context context) {
        super(context);
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    protected View newView(Context context, int position, ViewGroup group) {
        View view = null;
                view = mInflater.inflate(R.layout
                        .list_cell_person_conversion,null);
                PersonViewHolder personViewHolder = new PersonViewHolder(view);
                view.setTag(personViewHolder);
        return view;
    }

    @Override
    protected void bindView(View v, final int position, final UIConversation data) {
        final PersonViewHolder viewHolder = (PersonViewHolder)v.getTag();
        switch (getItemViewType(position)){
            case ConversationInfo.TYPE_GROUP:
                if(data.getIconUrl() != null)
                        viewHolder.ivAvatar.setImageURI(data.getIconUrl());
                viewHolder.tvLastMsgTime.setText(StringUtils.toDateString(String
                        .valueOf(data.getUIConversationTime())));
                    viewHolder.tvLastMsg.setText(data.getConversationContent());
                viewHolder.tvConversationName.setText(data.getUIConversationTitle());
                if(data.getUnReadMessageCount() == 0){
                    viewHolder.tvUnreadCount.setVisibility(View.INVISIBLE);
                }else{
                    viewHolder.tvUnreadCount.setVisibility(View.VISIBLE);
                    viewHolder.tvUnreadCount.setText(String.valueOf(data.getUnReadMessageCount()));
                }
                viewHolder.ivPhone.setImageResource(R.drawable
                        .ic_no_disturb);
                Log.e(tag," 群的id=== "+data.getConversationTargetId() +" ==="+data
                        .getConversationSenderId() );
                RongIM.getInstance().getRongIMClient()
                        .getConversationNotificationStatus(Conversation
                                .ConversationType.GROUP, data.getConversationTargetId(), new RongIMClient
                                .ResultCallback<Conversation.ConversationNotificationStatus>() {


                            @Override
                            public void onSuccess(Conversation
                                                          .ConversationNotificationStatus conversationNotificationStatus) {

                                viewHolder.ivPhone.setVisibility
                                        (conversationNotificationStatus.getValue()
                                                ==0?View.VISIBLE:View.INVISIBLE);

                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                            }
                        });
                break;
            case ConversationInfo.TYPE_PERSONAL:
                if(data.getIconUrl() != null)
                    viewHolder.ivAvatar.setImageURI(data.getIconUrl());

                    viewHolder.tvLastMsgTime.setText(StringUtils.toDateString
                            (String.valueOf(data.getUIConversationTime())));

                    viewHolder.tvLastMsg.setText(convertMessage(data));

                    viewHolder.tvConversationName.setText(data.getUIConversationTitle());
                    if(data.getUnReadMessageCount() == 0){
                        viewHolder.tvUnreadCount.setVisibility(View.INVISIBLE);
                    }else{
                        viewHolder.tvUnreadCount.setVisibility(View.VISIBLE);
                        viewHolder.tvUnreadCount.setText(String.valueOf(data
                                .getUnReadMessageCount()));
                    }
                ArrayList<UserEntity> list = (ArrayList<UserEntity>) ACache.get(mContext)
                        .getAsObject(Constants.KEY_USER);
                for(UserEntity entity: list){
                    if(entity.getUserId().equals(data
                            .getConversationSenderId()) || entity
                            .getUserId().equals(data.getConversationTargetId())){
                    final   String phone = entity.getMobilePhoneNumber();
                        viewHolder.ivPhone.setImageResource(R.mipmap.icon_phone);
                        viewHolder.ivPhone.setVisibility(View.VISIBLE);
                        viewHolder.ivPhone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ArrayList<UserEntity> list = (ArrayList<UserEntity>) ACache.get(mContext)
                                        .getAsObject(Constants.KEY_USER);
                                        Uri uri = Uri.parse("tel:" +phone);
                                        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        mContext.startActivity(intent);
                                }
                            });
                    }
                }


                break;
            case ConversationInfo.TYPE_SYSTEM:
                viewHolder.tvLastMsgTime.setText(StringUtils.toDateString
                        (String.valueOf(data.getUIConversationTime())));
                viewHolder.ivAvatar.setImageResource(R.mipmap.icon_system_conversation);
                viewHolder.tvLastMsg.setText(data.getConversationContent());
                viewHolder.tvConversationName.setText("系统消息");
                if(data.getUnReadMessageCount() == 0){
                    viewHolder.tvUnreadCount.setVisibility(View.INVISIBLE);
                }else{
                    viewHolder.tvUnreadCount.setVisibility(View.VISIBLE);
                    viewHolder.tvUnreadCount.setText(String.valueOf(data.getUnReadMessageCount()));
                }
                break;

        }

    }

    String convertMessage(UIConversation data){
        MessageContent content = data.getMessageContent();
        if(content instanceof TransferMessage){
            return "[转账]";
        }else if(content instanceof HongbaoMessage){
            return "[红包]";
        }
        return data.getConversationContent().toString();
    }

    @Override
    public int getItemViewType(int position) {
        switch (getItem(position).getConversationType())
        {
            case GROUP:
                return 1;
            case PRIVATE:
                return 0;
            case SYSTEM:
                return 2;
        }
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    static class PersonViewHolder {
        @Bind(R.id.iv_avatar)
        SimpleDraweeView ivAvatar;
        @Bind(R.id.tv_unread_count)
        TextView tvUnreadCount;
        @Bind(R.id.tv_conversation_name)
        TextView tvConversationName;
        @Bind(R.id.tv_last_msg)
        TextView tvLastMsg;
        @Bind(R.id.tv_last_msg_time)
        TextView tvLastMsgTime;
        @Bind(R.id.iv_phone)
        ImageView ivPhone;
        public PersonViewHolder(View itemView) {
            ButterKnife.bind(this,itemView);
        }

    }

}
