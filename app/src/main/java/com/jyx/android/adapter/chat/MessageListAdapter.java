package com.jyx.android.adapter.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jyx.android.R;
import com.jyx.android.base.Constants;
import com.jyx.android.model.ConversationInfo;
import com.jyx.android.model.MessageInfo;
import com.jyx.android.utils.PreferenceHelper;
import com.jyx.android.utils.StringUtils;
import com.jyx.android.utils.TLog;
import com.jyx.android.utils.TimeZoneUtil;
import com.umeng.socialize.utils.ResUtil;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by weilong on 1/25/2016.
 */
public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener , Filterable {


    private String tag = "MessageListAdapter";

    //define interface
        public static interface OnRecyclerViewItemClickListener {
            void onItemClick(View view , Object data);
        }
            private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private List<ConversationInfo> mConversationInfoList;
    private List<ConversationInfo> mUnfilterList;
    Context context;
    Filter filter;

    public MessageListAdapter(List<ConversationInfo> mConversationInfoList, Context
            context) {
        this.mConversationInfoList = mConversationInfoList;
        this.mUnfilterList = mConversationInfoList;
        this.context = context;
        this.filter = prepareFilter();
    }

    private Filter prepareFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<ConversationInfo> result = new ArrayList<>();
                String filterString = constraint.toString().toLowerCase().trim();
                for(ConversationInfo info:mUnfilterList){
                    if(info.getConversationName().contains(filterString)){
                        result.add(info);
                    }else{
                        MessageInfo messageInfo = info.getLastMsgInfo();
                        if(messageInfo != null && messageInfo.getMsgContent().contains
                                (filterString)){
                            result.add(info);
                        }
                    }

                }
                FilterResults filterResults = new FilterResults();
                filterResults.count = result.size();
                filterResults.values = result;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mConversationInfoList = (List<ConversationInfo>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取数据
                mOnItemClickListener.onItemClick(v,v.getTag());
            }
        }

    /**
     * 恢复未排序之前的状态
     */
    public void restore(){
        mConversationInfoList = mUnfilterList;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int
            viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType){
            case ConversationInfo.TYPE_GROUP:
                view = LayoutInflater.from(context).inflate(R.layout.list_cell_group_conversation,
                        null);
                holder = new GroupViewHolder(view);
                view.setOnClickListener(this);
                break;
            case ConversationInfo.TYPE_PERSONAL:
                view = LayoutInflater.from(context).inflate(R.layout
                        .list_cell_person_conversion,null);
                holder = new PersonViewHolder(view);
                view.setOnClickListener(this);
                break;
            case ConversationInfo.TYPE_SYSTEM:
                view = LayoutInflater.from(context).inflate(R.layout
                        .list_cell_system_notifition,null);
                holder = new SystemViewHolder(view);
                view.setOnClickListener(this);
                break;

        }
        return holder;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ConversationInfo info = mConversationInfoList.get(position);
        switch (getItemViewType(position)){
            case ConversationInfo.TYPE_GROUP:
                final GroupViewHolder groupViewHolder = (GroupViewHolder)holder;
                if(!TextUtils.isEmpty(info.getConversationAvatar()))
                    groupViewHolder.ivAvatar.setImageURI(Uri.parse(info.getConversationAvatar()));
                groupViewHolder.tvLastMsgTime.setText(StringUtils.toDateString(info.getLastMsgInfo
                        ().getMsgCreateTime()));
                groupViewHolder.tvLastMsg.setText(info.getLastMsgInfo().getMsgContent());
                groupViewHolder.tvConversationName.setText(info.getConversationName());
                if(info.getUnReadCount() == 0){
                    groupViewHolder.tvUnreadCount.setVisibility(View.INVISIBLE);
                }else{
                    groupViewHolder.tvUnreadCount.setVisibility(View.VISIBLE);
                    groupViewHolder.tvUnreadCount.setText(String.valueOf(info.getUnReadCount()));
                }
                groupViewHolder.itemView.setTag(mConversationInfoList.get(position));
                RongIM.getInstance().getRongIMClient()
                        .getConversationNotificationStatus(Conversation
                                .ConversationType.GROUP, mConversationInfoList.get
                                (position).getConversationId(), new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {


                            @Override
                            public void onSuccess(Conversation
                                                          .ConversationNotificationStatus conversationNotificationStatus) {
                                groupViewHolder.ivNoDisturb.setVisibility
                                        (conversationNotificationStatus.getValue()
                                                ==0?View.VISIBLE:View.INVISIBLE);

                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                            }
                        });
                break;
            case ConversationInfo.TYPE_PERSONAL:
                PersonViewHolder personViewHolder = (PersonViewHolder) holder;
                personViewHolder.ivAvatar.setImageURI(Uri.parse(info.getConversationAvatar()));

                personViewHolder.tvLastMsgTime.setText(StringUtils.toDateString(info
                        .getLastMsgInfo().getMsgCreateTime()));
                personViewHolder.tvLastMsg.setText(info.getLastMsgInfo().getMsgContent());
                personViewHolder.tvConversationName.setText(info.getConversationName());
                if(info.getUnReadCount() == 0){
                    personViewHolder.tvUnreadCount.setVisibility(View.INVISIBLE);
                }else{
                    personViewHolder.tvUnreadCount.setVisibility(View.VISIBLE);
                    personViewHolder.tvUnreadCount.setText(String.valueOf(info.getUnReadCount()));
                }
                personViewHolder.itemView.setTag(mConversationInfoList.get(position));
                personViewHolder.ivPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("message","拨打电话 == "+mConversationInfoList.get(position)
                                .getMobilePhoneNumber());
                       Uri uri = Uri.parse("tel:"+ mConversationInfoList.get(position)
                                .getMobilePhoneNumber());//mobile为你要拨打的电话号码，模拟器中为模拟器编号也可
                        Intent intent = new Intent(Intent.ACTION_DIAL,uri);
                        context.startActivity(intent);
                    }
                });
                break;
            case ConversationInfo.TYPE_SYSTEM:
                SystemViewHolder systemViewHolder = (SystemViewHolder)holder;
