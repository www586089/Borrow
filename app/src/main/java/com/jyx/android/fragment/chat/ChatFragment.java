package com.jyx.android.fragment.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.activity.chat.GiftDetailActivity;
import com.jyx.android.activity.chat.ImagePagerActivity;
import com.jyx.android.activity.chat.LocationActivity;
import com.jyx.android.activity.chat.TransferDetailActivity;
import com.jyx.android.activity.chat.ViewLocationActivity;
import com.jyx.android.activity.chat.message.GiftMessageItemProvider;
import com.jyx.android.activity.chat.message.HongbaoMessage;
import com.jyx.android.activity.chat.message.HongbaoMessageItemProvider;
import com.jyx.android.activity.chat.message.TransferMessage;
import com.jyx.android.activity.chat.message.TransferMessageItemProvider;
import com.jyx.android.activity.chat.redenvelope.ShowRedEnvelopeActivity;
import com.jyx.android.base.BaseFragment;
import com.jyx.android.model.provider.BDLocationInfo;
import com.jyx.android.model.provider.FlowerProvider;
import com.jyx.android.model.provider.GiftProvider;
import com.jyx.android.model.provider.HongbaoProvider;
import com.jyx.android.model.provider.SnapProvider;
import com.jyx.android.model.provider.TransferProvider;

import java.util.Locale;

import butterknife.Bind;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.LocationInputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.RichContentMessage;

/**
 * Created by Administrator on 2/17/2016.
 */
public class ChatFragment extends BaseFragment {

    private String mTargetId;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;

    SnapProvider snapProvider;

    static final String tag = "ChatFragment";

    @Bind(R.id.char_fm_ll)
    LinearLayout  linearLayout;
    @Bind(R.id.char_fm_tv_messge)
    TextView char_fm_tv_messge;
    @Bind(R.id.char_fm_tv_money)
    TextView char_fm_tv_money;
    @Bind(R.id.char_fm_img)
    ImageView char_fm_img;
    RongIM.LocationProvider.LocationCallback mLocationCallback;

