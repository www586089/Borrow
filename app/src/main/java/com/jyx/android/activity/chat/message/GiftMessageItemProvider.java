package com.jyx.android.activity.chat.message;

import android.content.Context;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jyx.android.R;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;
import io.rong.message.RichContentMessage;

/**
 * Created by Administrator on 2/18/2016.
 */

@ProviderTag(messageContent = RichContentMessage.class)
public class GiftMessageItemProvider extends IContainerItemProvider
        .MessageProvider<RichContentMessage> {

    class ViewHolder {
        TextView message;
        RelativeLayout background;
        SimpleDraweeView sdvImage;
    }

    @Override
    public void bindView(View view, int i, RichContentMessage message, UIMessage
            uiMessage) {
        ViewHolder holder = (ViewHolder) view.getTag();

        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            holder.background.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
        } else {
            holder.background.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
        }
        holder.message.setText(message.getTitle());
        holder.sdvImage.setImageURI(Uri.parse(message.getImgUrl()));
    }

    @Override
    public void onItemClick(View view, int i, RichContentMessage transferMessage, UIMessage uiMessage) {

    }

    @Override
    public void onItemLongClick(View view, int i, RichContentMessage message, UIMessage
            uiMessage) {

    }

    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message_gift,
                null);
        ViewHolder holder = new ViewHolder();
        holder.message = (TextView) view.findViewById(R.id.tv_description);
        holder.background = (RelativeLayout) view.findViewById(R.id.rl_gift);
        holder.sdvImage = (SimpleDraweeView) view.findViewById(R.id.sdv_image);
        view.setTag(holder);
        return view;
    }


    @Override
    public Spannable getContentSummary(RichContentMessage data) {
        return new SpannableString("[礼物]");
    }

}