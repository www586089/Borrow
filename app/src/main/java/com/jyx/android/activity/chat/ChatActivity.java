package com.jyx.android.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.activity.contact.GroupActivity;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.fragment.chat.ChatFragment;
import com.jyx.android.fragment.chat.DisplayTreasureFragment;
import com.jyx.android.fragment.chat.GroupMomentsFragment;
import com.jyx.android.fragment.chat.GroupRelationFragment;
import com.jyx.android.fragment.chat.GroupRentalFragment;
import com.jyx.android.fragment.chat.RentalFragment;
import com.jyx.android.fragment.contact.GroupDetailFragment;
import com.jyx.android.fragment.contact.GroupNoticeFragment;
import com.jyx.android.widget.view.MyViewPager;

import java.util.Locale;

import butterknife.Bind;
import io.rong.imlib.model.Conversation;

/**
 * Created by gaobo on 2015/12/13.
 */
public class ChatActivity extends BaseActivity {

    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.vp_chat)
    MyViewPager mVpChat;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;
    private final String tag = "ChatActivity";


    String[] mTabTitles;
    private  Class<?> [] mFragments;
    ConversationFragmentAdapter mAdapter;
    private String mTargetId;
    private String mTitle;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionBarTitle(getActionBarTitle());
        setActionRightText("");

        mConversationType = Conversation.ConversationType.valueOf(getIntent()
                .getData()
                .getLastPathSegment().toUpperCase(Locale.getDefault()));
        mTargetId = getIntent().getData().getQueryParameter("targetId");
        mTitle = getIntent().getData().getQueryParameter("title");
        if(mConversationType.toString().equals("GROUP")){
            mTabTitles = getResources().getStringArray(R.array.chat_group_tab);
            mFragments = new Class[]{
            GroupRelationFragment.class, // 关系
            ChatFragment.class,//消息
            GroupMomentsFragment.class,//群动态
            GroupRentalFragment.class,// 群租借
            GroupDetailFragment.class,//成员
            GroupNoticeFragment.class//通知
            };
        }else{
            mTabTitles = getResources().getStringArray(R.array.chat_person_tab);
            mFragments = new Class[]{
            ChatFragment.class,//消息
            DisplayTreasureFragment.class, //动态
            RentalFragment.class// 租借
            };
        }

        mAdapter = new ConversationFragmentAdapter(getSupportFragmentManager());
        mVpChat.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mVpChat);

        for(int i = 0; i < mTabLayout.getTabCount(); i++){
            mTabLayout.getTabAt(i).setCustomView(mAdapter.getTabView(i));
        }
        if(mConversationType.toString().equals("GROUP")){
            //选中消息
            mVpChat.setCurrentItem(1);
        }


    }


    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_conversation;
    }







    @Override
    protected boolean hasBackButton() {
        return  true;
    }

    /**
     * 判断消息是否是 push 消息
     */
    private void isReconnect(Intent intent) {


//        String token = null;
//
//        if (DemoContext.getInstance() != null) {
//
//            token = DemoContext.getInstance().getSharedPreferences().getString("DEMO_TOKEN", "default");
//        }
//
//        //push或通知过来
//        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {
//
//            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
//            if (intent.getData().getQueryParameter("push") != null
//                    && intent.getData().getQueryParameter("push").equals("true")) {
//
//                reconnect(token);
//            } else {
//                //程序切到后台，收到消息后点击进入,会执行这里
//                if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {
//
//                    reconnect(token);
//                } else {
//                    enterFragment(mConversationType, mTargetId);
//                }
//            }
//        }
    }


    @Override
    protected int getActionBarTitle() {
        return R.string.title_message;
    }


    /**
     * 重连
     *
     * @param token
     */
    private void reconnect(String token) {

//        if (getApplicationInfo().packageName.equals(App.getCurProcessName(getApplicationContext()))) {
//
//            RongIM.connect(token, new RongIMClient.ConnectCallback() {
//                @Override
//                public void onTokenIncorrect() {
//
//                }
//
//                @Override
//                public void onSuccess(String s) {
//
//                    enterFragment(mConversationType, mTargetId);
//                }
//
//                @Override
//                public void onError(RongIMClient.ErrorCode errorCode) {
//
//                }
//            });
//        }
    }


    private class ConversationFragmentAdapter extends FragmentStatePagerAdapter {

        public ConversationFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = null;
            try {
                f = (Fragment) mFragments[position].newInstance();
                Bundle bundle = new Bundle();
                bundle.putString(GroupActivity.KEY_GROUP_ID,mTargetId);
                bundle.putString(GroupActivity.KEY_GROUP_NAME,mTitle);
                f.setArguments(bundle);
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return f;
        }

        @Override
        public int getCount() {
            return mTabTitles.length;
        }

        public View getTabView(int position){
            View tabView = LayoutInflater.from(ChatActivity.this).inflate(R.layout
                    .item_chat_tab,
                    null);
            TextView tv = (TextView) tabView.findViewById(R.id.tv_title);
            View indicator = tabView.findViewById(R.id.v_indicator);
            tv.setText(mTabTitles[position]);

            //FIXME delete the test data
            if(position == 1){
                indicator.setVisibility(View.VISIBLE);
            }
            return tabView;
        }
    }
}
