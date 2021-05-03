package com.runspec.statisticsboard.dao;

import com.runspec.statisticsboard.entity.POIRunnerData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface POIRunnerDataRepository extends MongoRepository<POIRunnerData, String> {
}
