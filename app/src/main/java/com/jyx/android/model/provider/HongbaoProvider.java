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
import com.jyx.android.activity.chat.message.HongbaoMessage;
import com.jyx.android.activity.chat.redenvelope.GroupRedEnvelopeActivity;
import com.jyx.android.activity.chat.redenvelope.RedEnvelopeActivity;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imlib.RongIMClient;

/**
 * Created by Administrator on 1/25/2016.
 */

    public class HongbaoProvider extends InputProvider.ExtendProvider {

        HandlerThread mWorkThread;
        Handler mUploadHandler;

        public HongbaoProvider(RongContext context) {
            super(context);
            mWorkThread = new HandlerThread("hongbaoProvider");
            mWorkThread.start();
            mUploadHandler = new Handler(mWorkThread.getLooper());
        }


        @Override
        public Drawable obtainPluginDrawable(Context context) {
            return context.getResources().getDrawable(R.mipmap.icon_hongbao);
        }

        @Override
        public CharSequence obtainPluginTitle(Context context) {
            return context.getString(R.string.hongbao);
        }


        @Override
        public void onPluginClick(View view) {
            switch (getCurrentConversation().getConversationType()){
                case GROUP:
                    startActivityForResult(new Intent(getContext(),
                            GroupRedEnvelopeActivity.class).putExtra("targetId",
                            getCurrentConversation().getTargetId()),0);
                    break;
                case PRIVATE:
                    startActivityForResult(new Intent(getContext(),
                            RedEnvelopeActivity.class).putExtra("targetId",
                            getCurrentConversation().getTargetId()),0);
                    break;
            }

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
                int number = mIntent.getIntExtra("number",0);
                int isGroup = mIntent.getIntExtra("isGroup",0);
                String redPacketsId = mIntent.getStringExtra("redPacketsId");
                final HongbaoMessage message = new HongbaoMessage(desc,sum,
                        number,isGroup,redPacketsId);
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
