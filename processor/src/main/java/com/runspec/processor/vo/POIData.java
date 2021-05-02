package com.runspec.processor.vo;

import java.io.Serializable;

public class POIData implements Serializable {
    private String POIId;
    private double latitude;
    private double longitude;
    private double radius;

    public String getPOIId() {
        return POIId;
    }

    public void setPOIId(String POIId) {
        this.POIId = POIId;
    }

    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getRadius() {
        return radius;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }
}
