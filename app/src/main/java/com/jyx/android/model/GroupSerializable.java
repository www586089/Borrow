package com.jyx.android.model;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Administrator on 3/14/2016.
 */
public class GroupSerializable implements Serializable{

    String groupId;
    String groupName;
    String    portraitUri;

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }

    public GroupSerializable(String groupId, String groupName, String portraitUri) {

        this.groupId = groupId;
        this.groupName = groupName;
        this.portraitUri = portraitUri;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
