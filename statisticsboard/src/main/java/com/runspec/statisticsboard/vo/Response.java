package com.runspec.statisticsboard.vo;

import com.runspec.statisticsboard.entity.POIRunnerData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Response implements Serializable {
    private List<POIRunnerData> poiRunnerData;
    private HashMap<String, Integer> poiCountMap;

    public HashMap<String, Integer> getPoiCountMap() {
        return poiCountMap;
    }

    public void setPoiCountMap(HashMap<String, Integer> poiCountMap) {
        this.poiCountMap = poiCountMap;
    }

    public List<POIRunnerData> getPoiRunnerData() {
        return poiRunnerData;
    }

    public void setPoiRunnerData(List<POIRunnerData> poiRunnerData) {
        this.poiRunnerData = poiRunnerData;
    }

}
