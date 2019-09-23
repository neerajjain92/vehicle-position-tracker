package com.neeraj.positiontracker.queue;

import com.neeraj.positiontracker.domain.VehiclePosition;
import com.neeraj.positiontracker.service.PositionReceiverService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author neeraj on 23/09/19
 * Copyright (c) 2019, PositionTracker.
 * All rights reserved.
 */
@Component
@Data
public class VehiclePositionReceiver {

    private static Logger POSITION_RECEIVER_LOGGER = LoggerFactory.getLogger(VehiclePositionReceiver.class);

    @Autowired
    private PositionReceiverService positionReceiverService;

    @Value("${vehicle.position.publish.queue}")
    private String queueName;

    @JmsListener(destination = "${vehicle.position.publish.queue}")
    public void processVehiclePositionReceivedFromQueue(Map<String, String> incomingMessage) {
        Date messageReceivedTimeStamp = new Date();

        VehiclePosition vehiclePosition = VehiclePosition.build(vehicle -> {
            vehicle.setName(incomingMessage.get("vehicle"));
            vehicle.setLat(new BigDecimal(incomingMessage.get("lat")));
            vehicle.setLng(new BigDecimal(incomingMessage.get("long")));
            vehicle.setTimestamp(messageReceivedTimeStamp);
        });
//        POSITION_RECEIVER_LOGGER.info("Received Vehicle : {}", vehiclePosition);
        positionReceiverService.updateVehiclePosition(vehiclePosition);
    }
}
