package com.runspec.statisticsboard;

import com.runspec.statisticsboard.dao.POIRepository;
import com.runspec.statisticsboard.dao.POIRunnerDataRepository;
//import com.runspec.statisticsboard.dao.POICountRepository;
import com.runspec.statisticsboard.entity.POI;
//import com.runspec.statisticsboard.entity.POICount;
import com.runspec.statisticsboard.entity.POIRunnerData;
//import com.runspec.statisticsboard.util.POICountSaver;
import com.runspec.statisticsboard.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * send analyzed runner data info to dashboard
 */
@Service
public class RunnerDataService {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private POIRunnerDataRepository poiRunnerDataRepository;

    @Autowired
    private POIRepository poiRepository;


    private static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // Method sends analyzed runner data in every 5 seconds
    @Scheduled(fixedRate = 5000)
    public void trigger(){

        // get the runner data by date
//        totalRunnerDataRepository.findRunnerDataByDate(sdf.format(new Date())).forEach(e -> totalRunnerDataList.add(e));

        // prepare response
        Response response = new Response();
        // search the POI data
        List<POIRunnerData> POIRunnerDataList = new ArrayList<>(poiRunnerDataRepository.findAll());

        // data for table
        response.setPoiRunnerData(POIRunnerDataList);

        // data for map
        List<POI> POIDataList = new ArrayList<>(poiRepository.findAll());

        response.setPoiData(POIDataList);

        System.out.println("Sending POIRunner data to UI... Length: "+ POIRunnerDataList.size());
        System.out.println("Sending POICounts data to UI...Length: " + POIDataList.size());

        this.template.convertAndSend("/topic/runnerPOIData", response);
    }
}
