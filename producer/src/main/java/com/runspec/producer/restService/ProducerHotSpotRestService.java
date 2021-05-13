package com.runspec.producer.restService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.runspec.producer.restService.ProducerCollectRunningDataRestService;
import com.runspec.producer.util.TripPOIDataReader;
//import com.runspec.producer.vo.POICount;
import com.runspec.producer.vo.POIData;
import org.apache.log4j.Logger;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.util.List;

//get five top hottest poi
public class ProducerHotSpotRestService extends ServerResource {
    private static final Logger logger = Logger.getLogger(ProducerCollectRunningDataRestService.class);

    @Post("json")
    public String returnHotSpots() throws Exception {
//    public String returnHotSpots(Representation entity) throws Exception {
//        String request;
//        request = entity.getText();
//        System.out.println(request);

        //parse json string
//        JSONObject data = JSONObject.parseObject(request);
//        String longitude = data.getString("longitude");
//        String latitude = data.getString("latitude");

        TripPOIDataReader tdr = new TripPOIDataReader();
        tdr.connectDatabase();

        List<POIData> hotPoiDataList = tdr.getHotPoiData();

        //parse to json array
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(hotPoiDataList));

        System.out.println(jsonArray.toJSONString());

        return jsonArray.toJSONString();
    }
}
