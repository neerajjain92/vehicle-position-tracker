package com.neeraj.positiontracker.service.impl;

import com.neeraj.positiontracker.domain.VehiclePosition;
import com.neeraj.positiontracker.exception.VehicleNotFoundException;
import com.neeraj.positiontracker.repository.VehiclePositionRepository;
import com.neeraj.positiontracker.service.PositionReceiverService;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private GeodeticCalculator geoCalc = new GeodeticCalculator();
    private static final BigDecimal MeterPerSecond_TO_KPH_FACTOR = new BigDecimal("3.6");

    @Autowired
    private VehiclePositionRepository vehiclePositionRepository;

    @Override
    public void updateVehiclePosition(VehiclePosition vehiclePosition) {
        // Let's calculate the Speed of this Vehicle.
        BigDecimal speed = calculateSpeedOfThisVehicle(vehiclePosition);
        vehiclePosition.setSpeed(speed);
        POSITION_TRACKER_LOGGER.info("Updating Vehicle Position {} ", vehiclePosition);
        vehiclePositionRepository.save(vehiclePosition);
    }

    private BigDecimal calculateSpeedOfThisVehicle(VehiclePosition newPosition) {
        // Let's check if there is any existing entry for this vehicle.
        VehiclePosition posA = vehiclePositionRepository.findFirst1ByNameOrderByTimestampDesc(newPosition.getName());
        if (posA == null) return new BigDecimal("0");

        VehiclePosition posB = newPosition;

        long timeA_InMillis = posA.getTimestamp().getTime();
        long timeB_InMillis = posB.getTimestamp().getTime();
        long timeDifference = timeB_InMillis - timeA_InMillis;
        if (timeDifference == 0) return new BigDecimal("0");

        BigDecimal timeDifferenceInSeconds = new BigDecimal(timeDifference / 1000.0);

        GlobalPosition globalPositionA = new GlobalPosition(posA.getLat().doubleValue(), posA.getLng().doubleValue(), 0.0);
        GlobalPosition globalPositionB = new GlobalPosition(posB.getLat().doubleValue(), posB.getLng().doubleValue(), 0.0);

        double distanceBetweenPosition = geoCalc
                .calculateGeodeticCurve(Ellipsoid.WGS84, globalPositionA, globalPositionB)
                .getEllipsoidalDistance(); // Distance between Point A and Point B

        BigDecimal distanceInMetres = new BigDecimal("" + distanceBetweenPosition);

        BigDecimal speedInSeconds = distanceInMetres.divide(timeDifferenceInSeconds, RoundingMode.HALF_UP);
        BigDecimal kilometerPerHour = speedInSeconds.multiply(MeterPerSecond_TO_KPH_FACTOR);
        return kilometerPerHour;
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

        List<VehiclePosition> tripCoordinates = getTripCoordinatesForVehicle(vehicleName);
        if (tripCoordinates.isEmpty()) {
            throw new VehicleNotFoundException();
        }
        return tripCoordinates.get(tripCoordinates.size() - 1);
    }

    @Override
    public List<VehiclePosition> getHistoryForVehicle(String vehicleName) throws VehicleNotFoundException {
        POSITION_TRACKER_LOGGER.info("Getting History for {}", vehicleName);

        List<VehiclePosition> tripCoordinates = getTripCoordinatesForVehicle(vehicleName);
        if (tripCoordinates.isEmpty()) {
            throw new VehicleNotFoundException();
        }
        return tripCoordinates;
    }

    private List<VehiclePosition> getTripCoordinatesForVehicle(String vehicleName) {
        Example<VehiclePosition> vehiclePositionExample =
                Example.of(VehiclePosition.build(vehiclePosition -> vehiclePosition.setName(vehicleName)));

        List<VehiclePosition> positions = vehiclePositionRepository.findAll(vehiclePositionExample);
        return positions;
    }
}
