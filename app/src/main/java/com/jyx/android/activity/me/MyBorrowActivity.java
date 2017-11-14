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
import com.jyx.android.fragment.me.MyBorrowFragment;

import butterknife.Bind;

/**
 * 我的借入
 * Created by gaobo on 2015/11/9.
 */
public class MyBorrowActivity extends BaseActivity  {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.vp_my_borrow)
    ViewPager mVpMyBorrow;
    @Bind(R.id.toolbar_left_title)
    TextView mToolbarLeftTitle;

    private String[] mTabTitles;

    private MyBorrowAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_borrow;
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
        mVpMyBorrow.setOffscreenPageLimit(3);
        mTabTitles = getResources().getStringArray(R.array.my_borrow_tab);
        mAdapter = new MyBorrowAdapter(getSupportFragmentManager());
        mVpMyBorrow.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mVpMyBorrow);
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

    private class MyBorrowAdapter extends FragmentPagerAdapter {

        public MyBorrowAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MyBorrowFragment.newInstance(position);
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
