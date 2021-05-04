package com.runspec.statisticsboard.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.UUID;

//initialize the POI points
public class POIInitializer {
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;

    // get the table
    MongoCollection<Document> POI_collection;

    //initialize the POI points
    public void initializeMongoDBPOIDefaultData() {
        connectToMongoDB();
        initializePOI();
    }

    //connecting to database
    public void connectToMongoDB() {
        System.out.println("POI initializer: connecting to mongo db...");

        try {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            mongoDatabase = mongoClient.getDatabase("runspec-0502");

            mongoDatabase.getCollection("POI").drop();
            POI_collection = mongoDatabase.getCollection("POI");
            System.out.println("POI initializer: Connect to database successfully");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    //insert 10 POI points
    public void initializePOI() {
        POI_collection.insertOne(new Document("POIId", UUID.randomUUID().toString()).append("name", "Bergshamra subway station").append("latitude", 59.38178).append("longitude", 18.03680));
        POI_collection.insertOne(new Document("POIId", UUID.randomUUID().toString()).append("name", "KTH Entre").append("latitude", 59.34718).append("longitude", 18.07201));
        POI_collection.insertOne(new Document("POIId", UUID.randomUUID().toString()).append("name", "KTH Kista").append("latitude", 59.40498).append("longitude", 17.94948));
        POI_collection.insertOne(new Document("POIId", UUID.randomUUID().toString()).append("name", "Kungsträdgården park").append("latitude", 59.33204).append("longitude", 18.07115));
        POI_collection.insertOne(new Document("POIId", UUID.randomUUID().toString()).append("name", "IKEA Barkarby").append("latitude", 59.42127).append("longitude", 17.85908));
        POI_collection.insertOne(new Document("POIId", UUID.randomUUID().toString()).append("name", "Universitetet subway station").append("latitude", 59.36528).append("longitude", 18.05445));
        POI_collection.insertOne(new Document("POIId", UUID.randomUUID().toString()).append("name", "Vasa Museum").append("latitude", 59.32916).append("longitude", 18.09122));
        System.out.println("POI initializer: insert ok");
    }
}
