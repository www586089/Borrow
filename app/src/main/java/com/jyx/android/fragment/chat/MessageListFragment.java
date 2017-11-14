package com.jyx.android.fragment.chat;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.jyx.android.R;
import com.jyx.android.base.BaseFragment;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by user on 2015/11/25.
 */
public class MessageListFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message_list;
    }



    @Override
    public void init(Bundle savedInstanceState) {
        enterFragment();

    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    /**
     * 加载 会话列表 ConversationListFragment
     */
    private void enterFragment() {

//        getActivity().getSupportFragmentManager()

        ConversationListFragment fragment = (ConversationListFragment) getChildFragmentManager().findFragmentById(R.id.conversationlist);

        Log.d(getClass().getName(), "进入会话");
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();

        fragment.setUri(uri);
    }
}
