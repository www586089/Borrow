package com.jyx.android.model;

/**
 * Created by yiyi on 2016/1/14.
 */
public class RelationEntity {

    /**
     * imageurl :
     * name : 测试群
     * isgroup : 1
     * attention : 1
     * id : 135880388210885632
     * parentid : 0
     */

    private String imageurl;
    private String name;
    private String isgroup;
    private String attention;
    private String id;
    private String parentid;

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsgroup(String isgroup) {
        this.isgroup = isgroup;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getName() {
        return name;
    }

    public String getIsgroup() {
        return isgroup;
    }

    public String getAttention() {
        return attention;
    }

    public String getId() {
        return id;
    }

    public String getParentid() {
        return parentid;
    }
}
