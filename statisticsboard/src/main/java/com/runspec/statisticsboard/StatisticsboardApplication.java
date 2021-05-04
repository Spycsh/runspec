package com.runspec.statisticsboard;

import com.runspec.statisticsboard.util.POIInitializer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StatisticsboardApplication {

    public static void main(String[] args) {

        SpringApplication.run(StatisticsboardApplication.class, args);

        POIInitializer ip = new POIInitializer();
        ip.initializeMongoDBPOIDefaultData();

    }

}
