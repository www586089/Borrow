package com.jyx.android.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.jyx.android.R;
import com.jyx.android.activity.contact.AddContactFriendActivity;
import com.jyx.android.activity.contact.EstablishGroupActivity;
import com.jyx.android.base.Application;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.fragment.chat.ChatPagerFragment;
import com.jyx.android.fragment.contact.ContactFragment;
import com.jyx.android.fragment.discovery.DiscoveryPagerFragment;
import com.jyx.android.fragment.me.MeFragment;
import com.jyx.android.widget.JYXFragmentTabHost;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Tonlin on 2015/10/22.
 * APP 程序主界面
 */
public class MainActivity extends BaseActivity implements TabHost.OnTabChangeListener
//        ,Thread.UncaughtExceptionHandler
{


    @Bind(android.R.id.tabhost)
    JYXFragmentTabHost mTabHost;

    @Bind(R.id.root)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.real_tab_content)
    FrameLayout mRealTabContent;

    @Bind(R.id.iv_camera)
    ImageView iv_camera;


    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private int mActionBarSize;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
//        Thread.setDefaultUncaughtExceptionHandler(this);

        //set up toolbar
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.action_open, R.string.action_close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mActionBarSize = getActionBarSize();

        //set up tab
        mTabHost.setNoTabChangedTag("camera");
        mTabHost.setup(this, getSupportFragmentManager(), R.id.real_tab_content);
        if (Build.VERSION.SDK_INT > 10) {
            mTabHost.getTabWidget().setShowDividers(0);
        }

        addTab("借一下", R.drawable.main_tab_discovery, DiscoveryPagerFragment.class);
        addTab("消息", R.drawable.main_tab_chat, ChatPagerFragment.class);

        addTab("camera", R.mipmap.main_tab_camera, MeFragment.class);
        addTab("通讯录", R.drawable.main_tab_contact, ContactFragment.class);// ContactFragment.class);
        addTab("我的", R.drawable.main_tab_me, MeFragment.class);

        mTabHost.setOnTabChangedListener(this);
    }

    /**
     * 增加 底部下拉菜单
     * @param name
     * @param iconRes
     * @param clz
     */
    private void addTab(String name, int iconRes, Class clz) {
        TabHost.TabSpec tab = mTabHost.newTabSpec(name);

        View indicator = LayoutInflater.from(this).inflate(
                R.layout.main_tab, null);
        ImageView icon = (ImageView) indicator.findViewById(R.id.iv_icon);
        icon.setImageResource(iconRes);
        TextView title = (TextView) indicator.findViewById(R.id.tv_name);
        title.setText(name);
//        if(name.equals("借一下"))
//        {
//            title.setText("发现");
//        }

        tab.setIndicator(indicator);
        if ("camera".equals(name)) {
            icon.setVisibility(View.INVISIBLE);
            title.setVisibility(View.INVISIBLE);
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Application.showToast("快捷键盘");
                }
            });
        }
        mTabHost.addTab(tab, clz, null);
    }

    @Override
    public void onTabChanged(String tabId) {
        mToolbarTitle.setText(tabId);

        if(tabId.equals("借一下")){
            mToolbarTitle.setText("发现");
        }
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) mRealTabContent.getLayoutParams();
        if (tabId.equals("我的")) {
            mToolbar.setBackgroundColor(Color.TRANSPARENT);
            mToolbarTitle.setText("");
            p.topMargin = 0;
        } else {
            mToolbar.setBackgroundColor(getResources().getColor(R.color.main_yellow));
            p.topMargin = mActionBarSize;
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_contact:{
                Intent intent = new Intent(this, AddContactFriendActivity.class);
                startActivity(intent);
                break;

            }
            case R.id.scan:{
                //TODO
                Intent intent=new Intent(this, MipcaActivityCapture.class);
                startActivity(intent);
                break;
            }
            case R.id.new_group:{
                Intent intent = new Intent(this, EstablishGroupActivity.class);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 发布产品
     */
    @OnClick(R.id.iv_camera)
    void clickSignIn() {
        ActivityHelper.goPublishPage(MainActivity.this);
    }
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // 判断按下的是不是返回键
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    public void uncaughtException(Thread thread, Throwable ex) {
//        Log.i("AAA", "uncaughtException   " + ex);
//    }
}