//                systemViewHolder.tvLastMsgTime.setText(info.getLastMsgInfo().getMsgCreateTime());
//                systemViewHolder.tvLastMsg.setText(info.getLastMsgInfo().getMsgContent());
//                systemViewHolder.tvConversationName.setText(info.getConversationName());
//                if(info.getUnReadCount() == 0){
//                    systemViewHolder.tvUnreadCount.setVisibility(View.INVISIBLE);
//                }else{
//                    systemViewHolder.tvUnreadCount.setVisibility(View.VISIBLE);
//                    systemViewHolder.tvUnreadCount.setText(String.valueOf(info.getUnReadCount()));
//                }
                break;

        }
    }


    @Override
    public int getItemViewType(int position) {
        return mConversationInfoList.get(position).getConversationType();
    }

    @Override
    public int getItemCount() {
        return mConversationInfoList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }



    static class GroupViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.iv_avatar)
        SimpleDraweeView ivAvatar;
        @Bind(R.id.tv_unread_count)
        TextView tvUnreadCount;
        @Bind(R.id.tv_conversation_name)
        TextView tvConversationName;
        @Bind(R.id.tv_new_sub_group)
        TextView tvNewSubGroup;
        @Bind(R.id.tv_last_msg)
        TextView tvLastMsg;
        @Bind(R.id.iv_no_disturb)
        ImageView ivNoDisturb;
        @Bind(R.id.tv_last_msg_time)
        TextView tvLastMsgTime;
        public GroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
//            ivAvatar = (SimpleDraweeView) itemView.findViewById(R.id.iv_avatar);
//            tvUnreadCount = (TextView) itemView.findViewById(R.id.tv_unread_count);
//            tvConversationName = (TextView) itemView.findViewById(R.id.tv_conversation_name);
//            tvLastMsg = (TextView) itemView.findViewById(R.id.tv_last_msg);
//            ivNoDisturb = (ImageView) itemView.findViewById(R.id.iv_no_disturb);
//            tvLastMsgTime = (TextView) itemView.findViewById(R.id.tv_last_msg_time);
//            tvNewSubGroup = (TextView) itemView.findViewById(R.id.tv_new_sub_group);
        }
    }

    static class PersonViewHolder extends RecyclerView.ViewHolder{
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
            super(itemView);
            ButterKnife.bind(this,itemView);
//            ivAvatar = (SimpleDraweeView) itemView.findViewById(R.id.iv_avatar);
//            tvUnreadCount = (TextView) itemView.findViewById(R.id.tv_unread_count);
//            tvConversationName = (TextView) itemView.findViewById(R.id.tv_conversation_name);
//            tvLastMsg = (TextView) itemView.findViewById(R.id.tv_last_msg);
//            tvLastMsgTime = (TextView) itemView.findViewById(R.id.tv_last_msg_time);
//            ivPhone = (ImageView)itemView.findViewById(R.id.iv_phone);
        }

    }

    static class SystemViewHolder extends RecyclerView.ViewHolder{
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
        public SystemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }
}
