package com.jyx.android.activity.chat.redenvelope;

import android.os.Bundle;
import android.widget.ListView;

import com.jyx.android.R;
import com.jyx.android.adapter.chat.RealFlowersAdapter;
import com.jyx.android.adapter.chat.RealFlowersItem;
import com.jyx.android.base.BaseActivity;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by yiyi on 2015/11/6.
 */
public class RealFlowersActivity extends BaseActivity {
    @Bind(R.id.lv_realflowers_list)
    ListView mLvFlower;

    private ArrayList<RealFlowersItem> flowers_list = new ArrayList<RealFlowersItem>();
    private RealFlowersAdapter realflowersadapter = null;

    private void InitFlowersData()
    {
        flowers_list.clear();
        for (int i=0; i<10; i++)
        {
            RealFlowersItem rf = new RealFlowersItem("","测试" + i,"0.00");
            flowers_list.add(rf);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_realflowers;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_realflower;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        InitFlowersData();
        realflowersadapter = new RealFlowersAdapter(this);
        realflowersadapter.setRecords(flowers_list);
        mLvFlower.setAdapter(realflowersadapter);
    }
}
