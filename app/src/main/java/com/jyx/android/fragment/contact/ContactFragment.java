package com.jyx.android.fragment.contact;

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
import com.jyx.android.adapter.contact.ContactListAdapter;
import com.jyx.android.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Tonlin on 2015/10/22.
 */
public class ContactFragment extends BaseFragment {


    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    private String [] mTabTitles;

    private ContactFragmentAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contact;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTabTitles = getResources().getStringArray(R.array.contact_tab);
        mAdapter = new ContactFragmentAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        for(int i = 0; i < mTabLayout.getTabCount(); i++){
            mTabLayout.getTabAt(i).setCustomView(mAdapter.getTabView(i));
        }
    }

    private class ContactFragmentAdapter extends FragmentStatePagerAdapter {

        public ContactFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return FriendFragment.newInstance(ContactListAdapter.PAGE_FRIENDS);
            }else if(position == 1){
                return new GroupFragment();
            }else if(position == 2){
                return FriendFragment.newInstance(ContactListAdapter.PAGE_FOLLOW);
            }else if(position == 3){
                return FriendFragment.newInstance(ContactListAdapter.PAGE_FAN);
            }else {
                return new AddContactFriendFragment();
            }
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
            return tabView;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
