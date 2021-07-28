package com.runspec.processor.util;

import com.runspec.processor.vo.RunnerData;

import java.util.List;

public class GeoTraceThinner {
    double threshold = 0.0001;
    List<RunnerData> thinnedList;

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
            if (!(distance < this.threshold)) {
                thinnedList.add(runnerDatalist.get(checkIndex));
            }
            checkIndex += 1;
        }

        return thinnedList;
    }
}
