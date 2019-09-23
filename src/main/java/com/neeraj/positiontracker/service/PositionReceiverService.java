package com.neeraj.positiontracker.service;

import com.neeraj.positiontracker.domain.VehiclePosition;
import com.neeraj.positiontracker.exception.VehicleNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author neeraj on 23/09/19
 * Copyright (c) 2019, PositionTracker.
 * All rights reserved.
 */
public interface PositionReceiverService {

    void updateVehiclePosition(VehiclePosition vehiclePosition);

    List<VehiclePosition> getLatestPositionsOfAllVehiclesUpdatedSince(Date since);

    VehiclePosition getLatestPositionFor(String vehicleName) throws VehicleNotFoundException;

}
