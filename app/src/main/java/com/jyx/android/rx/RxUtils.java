package com.jyx.android.rx;

import com.jyx.android.model.BaseEntry;
import com.jyx.android.net.BizException;

/**
 * Author : Tree
 * Date : 2016-01-24
 */
public class RxUtils {
    public static void checkResult(BaseEntry<?> result){
        if(result.getResult() != 0){
            int code = result.getResult();
            String msg = result.getMsg();
            throw new BizException(code, msg);
        }
    }
}
