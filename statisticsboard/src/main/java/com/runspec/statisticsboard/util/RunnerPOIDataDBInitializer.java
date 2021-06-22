package com.runspec.statisticsboard.util;

//import com.mongodb.MongoClient;
import com.mongodb.client.*;
import org.bson.Document;

public class RunnerPOIDataDBInitializer {
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    boolean existFlag;

    // get the table
    MongoCollection<Document> runnerPOIData_collection;

    // initialize the table
    public void initializeMongoDBRunnerPOIData(){
        connectToMongoDB();
    }

    // connecting to database
    public boolean connectToMongoDB(){
        System.out.println("runnerPOIData initializer: connecting to mongo db...");
        try {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            mongoDatabase = mongoClient.getDatabase("runspec-0502");
            MongoIterable<String> allCollections = mongoDatabase.listCollectionNames();

            for(String e: allCollections){
                if("runnerPOIData".equals(e)){
                    System.out.println("runnerPOIData collection already exists, " +
                            "if you want to create a new table please manually delete it!");
                    this.existFlag = true;
                }
            }

            // mongoDatabase.getCollection("POI").drop();
            if(!existFlag){
                mongoDatabase.createCollection("runnerPOIData");
                runnerPOIData_collection = mongoDatabase.getCollection("runnerPOIData");
                System.out.println("runnerPOIData initializer: Connect to database successfully");
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return existFlag;

    }
}

