package com.runspec.processor;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.runspec.processor.util.PropertyFileReader;
import com.runspec.processor.util.RunnerDataDecoder;
import com.runspec.processor.vo.RunnerData;
import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction2;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.bson.Document;
import scala.Tuple2;

import org.apache.spark.api.java.function.Function;
import scala.util.parsing.json.JSONObject;


import java.util.*;


public class RunnerDataProcessor {
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;

    // get the table
    MongoCollection<Document> collection;


    public static void main(String[] args) throws Exception {
        // create Spark Config
        System.out.println("create Spark Config...");
        Properties prop = PropertyFileReader.readPropertyFile();
        SparkConf conf = new SparkConf()
                .setAppName(prop.getProperty("com.runspec.processor.spark.app.name"))
                .setMaster(prop.getProperty("com.runspec.processor.spark.master"))
                .set("spark.mongodb.input.uri", prop.getProperty("com.runspec.processor.mongo.uri"))
                .set("spark.mongodb.output.uri", prop.getProperty("com.runspec.processor.mongo.uri"));

        JavaSparkContext jsc = new JavaSparkContext(conf); // Create a Java Spark Context
        //batch interval of 5 seconds for incoming stream
        //see https://spark.apache.org/docs/latest/streaming-programming-guide.html#setting-the-right-batch-interval
        JavaStreamingContext jssc = new JavaStreamingContext(jsc, Durations.seconds(5));
//        jssc.checkpoint(prop.getProperty("com.runspec.processor.spark.checkpoint.dir"));

        // read the runner data from Kafka
        Map<String, String> kafkaParams = new HashMap<>();
        kafkaParams.put("zookeeper.connect", prop.getProperty("com.runspec.processor.kafka.zookeeper"));
        kafkaParams.put("metadata.broker.list", prop.getProperty("com.runspec.processor.kafka.brokerlist"));
        String topic = prop.getProperty("com.runspec.processor.kafka.topic");
        Set<String> topicSet = new HashSet<>();
        topicSet.add(topic);
        // create direct kafka stream
        // parameters: StreamingContext, K, V, KD, VD, Map<String, String>, Set
        JavaPairInputDStream<String, RunnerData> directKafkaStream = KafkaUtils.createDirectStream(
                jssc,
                String.class,
                RunnerData.class,
                StringDecoder.class,
                RunnerDataDecoder.class,
                kafkaParams,
                topicSet
        );



        //TODO filter
        //Create JavaDStream<RunnerData>
        //https://stackoverflow.com/questions/40926947/how-to-convert-javapairinputdstream-into-dataset-dataframe-in-spark
        JavaDStream<RunnerData> msgDataStream = directKafkaStream.map(tuple -> tuple._2());

        //cache stream as it is used in total and window based computation
        msgDataStream.cache();

        RunnerDataProcessor rdp = new RunnerDataProcessor();
        // try to connect to mongoDB
        try{
            rdp.mongoClient = new MongoClient("localhost", 27017);
            rdp.mongoDatabase = rdp.mongoClient.getDatabase("runspec-test");
            rdp.collection = rdp.mongoDatabase.getCollection("test");
            System.out.println("Connect to database successfully");
        }catch (Exception e){
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }


        // process data
        msgDataStream.foreachRDD((JavaRDD<RunnerData> rdds) -> {
            if(!rdds.isEmpty()){
                System.out.println("Runner Data coming >>>>>>>");

//                rdds.collect().forEach(data->System.out.println(data.getLongitude()));
                rdds.collect().forEach(data->rdp.storeInMongo(data));
            }

        });

        //start context
        jssc.start();
        jssc.awaitTermination();

    }

    private void storeInMongo(RunnerData data){
//        this.mongoClient

        Document document = new Document("tripId",data.getTripId()).
                append("userId",data.getUserId()).
                append("longitude", data.getLongitude()).
                append("latitude", data.getLatitude());
//        try
        collection.insertOne(document);
    }


}
