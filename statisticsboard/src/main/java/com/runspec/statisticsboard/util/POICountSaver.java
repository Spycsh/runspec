//package com.runspec.statisticsboard.util;
//
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import com.runspec.statisticsboard.entity.POICount;
//import org.bson.Document;
//
//import java.util.List;
//import java.util.UUID;
//
//// store the POI with count in a table, override
//public class POICountSaver {
//    MongoClient mongoClient;
//    MongoDatabase mongoDatabase;
//
//    // get the table
//    MongoCollection<Document> POICount_collection;
//
//    public void storePOICountTable(List<POICount> poiCountList){
//        connectToMongoDB();
//        saveTable(poiCountList);
//    }
//
//    //connecting to database
//    public void connectToMongoDB() {
//        System.out.println("POICount Saver: connecting to mongo db...");
//
//        try {
//            mongoClient = MongoClients.create("mongodb://localhost:27017");
//            mongoDatabase = mongoClient.getDatabase("runspec-0502");
//
//            mongoDatabase.getCollection("POICount").drop();
//            POICount_collection = mongoDatabase.getCollection("POICount");
//            System.out.println("POI Saver: Connect to database successfully");
//        } catch (Exception e) {
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//        }
//    }
//
//    public void saveTable(List<POICount> list){
//        for(POICount e: list){
//            POICount_collection.insertOne(new Document("POIId", e.getPOIId()).
//                    append("name", e.getName()).
//                    append("latitude", e.getLatitude()).append("longitude", e.getLongitude())
//                    .append("radius", e.getRadius()).append("count", e.getCount()));
//        }
//    }
//
//}
//
