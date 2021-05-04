package com.runspec.statisticsboard;

import com.runspec.statisticsboard.dao.POIRunnerDataRepository;
import com.runspec.statisticsboard.entity.POIRunnerData;
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

//    @Autowired
//    private MongoTemplate mongoTemplate;

//    @Autowired
//    private TotalRunnerDataRepository totalRunnerDataRepository;

    @Autowired
    private POIRunnerDataRepository poiRunnerDataRepository;

    private static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // Method sends analyzed runner data in every 5 seconds
    @Scheduled(fixedRate = 5000)
    public void trigger(){

        // get the runner data by date
//        totalRunnerDataRepository.findRunnerDataByDate(sdf.format(new Date())).forEach(e -> totalRunnerDataList.add(e));

        // search the POI data
        List<POIRunnerData> POIRunnerDataList = new ArrayList<>(poiRunnerDataRepository.findAll());

        // prepare response
        Response response = new Response();
        response.setPoiRunnerData(POIRunnerDataList);
        
        // {poiid: count of runners}
        HashMap<String, Integer> POICountMap = new HashMap<>();
        for(POIRunnerData e: POIRunnerDataList){
            if(POICountMap.containsKey(e.getPOIId())){
                POICountMap.put(e.getPOIId(), POICountMap.get(e.getPOIId()) + 1);
            }else{
                POICountMap.put(e.getPOIId(), 0);
            }
        }
        
        response.setPoiCountMap(POICountMap);

        System.out.println("Sending POIRunner data to UI... Length: "+ POIRunnerDataList.size());
        System.out.println("Sending POICountMap data to UI...Length: "+POICountMap);

        this.template.convertAndSend("/topic/runnerPOIData", response);
    }
}
