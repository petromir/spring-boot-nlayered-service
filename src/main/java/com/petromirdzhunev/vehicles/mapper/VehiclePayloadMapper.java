package com.petromirdzhunev.vehicles.mapper;

import org.springframework.stereotype.Component;

import com.petromirdzhunev.vehicles.controller.model.VehicleCreationRequestPayload;
import com.petromirdzhunev.vehicles.controller.model.VehicleCreationResponsePayload;
import com.petromirdzhunev.vehicles.controller.model.VehicleResponsePayload;
import com.petromirdzhunev.vehicles.controller.model.VehicleType;
import com.petromirdzhunev.vehicles.controller.model.VehicleUpdateRequestPayload;
import com.petromirdzhunev.vehicles.entity.VehicleEntity;

@Component
public class VehiclePayloadMapper {

	public VehicleEntity toVehicle(final VehicleCreationRequestPayload vehicleCreationPayload) {
		return VehicleEntity.builder()
		           .type(vehicleCreationPayload.getType().getValue())
		           .year(vehicleCreationPayload.getYear())
		           .make(vehicleCreationPayload.getMake())
		           .model(vehicleCreationPayload.getModel())
		           .plate(vehicleCreationPayload.getPlate())
		           .vin(vehicleCreationPayload.getVin())
		           .nickname(vehicleCreationPayload.getNickname())
		           .build();
	}

	public VehicleEntity toVehicle(final VehicleUpdateRequestPayload vehicleUpdatePayload) {
		return VehicleEntity.builder()
		           .type(vehicleUpdatePayload.getType().getValue())
		           .year(vehicleUpdatePayload.getYear())
		           .make(vehicleUpdatePayload.getMake())
		           .model(vehicleUpdatePayload.getModel())
		           .plate(vehicleUpdatePayload.getPlate())
		           .vin(vehicleUpdatePayload.getVin())
		           .nickname(vehicleUpdatePayload.getNickname())
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

	public VehicleCreationResponsePayload fromVehicle(final VehicleEntity vehicleEntity) {
		return new VehicleCreationResponsePayload().id(vehicleEntity.getId());
	}
}