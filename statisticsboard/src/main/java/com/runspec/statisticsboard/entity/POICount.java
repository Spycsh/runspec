//package com.runspec.statisticsboard.entity;
//
//import org.springframework.data.mongodb.core.mapping.Document;
//
//import java.io.Serializable;
//
//@Document(collection = "POICount")
//public class POICount implements Serializable {
//    private String POIId;
//    private String name;
//    private String latitude;
//    private String longitude;
//    private double radius;
//    private int count;
//
////    public POICount(POI poi) {
////        this.POIId = poi.getPOIId();
////        this.latitude = poi.getLatitude();
////        this.longitude = poi.getLongitude();
////        this.name = poi.getName();
////        this.radius = poi.getRadius();
////    }
//
//    public String getPOIId() {
//        return POIId;
//    }
//
//    public void setPOIId(String POIId) {
//        this.POIId = POIId;
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
//    public double getRadius() {
//        return radius;
//    }
//
//    public void setRadius(double radius) {
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
