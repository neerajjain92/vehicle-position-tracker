package com.neeraj.positiontracker.rest;

import com.neeraj.positiontracker.domain.VehiclePosition;
import com.neeraj.positiontracker.exception.VehicleNotFoundException;
import com.neeraj.positiontracker.service.PositionReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Date;

/**
 * @author neeraj on 23/09/19
 * Copyright (c) 2019, PositionTracker.
 * All rights reserved.
 */
@RestController
public class PositionTrackerController {

    @Autowired
    private PositionReceiverService positionReceiverService;

    @GetMapping("/vehicles/")
    public Collection<VehiclePosition> getUpdatedPositions(@RequestParam(value = "since", required = false)
                                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date since) {
        return positionReceiverService.getLatestPositionsOfAllVehiclesUpdatedSince(since);
    }

    @GetMapping("/vehicles/{vehicleName}")
    public ResponseEntity<VehiclePosition> getLatestPositionForVehicle(@PathVariable String vehicleName) {
        try {
            VehiclePosition vehiclePosition = positionReceiverService.getLatestPositionFor(vehicleName);
            return new ResponseEntity(vehiclePosition, HttpStatus.OK);
        } catch (VehicleNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public String healthCheck() {
        return "PositionTracker Controller:  I am healthy !!!!";
    }

}