    @Override public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Intent intent = getActivity().getIntent();
        mTargetId = intent.getData().getQueryParameter("targetId");
        Log.e(tag, "get mTargetId = " + mTargetId);
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
        ConversationFragment fragment = new ConversationFragment();

        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName)
                     .buildUpon()
                     .appendPath("conversation")
                     .appendPath(mConversationType.getName().toLowerCase())
                     .appendQueryParameter("targetId", mTargetId)
                     .build();

        fragment.setUri(uri);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        //xxx 为你要加载的 id
        transaction.add(R.id.chat_content, fragment);
        transaction.commit();
        initData();

    }


    @Override public void initData() {
        super.initData();
        snapProvider = new SnapProvider(RongContext.getInstance());
        //群聊
        InputProvider.ExtendProvider[] provider2 = { new ImageInputProvider(RongContext.getInstance()),//图片
                new CameraInputProvider(RongContext.getInstance()),//相机
                new LocationInputProvider(RongContext.getInstance()), new HongbaoProvider(RongContext.getInstance()),//红包
                snapProvider,//阅后即焚
        };
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.GROUP, provider2);

        RongIM.setLocationProvider(new RongIM.LocationProvider() {

            @Override public void onStartLocation(Context context, LocationCallback locationCallback) {
                //在这里打开你的地图页面,保存 locationCallback 对象。
                mLocationCallback = locationCallback;
                startActivityForResult(new Intent(context, LocationActivity.class), 20);
            }
        });
        //单聊
        InputProvider.ExtendProvider[] provider = { new ImageInputProvider(RongContext.getInstance()),//图片
                new CameraInputProvider(RongContext.getInstance()),//相机
                new LocationInputProvider(RongContext.getInstance()), new HongbaoProvider(RongContext.getInstance()),//红包
                snapProvider,//阅后即焚
                new TransferProvider(RongContext.getInstance()),//转账
                new FlowerProvider(RongContext.getInstance()),//鲜花
                new GiftProvider(RongContext.getInstance())//礼物
        };

        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);

        RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener());

        RongIM.registerMessageType(HongbaoMessage.class);
        RongIM.getInstance().registerMessageTemplate(new HongbaoMessageItemProvider());
        RongIM.registerMessageType(TransferMessage.class);
        RongIM.getInstance().registerMessageTemplate(new TransferMessageItemProvider());
        RongIM.registerMessageType(RichContentMessage.class);
        RongIM.getInstance().registerMessageTemplate(new GiftMessageItemProvider());
    }


    @Override public void initView(View view) {
        super.initView(view);
    }


    @Override protected int getLayoutId() {
        return R.layout.fragment_chat;
    }


    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        BDLocationInfo location = (BDLocationInfo) data.getSerializableExtra("location");
        if (location != null) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            Uri uri = Uri.parse("http://api.map.baidu.com/staticimage?center=" +
                    longitude +
                    "," +
                    latitude +
                    "&width=400&height=300&zoom=11&markers=" +
                    longitude +
                    "," +
                    latitude +
                    "&markerStyles=m,A");
            LocationMessage locationMessage = LocationMessage.obtain(longitude, latitude, location.getDescription(), uri);
            //如果地图地位成功，那么调用
            mLocationCallback.onSuccess(locationMessage);
        }
        else {
            mLocationCallback.onFailure("定位失败");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private class MyConversationBehaviorListener implements RongIM.ConversationBehaviorListener {

        @Override public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
            return false;
        }


        @Override public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
            return false;
        }


        @Override public boolean onMessageClick(Context context, View view, Message message) {
            if (snapProvider != null && snapProvider.ismBurnAfterRead()) {
                //按下阅后即焚
                int[] msg = new int[1];
                msg[0] = message.getMessageId();
                RongIM.getInstance().getRongIMClient().deleteMessages(msg, new RongIMClient.ResultCallback<Boolean>() {
                    @Override public void onSuccess(Boolean aBoolean) {
                        snapProvider.setmBurnAfterRead(false);
                    }


                    @Override public void onError(RongIMClient.ErrorCode errorCode) {
                    }
                });
                return true;
            }
            MessageContent content = message.getContent();
            if (content instanceof LocationMessage) {
                //打开地图
                startActivity(new Intent(context, ViewLocationActivity.class).putExtra("longitude", ((LocationMessage) content).getLat())
                                                                             .putExtra("latitude", ((LocationMessage) content).getLng()));
                return true;
            }
            else if (content instanceof HongbaoMessage) {
                HongbaoMessage hongbaoMessage = (HongbaoMessage) content;
                startActivity(new Intent(context, ShowRedEnvelopeActivity.class).putExtra("hongbao", hongbaoMessage)
                                                                                .putExtra("sendUserId", message.getSenderUserId()));
            }
            else if (content instanceof TransferMessage) {
                startActivity(new Intent(context, TransferDetailActivity.class));
            }
            else if (content instanceof RichContentMessage) {
                RichContentMessage richContentMessage = (RichContentMessage) content;
                startActivity(new Intent(context, GiftDetailActivity.class).putExtra("description", richContentMessage.getTitle())
                                                                           .putExtra("imageUrl", richContentMessage.getImgUrl()));
            }
            else if (content instanceof ImageMessage) {
                ImageMessage imageMessage = (ImageMessage) content;
                String url= String.valueOf(imageMessage.getRemoteUri());
//                startActivity(new Intent(context, ShowImageActivity.class).putExtra("url",url));
                String[] urls ={url};
                Intent intent = new Intent(context, ImagePagerActivity.class);
                // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
                context.startActivity(intent);
            }
            return false;
        }


        @Override public boolean onMessageLongClick(Context context, View view, Message message) {
            return false;
        }


        @Override public boolean onMessageLinkClick(Context context, String link) {
            return true;
        }
    }

}
