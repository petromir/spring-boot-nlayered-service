package com.petromirdzhunev.vehicle.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.petromirdzhunev.vehicle.controller.api.VehicleApi;
import com.petromirdzhunev.vehicle.controller.model.VehicleRequestPayload;
import com.petromirdzhunev.vehicle.controller.model.VehicleResponsePayload;
import com.petromirdzhunev.vehicle.entity.VehicleEntity;
import com.petromirdzhunev.vehicle.mapper.VehiclePayloadMapper;
import com.petromirdzhunev.vehicle.service.VehicleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class VehicleController implements VehicleApi {

	private final VehicleService vehicleService;
	private final VehiclePayloadMapper vehiclePayloadMapper;

	@Override
	public VehicleResponsePayload createVehicle(final VehicleRequestPayload vehicleRequestPayload) {
		final VehicleEntity createdVehicle = vehicleService.createVehicle(
				vehiclePayloadMapper.toVehicle(vehicleRequestPayload));
		return vehiclePayloadMapper.fromVehicle(createdVehicle);
	}

	@Override
	public void deleteVehicle(final Long id) {
		vehicleService.deleteVehicle(id);
	}

	@Override
	public VehicleResponsePayload getVehicle(final Long id) {
		return vehiclePayloadMapper.fromVehicle(vehicleService.vehicle(id));
	}

	@Override
	public List<VehicleResponsePayload> listVehicles() {
		return vehicleService.listVehicles()
		                     .stream()
		                     .map(vehiclePayloadMapper::fromVehicle)
		                     .toList();
	}

	@Override
	public VehicleResponsePayload updateVehicle(final Long id, final VehicleRequestPayload vehicleRequestPayload) {
		final VehicleEntity updatedVehicle = vehicleService.updateVehicle(id,
				vehiclePayloadMapper.toVehicle(vehicleRequestPayload));
		return vehiclePayloadMapper.fromVehicle(updatedVehicle);
	}
}