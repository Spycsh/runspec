package com.runspec.producer;


import com.alibaba.fastjson.JSONObject;
import com.runspec.producer.vo.RunnerData;
import org.apache.log4j.Logger;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
//import org.apache.log4j.Logger;


import java.util.Date;

//api service
public class ProducerCollectRunningDataRestService extends ServerResource {
    private static final Logger logger = Logger.getLogger(ProducerCollectRunningDataRestService.class);

    @Post("json")
    public String postRunnerData(Representation entity) throws Exception {
        String request;
        request = entity.getText();
        System.out.println(request);

        //parse json string
        JSONObject data = JSONObject.parseObject(request);
        String longitude = data.getString("longitude");
        String latitude = data.getString("latitude");
        String tripId = data.getString("tripId");
        String userId = data.getString("userId");

        //generate runnerData
        RunnerData runnerData = new RunnerData(tripId, userId, longitude, latitude, new Date());

        //initialize
        RunnerDataProducer runnerDataProducer = new RunnerDataProducer();
        runnerDataProducer.initializeKafkaProperties();
        // push runnerdata events to Kafka
        runnerDataProducer.sendEventToKafka(runnerData);
        return "ok";
    }
}
