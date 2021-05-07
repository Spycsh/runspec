package com.runspec.producer;

import com.alibaba.fastjson.JSONObject;
import com.runspec.producer.util.TripDataReader;
import com.runspec.producer.vo.RunnerData;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.util.Date;

public class ProducerReturnTripDataRestService extends ServerResource {

    @Post("json")
    public String returnTripData(Representation entity) throws Exception {
        String request;
        request = entity.getText();
        System.out.println(request);

        //parse json string
        JSONObject data = JSONObject.parseObject(request);
        String tripId = data.getString("tripId");
        String userId = data.getString("userId");


        TripDataReader tdr = new TripDataReader(userId, tripId);
        tdr.connectDatabase();
        return "get trip data request";
    }
}
