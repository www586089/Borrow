package com.jyx.android.model.provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import com.jyx.android.R;
import com.jyx.android.activity.chat.GiftActivity;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.message.RichContentMessage;

/**
 * Created by Administrator on 1/25/2016.
 */

public class GiftProvider extends InputProvider.ExtendProvider {

    HandlerThread mWorkThread;
    Handler mUploadHandler;

    public GiftProvider(RongContext context) {
        super(context);
        mWorkThread = new HandlerThread("gift");
        mWorkThread.start();
        mUploadHandler = new Handler(mWorkThread.getLooper());
    }


    @Override
    public Drawable obtainPluginDrawable(Context context) {
        return context.getResources().getDrawable(R.mipmap.icon_gift);
    }


    @Override
    public CharSequence obtainPluginTitle(Context context) {
        return context.getString(R.string.gift);
    }


    @Override
    public void onPluginClick(View view) {
        startActivityForResult(new Intent(getContext(), GiftActivity.class).putExtra
                ("targetUserId",getCurrentConversation().getTargetId()).putExtra("type","gift"),0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
            mUploadHandler.post(new MyRunnable(data));

        super.onActivityResult(requestCode, resultCode, data);
    }

    class MyRunnable implements Runnable {

        Intent mIntent;
        public MyRunnable(Intent intent) {
            mIntent = intent;
        }

        @Override
        public void run() {
            String desc = mIntent.getStringExtra("description");
            String imageUrl = mIntent.getStringExtra("imageUrl");
            String id = mIntent.getStringExtra("id");
            //礼物和鲜花消息， extra这个是 0 表示礼物，1表示鲜花 imageurl放图片，title放消息内容
            //content要放一下对应的gift_id 和flower_id
            final RichContentMessage message  = new RichContentMessage();
            message.setExtra("0");
            message.setImgUrl(imageUrl);
            message.setTitle(desc);
            message.setContent(id);

            if (RongIM.getInstance().getRongIMClient() != null)
                RongIM.getInstance().getRongIMClient().sendMessage
                        (getCurrentConversation().getConversationType(),
                                getCurrentConversation().getTargetId(), message, null,
                                null, new RongIMClient.SendMessageCallback() {
                                    @Override
                                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                                    }
                                    @Override
                                    public void onSuccess(Integer integer) {
                                    }
                                });
        }
    }
}
