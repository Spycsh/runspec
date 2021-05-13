package com.runspec.producer.restService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.runspec.producer.util.TripPOIDataReader;
import com.runspec.producer.vo.POIView;
import org.apache.log4j.Logger;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.util.List;

public class ProducerHistoryTripDataRestService extends ServerResource {
    private static final Logger logger = Logger.getLogger(ProducerHistoryTripDataRestService.class);

    @Post("json")
    public String returnHistoryTripData(Representation entity) throws Exception {
        String request;
        request = entity.getText();
        System.out.println(request);

        //parse json string
        JSONObject data = JSONObject.parseObject(request);
        String userId = data.getString("userId");


        TripPOIDataReader tdr = new TripPOIDataReader();
        tdr.connectDatabase();
        List<POIView> historyPoiViewList = tdr.getHistoryTripPOIData(userId);

        //pass pois to json string list
        System.out.println(historyPoiViewList.toString());
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(historyPoiViewList));

        return jsonArray.toJSONString();
    }
}
