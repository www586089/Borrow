package com.jyx.android.activity.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by ZhouYuzhen on 15/11/17.
 */
public class ConversationActivity extends BaseActivity {

    /**
     * 目标 Id
     */
    private String mTargetId;

    private String conversationId;
    private String conversationName;
    private Conversation.ConversationType mConversationType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_conversation;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Intent intent = getIntent();
        mTargetId ="5604c07d60b249ad1f10aedd";// intent.getData().getQueryParameter("targetId");
        conversationId = "AwakenDragon";
        conversationName = "东方龙启";
        mConversationType = Conversation.ConversationType.PRIVATE;

        setActionBarTitle(conversationName);

        Log.e("det", getApplicationInfo().packageName);
        Log.e("det2",(mConversationType.getName().toLowerCase()) );

//        ConversationFragment conversation = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);
//        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
//                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
//                .appendQueryParameter("targetId", mTargetId).build();
//
//        conversation.setUri(uri);
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }
}
