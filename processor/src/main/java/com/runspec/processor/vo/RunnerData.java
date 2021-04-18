package com.runspec.processor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * This is the class to represent the runner data
 * runner data is composed of following:
 * userId
 * longitude
 * latitude
 * altitude
 * timestamp
 * stepCount
 * distance
 * heartRate
 */
public class RunnerData implements Serializable {
    private String userId;
    private String longitude;
    private String latitude;
    private String altitude;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="MST")
    private Date timestamp;
    private String stepCount;
    private double distance;
    private int heartRate;

    // must clarify here the default constructor
    // https://www.jianshu.com/p/d9613b717059
    public RunnerData(){

    }

    public RunnerData(String userId, String longitude, String latitude, Date timestamp){
        this.userId = userId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timestamp = timestamp;
    }

    public RunnerData(String userId, String longitude, String latitude, String altitude, Date timestamp, String stepCount, double distance, int heartRate) {
        this.userId = userId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.timestamp = timestamp;
        this.stepCount = stepCount;
        this.distance = distance;
        this.heartRate = heartRate;
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getStepCount() {
        return stepCount;
    }

    public void setStepCount(String stepCount) {
        this.stepCount = stepCount;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }
}
