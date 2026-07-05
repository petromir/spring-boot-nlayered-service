package com.petromirdzhunev.vehicle.mapper;

import org.springframework.stereotype.Component;

import com.petromirdzhunev.vehicle.controller.model.VehicleRequestPayload;
import com.petromirdzhunev.vehicle.controller.model.VehicleResponsePayload;
import com.petromirdzhunev.vehicle.controller.model.VehicleType;
import com.petromirdzhunev.vehicle.entity.VehicleEntity;

@Component
public class VehiclePayloadMapper {

	public VehicleEntity toVehicle(final VehicleRequestPayload vehicleRequestPayload) {
		return VehicleEntity.builder()
		           .type(vehicleRequestPayload.getType().getValue())
		           .year(vehicleRequestPayload.getYear())
		           .make(vehicleRequestPayload.getMake())
		           .model(vehicleRequestPayload.getModel())
		           .plate(vehicleRequestPayload.getPlate())
		           .vin(vehicleRequestPayload.getVin())
		           .nickname(vehicleRequestPayload.getNickname())
		           .build();
	}

	public VehicleResponsePayload fromVehicle(final VehicleEntity vehicleEntity) {
		return new VehicleResponsePayload()
				.id(vehicleEntity.getId())
				.type(vehicleEntity.getType() == null ? null
						: VehicleType.fromValue(vehicleEntity.getType()))
				.year(vehicleEntity.getYear())
				.make(vehicleEntity.getMake())
				.model(vehicleEntity.getModel())
				.plate(vehicleEntity.getPlate())
				.vin(vehicleEntity.getVin())
				.nickname(vehicleEntity.getNickname());
	}
}