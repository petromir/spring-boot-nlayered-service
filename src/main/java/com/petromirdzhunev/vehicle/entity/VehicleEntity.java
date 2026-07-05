package com.petromirdzhunev.vehicle.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class VehicleEntity {
	Long id;
	String type;
	Integer year;
	String make;
	String model;
	String plate;
	String vin;
	String nickname;
}