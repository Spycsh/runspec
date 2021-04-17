package com.runspec.processor;

import com.runspec.processor.util.PropertyFileReader;
import com.runspec.processor.util.RunnerDataDecoder;
import com.runspec.processor.vo.RunnerData;
import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;


import java.util.*;


public class RunnerDataProcessor {
    public static void main(String[] args) throws Exception {
        // create Spark Config
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
        jssc.checkpoint(prop.getProperty("com.runspec.processor.spark.checkpoint.dir"));

        // read the runner data from Kafka
        Map<String, String> kafkaParams = new HashMap<>();
        kafkaParams.put("zookeeper.connect", prop.getProperty("com.runspec.processor.kafka.zookeeper"));
        kafkaParams.put("metadata.broker.list", prop.getProperty("com.runspec.processor.kafka.brokerlist"));
        String topic = prop.getProperty("com.runspec.kafka.topic");
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

        System.out.println("Runner Data coming >>>>>>>");

    }
}
