package com.runspec.processor;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.runspec.processor.entity.POIRunnerData;
import com.runspec.processor.util.GeoDistanceCalculator;
import com.runspec.processor.util.PropertyFileReader;
import com.runspec.processor.util.RunnerDataDecoder;
import com.runspec.processor.util.RunnerDataDeserializer;
import com.runspec.processor.vo.POIData;
import com.runspec.processor.vo.RunnerData;
import kafka.serializer.StringDecoder;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.StreamingContext;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.bson.Document;
import scala.Tuple2;

import org.apache.spark.api.java.function.Function;


import java.util.*;


public class RunnerDataProcessor {
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;

    // get the table
    MongoCollection<Document> runnerData_collection;
    MongoCollection<Document> runnerPOIData_collection;
    MongoCollection<Document> POIData_collection;


    public static void main(String[] args) throws Exception {
        // create Spark Config
        System.out.println("create Spark Config...");
        Properties prop = PropertyFileReader.readPropertyFile();
        SparkConf sc = new SparkConf()
                .setAppName(prop.getProperty("com.runspec.processor.spark.app.name"))
                .setMaster(prop.getProperty("com.runspec.processor.spark.master"));
//                .set("spark.mongodb.input.uri", prop.getProperty("com.runspec.processor.mongo.uri"))
//                .set("spark.mongodb.output.uri", prop.getProperty("com.runspec.processor.mongo.uri"));

        JavaSparkContext jsc = new JavaSparkContext(sc); // Create a Java Spark Context
        //batch interval of 5 seconds for incoming stream
        //see https://spark.apache.org/docs/latest/streaming-programming-guide.html#setting-the-right-batch-interval
        JavaStreamingContext jssc = new JavaStreamingContext(jsc, Durations.seconds(5));
//        jssc.checkpoint(prop.getProperty("com.runspec.processor.spark.checkpoint.dir"));

        // read the runner data from Kafka
//        Map<String, String> kafkaParams = new HashMap<>();
//        kafkaParams.put("zookeeper.connect", prop.getProperty("com.runspec.processor.kafka.zookeeper"));
//        kafkaParams.put("metadata.broker.list", prop.getProperty("com.runspec.processor.kafka.brokerlist"));
//        String topic = prop.getProperty("com.runspec.processor.kafka.topic");
//        Set<String> topicSet = new HashSet<>();
//        topicSet.add(topic);
        // create direct kafka stream
        // parameters: StreamingContext, K, V, KD, VD, Map<String, String>, Set
//        JavaPairInputDStream<String, RunnerData> directKafkaStream = KafkaUtils.createDirectStream(
//                jssc,
//                String.class,
//                RunnerData.class,
//                StringDecoder.class,
//                RunnerDataDecoder.class,
//                kafkaParams,
//                topicSet
//        );

        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", "localhost:9092");
        kafkaParams.put("zookeeper.connect", prop.getProperty("com.runspec.processor.kafka.zookeeper"));
        kafkaParams.put("key.deserializer", StringDeserializer.class);
//        kafkaParams.put("value.deserializer", RunnerDataDecoder.class);
        kafkaParams.put("value.deserializer", RunnerDataDeserializer.class);
        kafkaParams.put("group.id", "use_a_separate_group_id_for_each_stream");
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);

        Collection<String> topics = Arrays.asList(prop.getProperty("com.runspec.processor.kafka.topic"));
        JavaInputDStream<ConsumerRecord<String, RunnerData>> stream =
                KafkaUtils.createDirectStream(
                        jssc,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.Subscribe(topics, kafkaParams)
                );



        //TODO filter
        //Create JavaDStream<RunnerData>
        //https://stackoverflow.com/questions/40926947/how-to-convert-javapairinputdstream-into-dataset-dataframe-in-spark
//        JavaDStream<RunnerData> msgDataStream = directKafkaStream.map(tuple -> tuple._2());
        JavaDStream<RunnerData> msgDataStream = stream.map(record -> record.value());
        //cache stream as it is used in total and window based computation
        msgDataStream.cache();

        RunnerDataProcessor rdp = new RunnerDataProcessor();
        // try to connect to mongoDB
        try{
            rdp.mongoClient = new MongoClient("localhost", 27017);
            rdp.mongoDatabase = rdp.mongoClient.getDatabase("runspec-0502");
            rdp.runnerData_collection = rdp.mongoDatabase.getCollection("runnerData");
            rdp.runnerPOIData_collection = rdp.mongoDatabase.getCollection("runnerPOIData");
            rdp.POIData_collection = rdp.mongoDatabase.getCollection("POI");
            System.out.println("Connect to databases successfully");
        }catch (Exception e){
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }



        rdp.processRunnerData(msgDataStream);

        // home 59.382831, 18.026270
        // process poi data
        // Finished yet
        // TODO here requires a new dataloader class to initialize the default POIData and here
        //  only read the data from mongo. So here should be a list of POI to broadcast!!
        List<POIData> poiDataList = new ArrayList<>();
        FindIterable<Document> result = rdp.POIData_collection.find();
        for(Document doc: result){
            POIData poiData = new POIData();
            poiData.setPOIId(doc.get("POIId").toString());
            poiData.setLatitude((Double) doc.get("latitude"));
            poiData.setLongitude((Double) doc.get("longitude"));
            poiData.setRadius((Double) doc.get("radius")); // 500 meters

            poiDataList.add(poiData);
        }



