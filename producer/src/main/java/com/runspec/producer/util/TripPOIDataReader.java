
package com.runspec.producer.util;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.runspec.producer.vo.POI;
import com.runspec.producer.vo.POICount;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class TripPOIDataReader {

    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection<Document> runnerData_collection;
    MongoCollection<Document> runnerPOIData_collection;
    MongoCollection<Document> POIData_collection;
    MongoCollection<Document> POICountData_collection;

    public TripPOIDataReader() {

    }

    public void connectDatabase() {
        try {
            mongoClient = new MongoClient("localhost", 27017);
            mongoDatabase = mongoClient.getDatabase("runspec-0502");
            runnerData_collection = mongoDatabase.getCollection("runnerData");
            runnerPOIData_collection = mongoDatabase.getCollection("runnerPOIData");
            POICountData_collection = mongoDatabase.getCollection("POICount");

            System.out.println("Connect to databases successfully");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void getTripPOIData(){

    }

    public List<POICount> getHotPoiData(){
        List<POICount> hotPoiList = new ArrayList<>();
        FindIterable<Document> result = POICountData_collection.find().limit(5).sort(new BasicDBObject("count",-1));;
        for(Document doc: result){
            POICount poiData = new POICount();
            poiData.setPOIId(doc.get("POIId").toString());
            poiData.setLatitude(doc.get("latitude").toString());
            poiData.setLongitude(doc.get("longitude").toString());
            poiData.setRadius((Double) doc.get("radius")); // 500 meters
            poiData.setName(doc.get("name").toString());
            poiData.setCount(Integer.parseInt(doc.get("count").toString()));
            hotPoiList.add(poiData);
        }
        System.out.println(hotPoiList.toString());
        return hotPoiList;
    }
}
