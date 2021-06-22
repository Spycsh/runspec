package com.runspec.statisticsboard;

import com.runspec.statisticsboard.util.POIInitializer;

import com.runspec.statisticsboard.util.RunnerDataDBInitializer;
import com.runspec.statisticsboard.util.RunnerPOIDataDBInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StatisticsboardApplication {

    public static void main(String[] args) {

        SpringApplication.run(StatisticsboardApplication.class, args);

        POIInitializer init1 = new POIInitializer();
        RunnerDataDBInitializer init2 = new RunnerDataDBInitializer();
        RunnerPOIDataDBInitializer init3 = new RunnerPOIDataDBInitializer();
        init1.initializeMongoDBPOIDefaultData();
        init2.initializeMongoDBRunnerData();
        init3.initializeMongoDBRunnerPOIData();

    }

}
