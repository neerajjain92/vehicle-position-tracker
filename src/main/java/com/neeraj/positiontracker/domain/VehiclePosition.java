package com.neeraj.positiontracker.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.function.Consumer;

/**
 * @author neeraj on 23/09/19
 * Copyright (c) 2019, PositionTracker.
 * All rights reserved.
 */
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class VehiclePosition implements Comparator<VehiclePosition> {

    private String name;
    private BigDecimal lat;
    private BigDecimal lng;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone="UTC")
    private Date timestamp;
    private BigDecimal speed;

    public VehiclePosition setName(String name) {
        this.name = name;
        return this;
    }

    public VehiclePosition setLat(BigDecimal lat) {
        this.lat = lat;
        return this;
    }

    public VehiclePosition setLng(BigDecimal lng) {
        this.lng = lng;
        return this;
    }

    public VehiclePosition setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public VehiclePosition setSpeed(BigDecimal speed) {
        this.speed = speed;
        return this;
    }

    public static VehiclePosition build(Consumer<VehiclePosition> consumer) {
        VehiclePosition vehiclePosition = new VehiclePosition();
        consumer.accept(vehiclePosition);
        return vehiclePosition;
    }

    @Override
    public int compare(VehiclePosition o1, VehiclePosition o2) {
        return o1.timestamp.compareTo(o2.timestamp);
    }
}
