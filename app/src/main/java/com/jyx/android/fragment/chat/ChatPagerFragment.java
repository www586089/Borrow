package com.jyx.android.fragment.chat;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.adapter.chat.ConversationListAdapterEx;
import com.jyx.android.base.BaseFragment;
import com.jyx.android.fragment.discovery.RentalGroupFragment;

import butterknife.Bind;
import io.rong.imkit.RongContext;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by Tonlin on 2015/10/22.
 */
public class ChatPagerFragment extends BaseFragment {

    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.vp_message)
    ViewPager mVpMessage;

    private static final Class<?> [] mFragments = {

            ConversationListFragment.class,
            DisplayTreasureFragment.class, //晒宝
            RentalFragment.class,// 租借
            MessgeGroupMomentsFragment.class,//群动态
            RentalGroupFragment.class,// 群租借
            RelationFragment.class // 关系
    };

    private String [] mTabTitles;

    private ChatFragmentAdapter mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat_pager;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTabTitles = getResources().getStringArray(R.array.message_tab);

        mAdapter = new ChatFragmentAdapter(getChildFragmentManager());
        mVpMessage.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mVpMessage);

        for(int i = 0; i < mTabLayout.getTabCount(); i++){
            mTabLayout.getTabAt(i).setCustomView(mAdapter.getTabView(i));
        }

    }

    private class ChatFragmentAdapter extends FragmentStatePagerAdapter{

        public ChatFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = null;
            try {
                if(position == 0){
                    ConversationListFragment listFragment = ConversationListFragment.getInstance();
                    listFragment.setAdapter(new ConversationListAdapterEx
                            (RongContext.getInstance()));
                    Uri uri = Uri.parse("rong://" + getActivity()
                            .getApplicationInfo()
                            .packageName).buildUpon()
                                 .appendPath("conversationlist")
                                 .appendQueryParameter(
                                         Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                                 .appendQueryParameter(Conversation
                                         .ConversationType.GROUP.getName(),
                                         "false")//群组
                                 .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
                                 .build();
                    listFragment.setUri(uri);
                    f = listFragment;
                }else{
                    f = (Fragment) mFragments[position].newInstance();
                }
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
            View tabView = LayoutInflater.from(getActivity()).inflate(R.layout.item_chat_tab, null);
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
