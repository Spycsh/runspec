package com.runspec.producer;

//import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.util.Properties;

/**
 * read the runner data from sensor and push it to KafKa Stream
 */
public class RunnerDataProducer {
    public static void main(String args[]) throws Exception {

        Properties props = new Properties();
        // props put
        props.put("zookeeper.connect", "localhost:2181");
        props.put("metadata.broker.list", "localhost:9092");
        props.put("request.required.acks", "1");
        props.put("serializer.class", "com.iot.app.kafka.util.IoTDataEncoder");

        String topic = "runnerDataLoader";
        Producer<String, RunnerData> producer = new KafkaProducer<String, RunnerData>(props);

        RunnerDataProducer runnerDataProducer = new RunnerDataProducer();
        runnerDataProducer.generateEvent(producer, topic);
    }

    // send the runnerData to Kafka consumer
    private void generateEvent(Producer<String, RunnerData> producer, String topic) throws InterruptedException {

    }
}