        //  Broadcast variables allow the programmer to keep a read-only variable cached on each machine
        //  rather than shipping a copy of it with tasks
        // TODO Now just iterate, use GeoHash to improve
        for(POIData e: poiDataList){
            Broadcast<POIData> broadcastPOIValue = jssc.sparkContext().broadcast(e);
            rdp.processPOIData(msgDataStream, broadcastPOIValue);
        }
//        Broadcast<List<POIData>> broadcastPOIValues = jssc.sparkContext().
//                broadcast(poiDataList);

//        rdp.processPOIData(msgDataStream, broadcastPOIValues);

        //start context
        jssc.start();
        jssc.awaitTermination();

    }

    /**
     * save the runner data (tripId, runnerId, longitude, latitude) into mongo
     * @param msgDataStream
     */
    private void processRunnerData(JavaDStream<RunnerData> msgDataStream){
        msgDataStream.foreachRDD((JavaRDD<RunnerData> rdds) -> {
            if(!rdds.isEmpty()){
                System.out.println("Runner Data coming >>>>>>>");

//                rdds.collect().forEach(data->System.out.println(data.getLongitude()));
                rdds.collect().forEach(data -> storeInMongo(data));
            }

        });
    }

    /**
     * Method to get the vehicles which are in radius of POI and their distance from POI.
     *
     * @param msgDataStream original IoT data stream
     * @param broadcastPOIValues variable containing POI coordinates, route and vehicle types to monitor.
     */
    public void processPOIData(JavaDStream<RunnerData> msgDataStream, Broadcast<POIData> broadcastPOIValues) {
        JavaDStream<RunnerData> runnerDataStreamFiltered = msgDataStream
                .filter(rd -> GeoDistanceCalculator.isInPOIRadius(
                        Double.valueOf(rd.getLatitude()),
                        Double.valueOf(rd.getLongitude()),
                        broadcastPOIValues.value().getLatitude(),
                        broadcastPOIValues.value().getLongitude(),
                        broadcastPOIValues.value().getRadius()
                ));


        // pair with poi
        // TODO should be a list of POI Values here
        JavaPairDStream<RunnerData, POIData> poiDStreamPair = runnerDataStreamFiltered
                .mapToPair(runnerData -> new Tuple2<>(runnerData, broadcastPOIValues.value()));

        // Transform to dstream of POIRunner
        JavaDStream<POIRunnerData> poiRunnerDataJavaDStream = poiDStreamPair.map(poiRunnerDataFunc);

        // store into mongo
        poiRunnerDataJavaDStream.foreachRDD((JavaRDD<POIRunnerData> rdds) -> {
            if(!rdds.isEmpty()){
                System.out.println("Runner Data coming >>>>>>>");

//                rdds.collect().forEach(data->System.out.println(data.getLongitude()));
                rdds.collect().forEach(data -> storeInMongo(data));
            }

        });

    }

    //Function to create POITrafficData object from IoT data
    private static final Function<Tuple2<RunnerData, POIData>, POIRunnerData> poiRunnerDataFunc = (tuple -> {
        POIRunnerData poiRunnerData = new POIRunnerData();
        poiRunnerData.setTripId(tuple._1.getTripId());
        poiRunnerData.setPOIId(tuple._2.getPOIId());
        poiRunnerData.setUserId(tuple._1.getUserId());
        // get the timestamp of the runner data
        poiRunnerData.setTimestamp(tuple._1.getTimestamp());

        double distance = GeoDistanceCalculator.getDistance(
                Double.valueOf(tuple._1.getLatitude()),
                Double.valueOf(tuple._1.getLongitude()),
                tuple._2.getLatitude(),
                tuple._2.getLongitude()
        );
        System.out.println("Distance for " + tuple._1.getLatitude() + "," + tuple._1.getLongitude() + ","+
                tuple._2.getLatitude() + "," + tuple._2.getLongitude() + " = " + distance);
        poiRunnerData.setDistance(distance);
        return poiRunnerData;
    });

    private void storeInMongo(RunnerData data){


        Document document = new Document("tripId", data.getTripId()).
                append("userId",data.getUserId()).
                append("longitude", data.getLongitude()).
                append("latitude", data.getLatitude());
//        try
        System.out.println("---insert into Mongo---"+document.toString());
        runnerData_collection.insertOne(document);
    }

    // insert

    /**
     *  use tripId, userId, poiId to distinguish the situation when
     *  a runner is beside one POI in one trip
     *  first search if there is one record with the same tripId, userId, poiId, if there is, update it
     *  else insert it
     * @param data
     */
    private void storeInMongo(POIRunnerData data){
        // https://blog.csdn.net/u013174217/article/details/54576109
        BasicDBObject searchQuery = new BasicDBObject()
                .append("tripId", data.getTripId())
                .append("userId", data.getUserId())
                .append("poiId", data.getPOIId());


        Document newDocument = new Document()
                .append("tripId", data.getTripId())
                .append("userId", data.getUserId())
                .append("poiId", data.getPOIId())
                .append("distance", data.getDistance())
                .append("timestamp", data.getTimestamp());

        UpdateOptions options = new UpdateOptions().upsert(true);
        // try
        // upsert
        System.out.println("---update document---"+newDocument.toString());
        runnerPOIData_collection.replaceOne(searchQuery, newDocument, options);
    }
}