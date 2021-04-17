package com.runspec.producer;

//import org.apache.kafka.clients.producer.KafkaProducer;
import com.runspec.producer.vo.RunnerData;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import com.runspec.producer.util.PropertyFileReader;
import java.util.Properties;

/**
 * read the runner data from sensor and push it to KafKa Stream
 */
public class RunnerDataProducer {
    public static void main(String args[]) throws Exception {

        // TODO: get data from android sensor


        // read the properties of kafka from property file
        Properties prop = PropertyFileReader.readPropertyFile();
        String zookeeper = prop.getProperty("com.runspec.producer.zookeeper");
        String brokerList = prop.getProperty("com.runspec.producer.brokerlist");
        String topic = prop.getProperty("com.runspec.producer.topic");
        System.out.println("Using Zookeeper=" + zookeeper + " ,Broker-list=" + brokerList + " and topic " + topic);

        // set producer properties
        Properties properties = new Properties();
        properties.put("zookeeper.connect", zookeeper);
        properties.put("metadata.broker.list", brokerList);
        properties.put("request.required.acks", "1");
        properties.put("serializer.class", "com.runspec.producer.util.DataEncoder");


        Producer<String, RunnerData> producer = new KafkaProducer<String, RunnerData>(properties);

        RunnerDataProducer runnerDataProducer = new RunnerDataProducer();
        // push runnerdata events to Kafka
        runnerDataProducer.generateEvent(producer, topic);
    }

    // send the runnerData to Kafka consumer

    /**
     * generates the JSON Runner data, push in Kafka channels
     * @throws InterruptedException
     */
    private void generateEvent(Producer<String, RunnerData> producer, String topic) throws InterruptedException {
        // encode, kafka send
    }
}
