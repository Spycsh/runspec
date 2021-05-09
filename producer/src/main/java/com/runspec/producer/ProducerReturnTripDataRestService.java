package com.runspec.producer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.runspec.producer.util.TripPOIDataReader;
import com.runspec.producer.vo.POI;
import org.apache.log4j.Logger;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.util.List;

//get the POI which the user passed in the current trip
public class ProducerReturnTripDataRestService extends ServerResource {
    private static final Logger logger = Logger.getLogger(ProducerReturnTripDataRestService.class);

    @Post("json")
    public String returnTripData(Representation entity) throws Exception {
        String request;
        request = entity.getText();
        System.out.println(request);

        //parse json string
        JSONObject data = JSONObject.parseObject(request);
        String tripId = data.getString("tripId");
        String userId = data.getString("userId");


        TripPOIDataReader tdr = new TripPOIDataReader();
        tdr.connectDatabase();
        List<POI> passedPoiList = tdr.getTripPOIData(userId, tripId);

        //pass pois to json string list
        System.out.println(passedPoiList.toString());
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(passedPoiList));

        return jsonArray.toJSONString();
    }
}
