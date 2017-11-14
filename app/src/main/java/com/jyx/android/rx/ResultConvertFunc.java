package com.jyx.android.rx;

import com.jyx.android.model.BaseEntry;
import com.jyx.android.net.BizException;

import rx.functions.Func1;

/**
 * Author : Tree
 * Date : 2016-01-07
 */
public class ResultConvertFunc<T> implements Func1<BaseEntry<T>, T> {
    @Override
    public T call(BaseEntry<T> baseEntry) {
        if(baseEntry.getResult() != 0){
            int code = baseEntry.getResult();
            String msg = baseEntry.getMsg();
            throw new BizException(code, msg);
        }
        return baseEntry.getData();
    }
}
