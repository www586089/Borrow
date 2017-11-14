package com.jyx.android.fragment.discovery;

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
 * Created by Tonlin on 2015/10/22.
 */
public class DiscoveryPagerFragment  extends BaseFragment {

    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.vp_perer)
    ViewPager mVpPerer;//通用页

    private static final Class<?> [] mFragments = {
            RentalDiscoveryFragment.class,// 租借- 只是复制借一下那边的，到时需要重新写 gaobo
            DiscoveryRentalGroupFragment.class,// 群租借 只是复制借一下那边的，到时需要重新写 gaobo
            EventFragment.class,
    };

    private String [] mTabTitles;

    private PegeFragmentAdapter mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discovery_pager;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTabTitles = getResources().getStringArray(R.array.discorvery_tab);
        mAdapter = new PegeFragmentAdapter(getChildFragmentManager());
        mVpPerer.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mVpPerer);
        for(int i = 0; i < 3; i++){
            mTabLayout.getTabAt(i).setCustomView(mAdapter.getTabView(i));
        }
    }

    private class PegeFragmentAdapter extends FragmentStatePagerAdapter {

        public PegeFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = null;
            try {
                f = (Fragment) mFragments[position].newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return f;
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }

        public View getTabView(int position) {
            View tabView = LayoutInflater.from(getActivity()).inflate(R.layout.item_chat_tab, null);
            TextView tv = (TextView) tabView.findViewById(R.id.tv_title);
            View indicator = tabView.findViewById(R.id.v_indicator);
            tv.setText(mTabTitles[position]);
            if(position == 1){
                indicator.setVisibility(View.VISIBLE);
            }
            return tabView;
        }
    }
}

