package com.runspec.statisticsboard.dao;

import com.runspec.statisticsboard.entity.POI;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface POIRepository extends MongoRepository<POI, String> {
}
