package com.runspec.statisticsboard.vo;

import com.runspec.statisticsboard.entity.POIRunnerData;

import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {
    private List<POIRunnerData> poiRunnerData;

    public List<POIRunnerData> getPoiRunnerData() {
        return poiRunnerData;
    }

    public void setPoiRunnerData(List<POIRunnerData> poiRunnerData) {
        this.poiRunnerData = poiRunnerData;
    }
}
