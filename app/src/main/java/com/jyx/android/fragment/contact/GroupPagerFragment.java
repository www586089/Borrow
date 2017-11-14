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
import com.jyx.android.base.BaseFragment;

import butterknife.Bind;

/**
 * Author : Tree
 * Date : 2016-01-26
 */
public class GroupPagerFragment extends BaseFragment {

    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.vp_message)
    ViewPager mViewPager;

    private String [] mTabTitles;
    private static final Class<?> [] mFragments = {
            GroupDetailFragment.class,
            GroupDetailFragment.class,
            GroupDetailFragment.class,
            GroupDetailFragment.class,
            GroupDetailFragment.class,
            GroupDetailFragment.class
    };

    private GroupAdapter mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat_pager;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTabTitles = getResources().getStringArray(R.array.group_tab);
        mAdapter = new GroupAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        for(int i = 0; i < mTabLayout.getTabCount(); i++){
            mTabLayout.getTabAt(i).setCustomView(mAdapter.getTabView(i));
        }
    }

    private class GroupAdapter extends FragmentStatePagerAdapter{

        public GroupAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            Fragment f = null;
            try {
                f = (Fragment) mFragments[position].newInstance();
                f.setArguments(getArguments());
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
            return tabView;
        }
    }

}
