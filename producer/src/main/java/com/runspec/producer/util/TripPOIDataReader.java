
package com.runspec.producer.util;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.runspec.producer.vo.POIView;
//import com.runspec.producer.vo.POICount;
import com.runspec.producer.vo.POIData;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

//read trip data from mongo db
public class TripPOIDataReader {

    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection<Document> runnerData_collection;
    MongoCollection<Document> runnerPOIData_collection;
    MongoCollection<Document> POIData_collection;
//    MongoCollection<Document> POICountData_collection;

    //return a list of POI which the user passed by in the run trip
    public List<POIView> getTripPOIData(String userId, String tripId) {

        //get the record of current user and current trip
        BasicDBObject filter = new BasicDBObject();
        filter.put("userId", userId);
        filter.put("tripId", tripId);
        List<POIView> passedPoiViewList = new ArrayList<>();
        FindIterable<Document> runnerResult = runnerPOIData_collection.find(filter);
        for(Document doc: runnerResult){
            POIView poiView = new POIView();
            poiView.setPOIId(doc.get("poiId").toString());
            passedPoiViewList.add(poiView);
        }

        //get poi information of the required poi
        FindIterable<Document> poiResult = POIData_collection.find();
        for(Document doc: poiResult){
            String id = doc.get("POIId").toString();
            for( POIView poiView : passedPoiViewList){
                if(id.equals(poiView.getPOIId())){
                    poiView.setName(doc.get("name").toString());
                    poiView.setLatitude(doc.get("latitude").toString());
                    poiView.setLongitude(doc.get("longitude").toString());
                    poiView.setRadius(Double.parseDouble(doc.get("radius").toString()));
                }
            }
        }
        return passedPoiViewList;
    }

    //connect to database
    public void connectDatabase() {
        try {
            mongoClient = new MongoClient("localhost", 27017);
            mongoDatabase = mongoClient.getDatabase("runspec-0502");
            runnerData_collection = mongoDatabase.getCollection("runnerData");
            runnerPOIData_collection = mongoDatabase.getCollection("runnerPOIData");
//            POICountData_collection = mongoDatabase.getCollection("POICount");
            POIData_collection = mongoDatabase.getCollection("POI");
            System.out.println("Connect to databases successfully");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }



    //get five top hottest POI
    public List<POIData> getHotPoiData(){
        List<POIData> hotPoiDataList = new ArrayList<>();
        FindIterable<Document> result = POIData_collection.find().limit(5).sort(new BasicDBObject("count",-1));;
        for(Document doc: result){
            POIData poiData = new POIData();
            poiData.setPOIId(doc.get("POIId").toString());
            poiData.setLatitude(doc.get("latitude").toString());
            poiData.setLongitude(doc.get("longitude").toString());
            poiData.setRadius((Double) doc.get("radius")); // 500 meters
            poiData.setName(doc.get("name").toString());
            poiData.setCount(Integer.parseInt(doc.get("count").toString()));
            hotPoiDataList.add(poiData);
        }
        System.out.println(hotPoiDataList.toString());
        return hotPoiDataList;
    }
}
