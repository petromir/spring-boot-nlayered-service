package com.petromirdzhunev.vehicle.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.petromirdzhunev.vehicle.entity.VehicleEntity;
import com.petromirdzhunev.vehicle.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {

	private final VehicleRepository vehicleRepository;

	public VehicleEntity createVehicle(final VehicleEntity vehicle) {
		return vehicleRepository.insertVehicle(vehicle);
	}

	public void deleteVehicle(final Long id) {
		vehicleRepository.deleteVehicle(id);
	}

	public VehicleEntity vehicle(final Long id) {
		return vehicleRepository.vehicle(id);
	}

	public List<VehicleEntity> listVehicles() {
		return vehicleRepository.vehicles();
	}

	public VehicleEntity updateVehicle(final Long id, final VehicleEntity vehicle) {
		return vehicleRepository.updateVehicle(id, vehicle);
	}
}
