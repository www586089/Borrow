package com.jyx.android.model.provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import com.jyx.android.R;
import com.jyx.android.activity.chat.TransferActivity;
import com.jyx.android.activity.chat.message.TransferMessage;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imlib.RongIMClient;

/**
 * Created by Administrator on 1/25/2016.
 */

public class TransferProvider extends InputProvider.ExtendProvider {

    HandlerThread mWorkThread;
    Handler mUploadHandler;

    public TransferProvider(RongContext context) {
        super(context);
        mWorkThread = new HandlerThread("transfer");
        mWorkThread.start();
        mUploadHandler = new Handler(mWorkThread.getLooper());
    }

    /**
     * 设置展示的图标
     * @param context
     * @return
     */
    @Override
    public Drawable obtainPluginDrawable(Context context) {
        return context.getResources().getDrawable(R.mipmap.icon_transfer);
    }

    /**
     * 设置图标下的title
     * @param context
     * @return
     */
    @Override
    public CharSequence obtainPluginTitle(Context context) {
        return context.getString(R.string.transfer);
    }

    /**
     * click 事件
     * @param view
     */
    @Override
    public void onPluginClick(View view) {
        startActivityForResult(new Intent(getContext(), TransferActivity.class)
                .putExtra("targetId",getCurrentConversation().getTargetId()),0);
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
            double sum = mIntent.getDoubleExtra("sum",0);
            final TransferMessage message = new TransferMessage(desc,sum);
            if (RongIM.getInstance().getRongIMClient() != null)
                RongIM.getInstance().getRongIMClient().sendMessage
                        (getCurrentConversation().getConversationType(),
                                getCurrentConversation().getTargetId(), message, null,
                                null, new RongIMClient.SendMessageCallback() {
                                    @Override
                                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                                        Log.d("ExtendProvider", "onError--" + errorCode);
                                    }

                                    @Override
                                    public void onSuccess(Integer integer) {
                                        Log.d("ExtendProvider", "onSuccess--" + integer);
                                    }
                                });
        }
    }


}
