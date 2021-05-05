package com.runspec.producer;

//import org.apache.kafka.clients.producer.KafkaProducer;
import com.runspec.producer.vo.RunnerData;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.Producer;
import kafka.javaapi.producer.Producer;
import com.runspec.producer.util.PropertyFileReader;
import org.bson.types.ObjectId;

import java.util.*;

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
        properties.put("serializer.class", "com.runspec.producer.util.RunnerDataEncoder");


        Producer<String, RunnerData> producer = new Producer<String, RunnerData>(new ProducerConfig(properties));

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
        // TODO get data from sensor


        Random rand = new Random();
        System.out.println("Start testing!");
        while(true){
            List<RunnerData> eventList = new ArrayList<RunnerData>();
            for(int i=0; i<100; i++){   // create 100 runners
                // runnerId should be got from front end
                String runnerId = UUID.randomUUID().toString();

                // generate Mongo objectId for one trip
                String tripId = UUID.randomUUID().toString();

                for(int j=0;j<5;j++){   //for each runner, create 5 runner data
                    String latitude = "59.34" + rand.nextInt(9);
                    String longitude = "18.07" + rand.nextInt(9);
                    Date aDate = new Date();
                    RunnerData event = new RunnerData(tripId, runnerId, longitude, latitude, aDate);
                    eventList.add(event);
                }
            }
            for(RunnerData event: eventList){
                KeyedMessage<String, RunnerData> data = new KeyedMessage<String, RunnerData>(topic, event);
                producer.send(data);
                Thread.sleep(rand.nextInt(3000 - 1000) + 1000);

            }



        }


    }
}
