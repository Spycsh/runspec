package com.runspec.statisticsboard.dao;

import com.runspec.statisticsboard.entity.POIRunnerData;
import org.springframework.data.mongodb.repository.MongoRepository;

// TODO Mongo dependency
public interface POIRunnerDataRepository extends MongoRepository<POIRunnerData, String> {
}
