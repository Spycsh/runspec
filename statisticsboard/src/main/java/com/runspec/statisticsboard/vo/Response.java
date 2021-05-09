package com.runspec.statisticsboard.vo;

import com.runspec.statisticsboard.entity.POI;
import com.runspec.statisticsboard.entity.POIRunnerData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Response implements Serializable {
    private List<POIRunnerData> poiRunnerData;
    private List<POI> poiData;

    public List<POIRunnerData> getPoiRunnerData() {
        return poiRunnerData;
    }

    public void setPoiRunnerData(List<POIRunnerData> poiRunnerData) {
        this.poiRunnerData = poiRunnerData;
    }

    public List<POI> getPoiData() {
        return poiData;
    }

    public void setPoiData(List<POI> poiCounts) {
        this.poiData = poiCounts;
    }
}
