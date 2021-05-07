
package com.runspec.producer.util;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class TripDataReader {
    String userId;
    String tripId;

    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection<Document> runnerData_collection;
    MongoCollection<Document> runnerPOIData_collection;
    MongoCollection<Document> POIData_collection;

    public TripDataReader(String userId, String tripId) {
        this.userId = userId;
        this.tripId = tripId;
    }

    public void connectDatabase() {
        try {
            mongoClient = new MongoClient("localhost", 27017);
            mongoDatabase = mongoClient.getDatabase("runspec-0502");
            runnerData_collection = mongoDatabase.getCollection("runnerData");
            runnerPOIData_collection = mongoDatabase.getCollection("runnerPOIData");
            POIData_collection = mongoDatabase.getCollection("POI");
            System.out.println("Connect to databases successfully");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void getTripData(){

    }
}
