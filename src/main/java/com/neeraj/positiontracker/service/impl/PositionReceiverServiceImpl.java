package com.neeraj.positiontracker.service.impl;

import com.neeraj.positiontracker.domain.VehiclePosition;
import com.neeraj.positiontracker.exception.VehicleNotFoundException;
import com.neeraj.positiontracker.repository.VehiclePositionRepository;
import com.neeraj.positiontracker.service.PositionReceiverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author neeraj on 23/09/19
 * Copyright (c) 2019, PositionTracker.
 * All rights reserved.
 */
@Service
public class PositionReceiverServiceImpl implements PositionReceiverService {

    private static final Logger POSITION_TRACKER_LOGGER = LoggerFactory.getLogger(PositionReceiverServiceImpl.class);

    @Autowired
    private VehiclePositionRepository vehiclePositionRepository;

    @Override
    public void updateVehiclePosition(VehiclePosition vehiclePosition) {
        POSITION_TRACKER_LOGGER.info("Updating Vehicle Position {} ", vehiclePosition);
        vehiclePositionRepository.save(vehiclePosition);
    }

    @Override
    public List<VehiclePosition> getLatestPositionsOfAllVehiclesUpdatedSince(Date since) {
        POSITION_TRACKER_LOGGER.info("Get Latest Positions of All vehicles updated since {} ", since);
        return since == null ? vehiclePositionRepository.findAll() :
                vehiclePositionRepository.findByTimestampAfter(since);
    }

    @Override
    public VehiclePosition getLatestPositionFor(String vehicleName) throws VehicleNotFoundException {
        POSITION_TRACKER_LOGGER.info("Get Latest Position For {}", vehicleName);
        Example<VehiclePosition> vehiclePositionExample =
                Example.of(VehiclePosition.build(vehiclePosition -> vehiclePosition.setName(vehicleName)));

        List<VehiclePosition> positions = vehiclePositionRepository.findAll(vehiclePositionExample);
        if (positions.isEmpty()) {
            throw new VehicleNotFoundException();
        }
        return positions.get(positions.size() - 1);
    }
}
