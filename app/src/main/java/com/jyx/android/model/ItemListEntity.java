package com.jyx.android.model;

/**
 * Created by Administrator on 2016/3/11.
 */
public class ItemListEntity {

    /**
     * imagejson : ["http://localhost:8084/JYX/ImageShow?image=132274026293789696.jpg"]
     * item_id : 153564652079449088
     * discribe : 描述
     * price : 0
     * name : 测试
     */

    private String imagejson;
    private String item_id;
    private String discribe;
    private String price;
    private String name;
    private String operatype_name;

    public String getOperatype_name() {
        return operatype_name;
    }

    public void setOperatype_name(String operatype_name) {
        this.operatype_name = operatype_name;
    }

    public void setImagejson(String imagejson) {
        this.imagejson = imagejson;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public void setDiscribe(String discribe) {
        this.discribe = discribe;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagejson() {
        return imagejson;
    }

    public String getItem_id() {
        return item_id;
    }

    public String getDiscribe() {
        return discribe;
    }

    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}
