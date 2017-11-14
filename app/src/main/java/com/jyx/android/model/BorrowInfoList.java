package com.jyx.android.model;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiyi on 2015/12/25.
 */
public class BorrowInfoList implements ListEntity  {
    private List<BorrowInfo> itemlist = new ArrayList<BorrowInfo>();

    public List<BorrowInfo> getItemlist() {
        return itemlist;
    }

    public void setItemlist(List<BorrowInfo> itemlist) {
        this.itemlist = itemlist;
    }

    @Override
    public List<?> getList() {
        return itemlist;
    }

//    public static BorrowInfo toItem(AVObject avo)
//    {
//        BorrowInfo model = new BorrowInfo();
//
//        model.setBorrowPersonAvatar(avo.getString(""));
//        model.setBorrowPersonName(avo.getString(""));
//        model.setGoodsDesc(avo.getString(""));
//        model.setGoodsPic(avo.getString(""));
//        model.setGoodsPrice(avo.getInt("money"));
//        model.setItemId(avo.getString("item_Id"));
//        model.setUser_Id(avo.getString("user_Id"));
//        model.setBuyer_Id(avo.getString("user_buyer_Id"));
//
//        return model;
//    }
}
