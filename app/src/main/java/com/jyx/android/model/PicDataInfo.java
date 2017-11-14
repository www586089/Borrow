package com.jyx.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PicDataInfo implements Parcelable{
    private long mediaId = 0L;
    private String dataPath = null;
    private int isSelected = 0;

    public PicDataInfo() {}
    public PicDataInfo(long mediaId, String dataPath) {
        setMediaId(mediaId);
        setDataPath(dataPath);
    }
    public long getMediaId() {
        return mediaId;
    }

    public void setMediaId(long mediaId) {
        this.mediaId = mediaId;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public PicDataInfo(Parcel in) {
        this.mediaId = in.readLong();
        this.dataPath = in.readString();
        this.isSelected = in.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<PicDataInfo> CREATOR = new Creator<PicDataInfo>() {
        @Override
        public PicDataInfo[] newArray(int size) {
            return new PicDataInfo[size];
        }

        @Override
        public PicDataInfo createFromParcel(Parcel in) {
            return new PicDataInfo(in);
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mediaId);
        dest.writeString(this.dataPath);
        dest.writeInt(this.isSelected);
    }
}