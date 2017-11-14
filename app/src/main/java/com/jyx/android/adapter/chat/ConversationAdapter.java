package com.jyx.android.adapter.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.model.ConversationInfo;

import java.util.List;

/**
 * Author : Tree
 * Date : 2015-11-04
 */
public class ConversationAdapter extends BaseAdapter{
    private Context mContext;


    private List<ConversationInfo> mConversationInfoList;

    public ConversationAdapter(Context context, List<ConversationInfo> conversationInfoList){
        mContext = context;
        mConversationInfoList = conversationInfoList;
    }

    @Override
    public int getCount() {
        return mConversationInfoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mConversationInfoList.get(position).getConversationType();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_conversation, null);
            viewHolder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            viewHolder.tvUnreadCount = (TextView) convertView.findViewById(R.id.tv_unread_count);
            viewHolder.tvConversationName = (TextView) convertView.findViewById(R.id.tv_conversation_name);
            viewHolder.tvLastMsg = (TextView) convertView.findViewById(R.id.tv_last_msg);
            viewHolder.ivNoDisturb = (ImageView) convertView.findViewById(R.id.iv_no_disturb);
            viewHolder.tvLastMsgTime = (TextView) convertView.findViewById(R.id.tv_last_msg_time);
            viewHolder.tvNewSubGroup = (TextView) convertView.findViewById(R.id.tv_new_sub_group);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ConversationInfo conversationInfo = mConversationInfoList.get(position);
        boolean isGroup = conversationInfo.getConversationType() == ConversationInfo.TYPE_GROUP;

        viewHolder.ivAvatar.setImageResource(R.mipmap.me_photo_2x_81);
        viewHolder.tvUnreadCount.setVisibility(conversationInfo.getUnReadCount() > 0 ? View.VISIBLE : View.INVISIBLE);
        viewHolder.tvUnreadCount.setText(String.valueOf(conversationInfo.getUnReadCount()));
        viewHolder.tvNewSubGroup.setVisibility(isGroup ? View.VISIBLE : View.INVISIBLE);
        viewHolder.tvConversationName.setText(conversationInfo.getConversationName());
        if(isGroup){
            viewHolder.tvLastMsg.setText(conversationInfo.getLastMsgInfo().getMsgPosterName() + conversationInfo.getLastMsgInfo().getMsgContent());
        }else {
            viewHolder.tvLastMsg.setText(conversationInfo.getLastMsgInfo().getMsgContent());
        }
        viewHolder.ivNoDisturb.setVisibility(conversationInfo.isDND() ? View.VISIBLE : View.INVISIBLE);
        viewHolder.tvLastMsgTime.setText(conversationInfo.getLastMsgInfo().getMsgCreateTime());

        OnListItemClickListener listener = new OnListItemClickListener(mContext);
        viewHolder.tvNewSubGroup.setOnClickListener(listener);
        return convertView;
    }

    private static class OnListItemClickListener implements View.OnClickListener{
        private Context mContext;


        public OnListItemClickListener(Context context){
            mContext = context;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_new_sub_group:
                    ActivityHelper.goNewSubGroup(mContext);
                    break;
                default:
                    break;
            }

        }
    }


    static class ViewHolder{
        ImageView ivAvatar;
        TextView tvUnreadCount;
        TextView tvConversationName;
        TextView tvNewSubGroup;
        TextView tvLastMsg;
        ImageView ivNoDisturb;
        TextView tvLastMsgTime;

    }
}
