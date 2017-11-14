package com.jyx.android.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyx.android.R;
import com.jyx.android.interf.BaseFragmentInterface;
import com.jyx.android.widget.DialogControl;
import com.jyx.android.widget.WaitDialog;

import java.util.List;

import butterknife.ButterKnife;


public class BaseFragment extends Fragment implements View.OnClickListener,BaseFragmentInterface {
    protected static final int STATE_NONE = 0;//第一次，进行加载
    protected static final int STATE_REFRESH = 1;//下来刷新状态
    protected static final int STATE_LOADMORE = 2;//加载更多，状态
    protected int mState = STATE_NONE;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);

        init(savedInstanceState);
        return view;
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initData() {

    }
    @Override
    public void initView(View view) {

    }
    @LayoutRes
    protected int getLayoutId() {
        return 0;
    }

    protected void hideWaitDialog() {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            ((DialogControl) activity).hideWaitDialog();
        }
    }

    protected WaitDialog showWaitDialog() {
        return showWaitDialog(R.string.loading);
    }

    protected WaitDialog showWaitDialog(int resid) {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            return ((DialogControl) activity).showWaitDialog(resid);
        }
        return null;
    }

    @Override
    public void onClick(View v) {
    }

    public boolean onBackPressed() {
        return false;
    }
}
