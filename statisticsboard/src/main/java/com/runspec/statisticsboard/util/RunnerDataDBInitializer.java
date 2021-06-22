package com.runspec.statisticsboard.util;

import com.mongodb.client.*;
import org.bson.Document;

public class RunnerDataDBInitializer {
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    boolean existFlag;

    // get the table
    MongoCollection<Document> runnerData_collection;

    // initialize the table
    public void initializeMongoDBRunnerData(){
        connectToMongoDB();
    }

    // connecting to database
    public boolean connectToMongoDB(){
        System.out.println("runnerData initializer: connecting to mongo db...");
        try {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            mongoDatabase = mongoClient.getDatabase("runspec-0502");
            MongoIterable<String> allCollections = mongoDatabase.listCollectionNames();

            for(String e: allCollections){
                if("runnerData".equals(e)){
                    System.out.println("runnerData collection already exists, " +
                            "if you want to create a new table please manually delete it!");
                    this.existFlag = true;
                }
            }

            // mongoDatabase.getCollection("POI").drop();
            if(!existFlag){
                mongoDatabase.createCollection("runnerData");
                runnerData_collection = mongoDatabase.getCollection("runnerData");
                System.out.println("runnerData initializer: Connect to database successfully");
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return existFlag;

    }
}

