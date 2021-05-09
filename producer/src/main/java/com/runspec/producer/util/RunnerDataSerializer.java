package com.runspec.producer.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runspec.producer.vo.RunnerData;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class RunnerDataSerializer implements Serializer<RunnerData> {

    @Override public void configure(Map<String, ?> map, boolean b) {

    }

    @Override public byte[] serialize(String arg0, RunnerData arg1) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(arg1).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override public void close() {

    }

}