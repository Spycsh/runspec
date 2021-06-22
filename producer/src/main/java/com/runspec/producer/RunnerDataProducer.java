package com.runspec.producer;

//import org.apache.kafka.clients.producer.KafkaProducer;

//import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.runspec.producer.util.RunnerDataSerializer;
import com.runspec.producer.vo.RunnerData;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.Producer;
//import kafka.javaapi.producer.Producer;
import com.runspec.producer.util.PropertyFileReader;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.bson.types.ObjectId;
import org.restlet.Component;
import org.restlet.data.Protocol;

import java.util.*;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.MessageFormat;
/**
 * read the runner data from sensor and push it to KafKa Stream
 */
public class RunnerDataProducer {
    String topic = "";
    Producer<String, RunnerData> producer;
    public Properties initializeKafkaProperties() throws Exception {
        // read the properties of kafka from property file
        Properties prop = PropertyFileReader.readPropertyFile();
        String zookeeper = prop.getProperty("com.runspec.producer.zookeeper");
        String brokerList = prop.getProperty("com.runspec.producer.brokerlist");
        topic = prop.getProperty("com.runspec.producer.topic");
        System.out.println("Using Zookeeper=" + zookeeper + " ,Broker-list=" + brokerList + " and topic " + topic);

        // set producer properties
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeper);
//        props.put("metadata.broker.list", brokerList);
//        properties.put("request.required.acks", "1");
//        props.put("serializer.class", "com.runspec.producer.util.RunnerDataEncoder");
        props.put("bootstrap.servers", "localhost:9092");
//        props.put("acks", "all");
//        props.put("retries", 0);
//        props.put("batch.size", 16384);
//        props.put("linger.ms", 1);
//        props.put("buffer.memory", 33554432);
        props.put("key.serializer", StringSerializer.class);
        props.put("value.serializer", RunnerDataSerializer.class);

        producer = new KafkaProducer<>(props);

        return props;
    }

    public static void main(String args[]) throws Exception {

        // use this part of code if you want to use REST to receive the data from edge device
        //        Component component = new ProducerRestComponent();
        //        component.getServers().add(Protocol.HTTP, 8182);
        //        component.start();

        // Here use mqtt broker
        String broker = "tcp://0.0.0.0:1883";
        String clientId = "JavaClient";

        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker:" + broker);
            mqttClient.connect(connOpts);
            System.out.println("Connected....");

            String topic = "runspec/runnerdata";
            System.out.println("Subscribe to topic: " + topic);
            mqttClient.subscribe(topic);

            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    System.out.println("message arrived from mqtt");
                    // parse the payload as json and get runner info from it
                    byte[] bArr = mqttMessage.getPayload();
                    JSONObject data = JSONObject.parseObject(new String(bArr));
                    String longitude = data.getString("longitude");
                    String latitude = data.getString("latitude");
                    String tripId = data.getString("tripId");
                    String userId = data.getString("userId");

                    //generate runnerData
                    RunnerData runnerData = new RunnerData(tripId, userId, longitude, latitude, new Date());

                    //initialize
                    RunnerDataProducer runnerDataProducer = new RunnerDataProducer();
                    runnerDataProducer.initializeKafkaProperties();
                    // push runnerdata events to Kafka
                    runnerDataProducer.sendEventToKafka(runnerData);

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
        }catch (MqttException e) {
            System.out.println("reason" + e.getReasonCode());
            System.out.println("msg" + e.getMessage());
            System.out.println("loc" + e.getLocalizedMessage());
            System.out.println("cause" + e.getCause());
            System.out.println("excep" + e);
            e.printStackTrace();
        }

    }

    // send the runnerData to Kafka consumer

    /**
     * push in Kafka channels
     *
     * @throws InterruptedException
     */
    public void sendEventToKafka(RunnerData runnerData) throws InterruptedException {
        // encode, kafka send

//        Random rand = new Random();
//        System.out.println("Start testing!");
//        while(true){
//            List<RunnerData> eventList = new ArrayList<RunnerData>();
//            for(int i=0; i<100; i++){   // create 100 runners
//                // runnerId should be got from front end
//                String runnerId = UUID.randomUUID().toString();
//
//                // generate Mongo objectId for one trip
//                String tripId = UUID.randomUUID().toString();
//
//                for(int j=0;j<5;j++){   //for each runner, create 5 runner data
//                    String latitude = "59.34" + rand.nextInt(9);
//                    String longitude = "18.07" + rand.nextInt(9);
//                    Date aDate = new Date();
//                    RunnerData event = new RunnerData(tripId, runnerId, longitude, latitude, aDate);
//                    eventList.add(event);
//                }
//            }
//            for(RunnerData event: eventList){
//                KeyedMessage<String, RunnerData> data = new KeyedMessage<String, RunnerData>(topic, event);
//                producer.send(data);
//                Thread.sleep(rand.nextInt(3000 - 1000) + 1000);
//
//            }
//                KeyedMessage<String, RunnerData> data = new KeyedMessage<String, RunnerData>(topic, runnerData);
//                producer.send(data);
                System.out.println("ready to send..."+runnerData.getLatitude()+":"+runnerData.getLongitude());
                producer.send(new ProducerRecord<String, RunnerData>(topic, runnerData));
//              System.out.println("send: ok");
    }


}
