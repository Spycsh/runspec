package com.runspec.statisticsboard.vo;

import com.runspec.statisticsboard.entity.POIRunnerData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Response implements Serializable {
    private List<POIRunnerData> poiRunnerData;
    private List<POIMapData> poiMapData;

    public List<POIRunnerData> getPoiRunnerData() {
        return poiRunnerData;
    }

    public void setPoiRunnerData(List<POIRunnerData> poiRunnerData) {
        this.poiRunnerData = poiRunnerData;
    }

    public List<POIMapData> getPoiMapData() {
        return poiMapData;
    }

    public void setPoiMapData(List<POIMapData> poiMapData) {
        this.poiMapData = poiMapData;
    }
}
