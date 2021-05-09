package com.runspec.processor.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runspec.processor.vo.RunnerData;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class RunnerDataDeserializer implements Deserializer<RunnerData> {
    @Override public void close() {

    }

    @Override public void configure(Map<String, ?> arg0, boolean arg1) {

    }

    @Override
    public RunnerData deserialize(String arg0, byte[] arg1) {
        ObjectMapper mapper = new ObjectMapper();
        RunnerData user = null;
        try {
            user = mapper.readValue(arg1, RunnerData.class);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return user;
    }
}
