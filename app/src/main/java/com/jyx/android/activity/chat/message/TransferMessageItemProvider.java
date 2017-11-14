package com.jyx.android.activity.chat.message;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jyx.android.R;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * Created by Administrator on 2/18/2016.
 */

@ProviderTag(messageContent = TransferMessage.class)
public class TransferMessageItemProvider extends IContainerItemProvider
        .MessageProvider<TransferMessage> {

    class ViewHolder {
        TextView message;
        RelativeLayout background;
    }

    @Override
    public void bindView(View view, int i, TransferMessage transferMessage, UIMessage
            uiMessage) {
        ViewHolder holder = (ViewHolder) view.getTag();

        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            holder.background.setBackgroundResource(R.mipmap.icon_message_right);
        } else {
            holder.background.setBackgroundResource(R.mipmap.icon_message_left);
        }
        holder.message.setText(transferMessage.getDescription());
    }

    @Override
    public void onItemClick(View view, int i, TransferMessage transferMessage, UIMessage uiMessage) {

    }

    @Override
    public void onItemLongClick(View view, int i, TransferMessage transferMessage, UIMessage
            uiMessage) {

    }

    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message_transfer,
                null);
        ViewHolder holder = new ViewHolder();
        holder.message = (TextView) view.findViewById(R.id.tv_description);
        holder.background = (RelativeLayout) view.findViewById(R.id.rl_transfer);
        view.setTag(holder);
        return view;
    }


    @Override
    public Spannable getContentSummary(TransferMessage data) {
        return new SpannableString("[转账]");
    }

}