package com.runspec.statisticsboard.util;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.UUID;

//initialize the POI points
public class POIInitializer {
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    boolean existFlag;

    // get the table
    MongoCollection<Document> POI_collection;

    //initialize the POI points
    public void initializeMongoDBPOIDefaultData() {
        connectToMongoDB();
        if(!existFlag){
            initializePOI();
        }

    }

    //connecting to database
    public boolean connectToMongoDB() {
        System.out.println("POI initializer: connecting to mongo db...");
        try {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            mongoDatabase = mongoClient.getDatabase("runspec-0502");
            MongoIterable<String> allCollections = mongoDatabase.listCollectionNames();

            for(String e: allCollections){
                if("POI".equals(e)){
                    System.out.println("POI already exists, " +
                            "if you want to create a new table please manually delete it!");
                    this.existFlag = true;
                }
            }

            // mongoDatabase.getCollection("POI").drop();
            if(!existFlag){
                POI_collection = mongoDatabase.getCollection("POI");
                System.out.println("POI initializer: Connect to database successfully");
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return existFlag;

    }

    //insert 10 POI points
    public void initializePOI() {
        // must set double!
        POI_collection.insertOne(new Document("POIId", "1").append("name", "Bergshamra subway station").append("latitude", 59.38178).append("longitude", 18.03680).append("radius", 0.5).append("count", 0));
        // other we set it to be 0.5KM
        POI_collection.insertOne(new Document("POIId", "2").append("name", "KTH Entre").append("latitude", 59.34718).append("longitude", 18.07201).append("radius", 0.5).append("count", 0));
        POI_collection.insertOne(new Document("POIId", "3").append("name", "KTH Kista").append("latitude", 59.40498).append("longitude", 17.94948).append("radius", 0.5).append("count", 0));
        POI_collection.insertOne(new Document("POIId", "4").append("name", "Kungsträdgården park").append("latitude", 59.33204).append("longitude", 18.07115).append("radius", 0.5).append("count", 0));
        POI_collection.insertOne(new Document("POIId", "5").append("name", "IKEA Barkarby").append("latitude", 59.42127).append("longitude", 17.85908).append("radius", 0.5).append("count", 0));
        POI_collection.insertOne(new Document("POIId", "6").append("name", "Universitetet subway station").append("latitude", 59.36528).append("longitude", 18.05445).append("radius", 0.5).append("count", 0));
        POI_collection.insertOne(new Document("POIId", "7").append("name", "Vasa Museum").append("latitude", 59.32916).append("longitude", 18.09122).append("radius", 0.5).append("count", 0));
        POI_collection.insertOne(new Document("POIId", "8").append("name", "Lappis Campus").append("latitude", 59.3697868377635).append("longitude", 18.062775159109744).append("radius", 0.5).append("count", 0));
        System.out.println("POI initializer: insert ok");
    }
}
