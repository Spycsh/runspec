package com.runspec.statisticsboard.vo;

import com.runspec.statisticsboard.entity.POICount;
import com.runspec.statisticsboard.entity.POIRunnerData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Response implements Serializable {
    private List<POIRunnerData> poiRunnerData;
    private List<POICount> poiCounts;

    public List<POIRunnerData> getPoiRunnerData() {
        return poiRunnerData;
    }

    public void setPoiRunnerData(List<POIRunnerData> poiRunnerData) {
        this.poiRunnerData = poiRunnerData;
    }

    public List<POICount> getPoiCountData() {
        return poiCounts;
    }

    public void setPoiMapData(List<POICount> poiCounts) {
        this.poiCounts = poiCounts;
    }
}
