package com.petromirdzhunev.vehicles.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.petromirdzhunev.vehicles.controller.api.VehiclesApi;
import com.petromirdzhunev.vehicles.controller.model.VehicleRequestPayload;
import com.petromirdzhunev.vehicles.controller.model.VehicleResponsePayload;
import com.petromirdzhunev.vehicles.entity.VehicleEntity;
import com.petromirdzhunev.vehicles.mapper.VehiclePayloadMapper;
import com.petromirdzhunev.vehicles.service.VehiclesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class VehiclesController implements VehiclesApi {

	private final VehiclesService vehiclesService;
	private final VehiclePayloadMapper vehiclePayloadMapper;

	@Override
	public VehicleResponsePayload createVehicle(final VehicleRequestPayload vehicleRequestPayload) {
		final VehicleEntity createdVehicle = vehiclesService.createVehicle(
				vehiclePayloadMapper.toVehicle(vehicleRequestPayload));
		return vehiclePayloadMapper.fromVehicle(createdVehicle);
	}

	@Override
	public void deleteVehicle(final Long id) {
		vehiclesService.deleteVehicle(id);
	}

	@Override
	public VehicleResponsePayload getVehicle(final Long id) {
		return vehiclePayloadMapper.fromVehicle(vehiclesService.vehicle(id));
	}

	@Override
	public List<VehicleResponsePayload> listVehicles() {
		return vehiclesService.listVehicles()
		                      .stream()
		                      .map(vehiclePayloadMapper::fromVehicle)
		                      .toList();
	}

	@Override
	public VehicleResponsePayload updateVehicle(final Long id, final VehicleRequestPayload vehicleRequestPayload) {
		final VehicleEntity updatedVehicle = vehiclesService.updateVehicle(id,
				vehiclePayloadMapper.toVehicle(vehicleRequestPayload));
		return vehiclePayloadMapper.fromVehicle(updatedVehicle);
	}
}