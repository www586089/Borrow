package com.jyx.android.rx;

import com.jyx.android.R;
import com.jyx.android.base.Application;
import com.jyx.android.net.BizException;

/**
 * Author : Tree
 * Date : 2016-01-07
 */
public class CommonExceptionHandler {

    public static void handleBizException(Throwable t){
        t.printStackTrace();
        if(t instanceof BizException){
            Application.showToast(t.getMessage());
            return;
        }
        Application.showToast(R.string.network_exception);
    }

}
