package com.jyx.android.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiyi on 2015/12/23.
 */
public class PayDetailList implements ListEntity {
    private List<PayDetailInfo> itemlist = new ArrayList<PayDetailInfo>();

    public List<PayDetailInfo> getItemlist() {
        return itemlist;
    }

    public void setItemlist(List<PayDetailInfo> itemlist) {
        this.itemlist = itemlist;
    }

    @Override
    public List<?> getList() {
        return itemlist;
    }

//    public static PayDetailInfo toItem(AVObject avo)
//    {
//        PayDetailInfo model = new PayDetailInfo();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        model.setRemark(avo.getString("Remark"));
//        model.setMoney(avo.getInt("money"));
//        model.setPaytime(sdf.format(avo.getDate("payTime")));
//        model.setInOutFlag(avo.getInt("inOutFlag"));
//
//        return model;
//    }
}
