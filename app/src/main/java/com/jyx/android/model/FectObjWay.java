package com.jyx.android.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zfang on 2015/12/29.
 */
public class FectObjWay implements Serializable{

    private String objectId;
    private Date createdAt;
    private Date updatedAt;
    private String code;
    private String name;
    private String wayDes;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWayDes() {
        return wayDes;
    }

    public void setWayDes(String wayDes) {
        this.wayDes = wayDes;
    }
}
