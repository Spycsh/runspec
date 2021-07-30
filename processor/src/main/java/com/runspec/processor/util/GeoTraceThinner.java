package com.runspec.processor.util;

import com.runspec.processor.vo.RunnerData;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class GeoTraceThinner {
    double threshold = 0.000001;
    List<RunnerData> thinnedList = new ArrayList<>(); // must initialize here, otherwise nullpoint exception

    // for real time used
    LinkedList<RunnerData> window = new LinkedList<>();

    /**
     * calculate the distance from the first point to the line connected
     * by the second point and the third point
     */
    private double pointToLineDistance(double lat1, double lon1, double lat2, double lon2, double lat3, double lon3){
        if(lat2 == lat3){
            return 9999999;
        }
        // y = kx + b
        double slope = (lon3 - lon2) / (lat3 - lat2);   // get k = (y3-y2) / (x3-x2)
        double intercept = lon2 - slope * lat2;         // get b = y2 - k * x2

        double distance = Math.abs(slope * lat1 - lon1 + intercept) / Math.sqrt(1 + Math.pow(slope,2)); // |k*x0 - y0 + b| / sqrt(k**2 + 1)
        return distance;
    }

    private List<RunnerData> diluting(List<RunnerData> runnerDatalist){
        int checkIndex = 1;
        thinnedList.add(runnerDatalist.get(0));

        while(checkIndex < runnerDatalist.size() - 1){
            double distance = pointToLineDistance(
                    Double.parseDouble(runnerDatalist.get(checkIndex).getLatitude()),
                    Double.parseDouble(runnerDatalist.get(checkIndex).getLongitude()),
                    Double.parseDouble(thinnedList.get(thinnedList.size()-1).getLatitude()),
                    Double.parseDouble(thinnedList.get(thinnedList.size()-1).getLongitude()),
                    Double.parseDouble(runnerDatalist.get(checkIndex+1).getLatitude()),
                    Double.parseDouble(runnerDatalist.get(checkIndex+1).getLongitude())
            );
            if (distance > this.threshold) {
                thinnedList.add(runnerDatalist.get(checkIndex));
            }
            checkIndex += 1;
        }

        return thinnedList;
    }

    private void dilutingRealTime(RunnerData runnerData){
        if(thinnedList.size()==0) {
            thinnedList.add(runnerData);
            return;
        }
        if(window.size() < 2){
            window.add(runnerData);
        }

        if(window.size() == 2){
            double distance = pointToLineDistance(
                    Double.parseDouble(window.get(0).getLatitude()),
                    Double.parseDouble(window.get(0).getLongitude()),
                    Double.parseDouble(thinnedList.get(thinnedList.size()-1).getLatitude()),
                    Double.parseDouble(thinnedList.get(thinnedList.size()-1).getLongitude()),
                    Double.parseDouble(window.get(1).getLatitude()),
                    Double.parseDouble(window.get(1).getLongitude())
            );
            if (distance > this.threshold) {
                thinnedList.add(window.get(0));
            }
            window.removeFirst();
        }

    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public List<RunnerData> getThinnedList() {
        return thinnedList;
    }

    public void setThinnedList(List<RunnerData> thinnedList) {
        this.thinnedList = thinnedList;
    }

    // test
    public static void main(String[] args){
        List<RunnerData> runnerDataList = new ArrayList<>();
        runnerDataList.add(new RunnerData("1", "2", "104.066228", "30.644527", new Date()));
        runnerDataList.add(new RunnerData("1", "2", "104.066279", "30.643528", new Date()));
        runnerDataList.add(new RunnerData("1", "2", "104.066296", "30.642528", new Date()));
        runnerDataList.add(new RunnerData("1", "2", "104.066296", "30.642528", new Date()));
        runnerDataList.add(new RunnerData("1", "2", "104.066296", "30.642528", new Date()));
        runnerDataList.add(new RunnerData("1", "2", "104.066314", "30.641529", new Date()));
        runnerDataList.add(new RunnerData("1", "2", "104.07", "30.641529", new Date()));

        GeoTraceThinner thinner = new GeoTraceThinner();
        List<RunnerData> thinnedList = thinner.diluting(runnerDataList);
        for(RunnerData e: thinnedList){
            System.out.println(e.getLongitude() + ":" + e.getLatitude());
        }

//        GeoTraceThinner thinner = new GeoTraceThinner();
//        for(RunnerData rd:runnerDataList){
//            thinner.dilutingRealTime(rd);
//        }
//        for(RunnerData e: thinner.getThinnedList()){
//            System.out.println(e.getLongitude() + ":" + e.getLatitude());
//        }

    }
}