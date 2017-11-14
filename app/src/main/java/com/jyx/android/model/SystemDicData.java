package com.jyx.android.model;

import java.util.List;

/**
 * Author : Tree
 * Date : 2016-01-07
 */
public class SystemDicData /*implements Parcelable*/{

    private List<SysDataItem> feature;
    private List<SysDataItem> gettype;
    private List<SysDataItem> operatype;
    private List<SysDataItem> renttype;
    private List<SysDataItem> applytype;
    private List<SysDataItem> newstype;
    private List<SysDataItem> classification;

    public List<SysDataItem> getFeature() {
        return feature;
    }

    public void setFeature(List<SysDataItem> feature) {
        this.feature = feature;
    }

    public List<SysDataItem> getGettype() {
        return gettype;
    }

    public void setGettype(List<SysDataItem> gettype) {
        this.gettype = gettype;
    }

    public List<SysDataItem> getOperatype() {
        return operatype;
    }

    public void setOperatype(List<SysDataItem> operatype) {
        this.operatype = operatype;
    }

    public List<SysDataItem> getRenttype() {
        return renttype;
    }

    public void setRenttype(List<SysDataItem> renttype) {
        this.renttype = renttype;
    }

    public List<SysDataItem> getApplytype() {
        return applytype;
    }

    public void setApplytype(List<SysDataItem> applytype) {
        this.applytype = applytype;
    }

    public List<SysDataItem> getNewstype() {
        return newstype;
    }

    public void setNewstype(List<SysDataItem> newstype) {
        this.newstype = newstype;
    }

    public List<SysDataItem> getClassification() {
        return classification;
    }

    public void setClassification(List<SysDataItem> classification) {
        this.classification = classification;
    }
    /*    public SystemDicData(Parcel in) {
        feature = in.readArrayList();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
    public static final Parcelable.Creator<SystemDicData> CREATOR = new Creator<SystemDicData>() {
        @Override
        public SystemDicData[] newArray(int size) {
            return new SystemDicData[size];
        }

        @Override
        public SystemDicData createFromParcel(Parcel in) {
            return new SystemDicData(in);
        }
    };*/
}
