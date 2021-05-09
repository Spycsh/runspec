//package com.runspec.producer.util;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.runspec.producer.vo.RunnerData;
//
//import kafka.serializer.Encoder;
//import kafka.utils.VerifiableProperties;
//
///**
// * convert RunnerData object to JSON String
// */
//public class RunnerDataEncoder implements Encoder<RunnerData> {
//
////    private static final Logger logger = Logger.getLogger(IoTDataEncoder.class);
//    private static ObjectMapper objectMapper = new ObjectMapper();
//    public RunnerDataEncoder(VerifiableProperties verifiableProperties) {
//
//    }
//    public byte[] toBytes(RunnerData iotEvent) {
//        try {
//            String msg = objectMapper.writeValueAsString(iotEvent);
//            // logger.info(msg);
//            System.out.println(msg);
//            return msg.getBytes();
//        } catch (JsonProcessingException e) {
//            // logger.error("Error in Serialization", e);
//            System.out.println("Error in Serialization");
//        }
//        return null;
//    }
//}