package com.runspec.statisticsboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class StatisticsboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatisticsboardApplication.class, args);
    }

}
