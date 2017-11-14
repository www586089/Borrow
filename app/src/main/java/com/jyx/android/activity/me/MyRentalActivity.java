package com.jyx.android.activity.me;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.fragment.me.MyRentalFragment;

import butterknife.Bind;

/**
 * 我的借出页面
 * Author : Tree
 * Date : 2015-11-10
 */
public class MyRentalActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.vp_my_rental)
    ViewPager mVpMyRental;
    @Bind(R.id.toolbar_left_title)
    TextView mToolbarLeftTitle;

    private String[] mTabTitles;

    private MyRentalAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_rental;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        //set up toolbar
//        mToolbar.setTitle(R.string.action_back);
        mToolbar.setTitle("");
        mToolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mToolbarLeftTitle.setOnClickListener(this);

        //set up ViewPager
        mVpMyRental.setOffscreenPageLimit(3);
        mTabTitles = getResources().getStringArray(R.array.my_rental_tab);
        mAdapter = new MyRentalAdapter(getSupportFragmentManager());
        mVpMyRental.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mVpMyRental);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_left_title:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    private class MyRentalAdapter extends FragmentPagerAdapter {

        public MyRentalAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MyRentalFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return mTabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
        }
    }

}
