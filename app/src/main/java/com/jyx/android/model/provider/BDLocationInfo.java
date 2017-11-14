package com.jyx.android.model.provider;

import java.io.Serializable;

/**
 * Created by Administrator on 2/19/2016.
 */
public class BDLocationInfo implements Serializable {
    String location;
    String description;
    double longitude ,latitude;

    public BDLocationInfo(String location, String description, double longitude, double
            latitude) {
        this.location = location;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Location :"+location+" description :"+description;
    }
}
