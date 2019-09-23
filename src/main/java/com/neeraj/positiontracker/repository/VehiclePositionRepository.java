package com.neeraj.positiontracker.repository;

import com.neeraj.positiontracker.domain.VehiclePosition;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author neeraj on 23/09/19
 * Copyright (c) 2019, PositionTracker.
 * All rights reserved.
 */
@Repository
public interface VehiclePositionRepository extends MongoRepository<VehiclePosition, String> {

    List<VehiclePosition> findByNameAndTimestampAfter(String name, Date timestamp);

    List<VehiclePosition> findByTimestampAfter(Date since);
}
