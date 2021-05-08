//package com.runspec.statisticsboard.vo;
//
//import com.runspec.statisticsboard.entity.POI;
//
//import java.io.Serializable;
//
//public class POIMapData implements Serializable {
//    private String POIId;
//    private String latitude;
//    private String longitude;
//    private String name;
//    private Double radius;
//    // add the number of runners around
//    private int count;
//
//    public POIMapData(POI poi) {
//        this.POIId = poi.getPOIId();
//        this.latitude = poi.getLatitude();
//        this.longitude = poi.getLongitude();
//        this.name = poi.getName();
//        this.radius = poi.getRadius();
//    }
//
//    public String getPOIId() {
//        return POIId;
//    }
//
//    public void setPOIId(String POIId) {
//        this.POIId = POIId;
//    }
//
//    public String getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(String latitude) {
//        this.latitude = latitude;
//    }
//
//    public String getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(String longitude) {
//        this.longitude = longitude;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Double getRadius() {
//        return radius;
//    }
//
//    public void setRadius(Double radius) {
//        this.radius = radius;
//    }
//
//    public int getCount() {
//        return count;
//    }
//
//    public void setCount(int count) {
//        this.count = count;
//    }
//}
