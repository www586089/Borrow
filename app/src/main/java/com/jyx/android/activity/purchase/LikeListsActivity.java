package com.jyx.android.activity.purchase;

import android.os.Bundle;
import android.widget.ListView;

import com.jyx.android.R;
import com.jyx.android.adapter.purchase.LikeListsAdapter;
import com.jyx.android.adapter.purchase.LikeListsItem;
import com.jyx.android.base.BaseActivity;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by yiyi on 2015/11/6.
 */
public class LikeListsActivity extends BaseActivity {
    @Bind(R.id.lv_likelists_list)
    ListView mLvLike;

    private ArrayList<LikeListsItem> like_lists = new ArrayList<LikeListsItem>();
    private LikeListsAdapter likelistsadapter = null;

    private void InitLikeListsData()
    {
        like_lists.clear();
        for (int i=0; i<10; i++)
        {
            LikeListsItem rf = new LikeListsItem("","测试" + i, "备注" + i, false);
            like_lists.add(rf);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_likelists;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_likelists;
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
        setActionRightText("");
        InitLikeListsData();
        likelistsadapter = new LikeListsAdapter(this);
        likelistsadapter.setRecords(like_lists);
        mLvLike.setAdapter(likelistsadapter);
    }
}
