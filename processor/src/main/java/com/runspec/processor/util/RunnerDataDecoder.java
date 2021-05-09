//package com.runspec.processor.util;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.runspec.processor.vo.RunnerData;
//import kafka.serializer.Decoder;
//import kafka.utils.VerifiableProperties;
//
///**
// * convert JSON String to RunnerData object
// */
//public class RunnerDataDecoder implements Decoder<RunnerData> {
//
////    private static final Logger logger = Logger.getLogger(IoTDataEncoder.class);
//    private static ObjectMapper objectMapper = new ObjectMapper();
//
//    public RunnerDataDecoder(VerifiableProperties verifiableProperties) {
//
//    }
//    public RunnerData fromBytes(byte[] bytes) {
//        try {
//            return objectMapper.readValue(bytes, RunnerData.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}