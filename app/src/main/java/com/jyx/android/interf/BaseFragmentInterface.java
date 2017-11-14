package com.jyx.android.interf;

import android.os.Bundle;
import android.view.View;

/**
 * Created by user on 2015/10/28.
 */
public interface BaseFragmentInterface {

    public void initView(View view);

    public void initData();

    public void init(Bundle savedInstanceState) ;

}
