package com.neeraj.positiontracker.service;

import com.neeraj.positiontracker.domain.VehiclePosition;
import com.neeraj.positiontracker.exception.VehicleNotFoundException;

import java.util.Date;
import java.util.List;

/**
 * @author neeraj on 23/09/19
 * Copyright (c) 2019, PositionTracker.
 * All rights reserved.
 */
public interface PositionReceiverService {

    /**
     * Method to update {@link VehiclePosition} in the repository.
     *
     * @param vehiclePosition : Instance of {@link VehiclePosition} to be updated in MongoDB.
     */
    void updateVehiclePosition(VehiclePosition vehiclePosition);

    /**
     * Method to get the latest coordinates for all vehicles.
     *
     * @param since : Since when user is requesting all the coordinates.
     * @return : {@link List<VehiclePosition>} List of all vehicles with their respective
     * LatLng updated recently.
     */
    List<VehiclePosition> getLatestPositionsOfAllVehiclesUpdatedSince(Date since);

    /**
     * Method to get the latest co-ordinates for specific {@param vehicleName}.
     *
     * @param vehicleName : The vehicle for which the position is being fetched.
     * @return {@link VehiclePosition} instance.
     * @throws VehicleNotFoundException if vehicle does not exist.
     */
    VehiclePosition getLatestPositionFor(String vehicleName) throws VehicleNotFoundException;

    /**
     * Method to get the entire history of vehicle specified by {@link vehicleName}
     *
     * @param vehicleName
     * @return {@link List<VehiclePosition>} instance.
     * @throws VehicleNotFoundException if vehicle does not exist.
     */
    List<VehiclePosition> getHistoryForVehicle(String vehicleName) throws VehicleNotFoundException;
}
