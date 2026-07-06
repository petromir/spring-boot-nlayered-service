package com.petromirdzhunev.vehicle.repository;

import static com.petromirdzhunev.user.repository.jooq.Tables.VEHICLE_DB_TABLE;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import com.petromirdzhunev.exception.EntityAlreadyExistsException;
import com.petromirdzhunev.exception.EntityNotFoundException;
import com.petromirdzhunev.vehicle.entity.VehicleEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VehicleRepository {

	private final DSLContext db;

	public VehicleEntity insertVehicle(final VehicleEntity vehicle) {
		return db.insertInto(VEHICLE_DB_TABLE)
		         .set(VEHICLE_DB_TABLE.TYPE, vehicle.getType())
		         .set(VEHICLE_DB_TABLE.YEAR, vehicle.getYear())
		         .set(VEHICLE_DB_TABLE.MAKE, vehicle.getMake())
		         .set(VEHICLE_DB_TABLE.MODEL, vehicle.getModel())
		         .set(VEHICLE_DB_TABLE.PLATE, vehicle.getPlate())
		         .set(VEHICLE_DB_TABLE.VIN, vehicle.getVin())
		         .set(VEHICLE_DB_TABLE.NICKNAME, vehicle.getNickname())
		         .returning()
		         .fetchOptional()
		         .map(this::toVehicleEntity)
		         .orElseThrow(() -> new EntityNotFoundException(
				         "Vehicle not inserted [vin=%s]".formatted(vehicle.getVin())));
	}

	public VehicleEntity vehicle(final Long vehicleId) {
		return db.select(VEHICLE_DB_TABLE.ID,
				         VEHICLE_DB_TABLE.TYPE,
				         VEHICLE_DB_TABLE.YEAR,
				         VEHICLE_DB_TABLE.MAKE,
				         VEHICLE_DB_TABLE.MODEL,
				         VEHICLE_DB_TABLE.PLATE,
				         VEHICLE_DB_TABLE.VIN,
				         VEHICLE_DB_TABLE.NICKNAME)
		         .from(VEHICLE_DB_TABLE)
		         .where(VEHICLE_DB_TABLE.ID.eq(vehicleId))
		         .fetchOptional()
		         .map(this::toVehicleEntity)
		         .orElseThrow(() -> new EntityNotFoundException(
				         "No existing vehicle found for [id=%d]".formatted(vehicleId)));
	}

	public List<VehicleEntity> vehicles() {
		return db.select(VEHICLE_DB_TABLE.ID,
			         VEHICLE_DB_TABLE.TYPE,
			         VEHICLE_DB_TABLE.YEAR,
			         VEHICLE_DB_TABLE.MAKE,
			         VEHICLE_DB_TABLE.MODEL,
			         VEHICLE_DB_TABLE.PLATE,
			         VEHICLE_DB_TABLE.VIN,
			         VEHICLE_DB_TABLE.NICKNAME)
		         .from(VEHICLE_DB_TABLE)
		         .orderBy(VEHICLE_DB_TABLE.ID)
		         .fetch()
				.map(this::toVehicleEntity);
	}

	public VehicleEntity updateVehicle(final Long vehicleId, final VehicleEntity vehicle) {
		return db.update(VEHICLE_DB_TABLE)
		         .set(VEHICLE_DB_TABLE.TYPE, vehicle.getType())
		         .set(VEHICLE_DB_TABLE.YEAR, vehicle.getYear())
		         .set(VEHICLE_DB_TABLE.MAKE, vehicle.getMake())
		         .set(VEHICLE_DB_TABLE.MODEL, vehicle.getModel())
		         .set(VEHICLE_DB_TABLE.PLATE, vehicle.getPlate())
		         .set(VEHICLE_DB_TABLE.VIN, vehicle.getVin())
		         .set(VEHICLE_DB_TABLE.NICKNAME, vehicle.getNickname())
		         .where(VEHICLE_DB_TABLE.ID.eq(vehicleId))
		         .returning()
		         .fetchOptional()
		         .map(this::toVehicleEntity)
		         .orElseThrow(() -> new EntityNotFoundException(
				         "No existing vehicle found for [id=%d]".formatted(vehicleId)));
	}

	public void deleteVehicle(final Long vehicleId) {
		final int deleted = db.deleteFrom(VEHICLE_DB_TABLE)
		                      .where(VEHICLE_DB_TABLE.ID.eq(vehicleId))
		                      .execute();

		if (deleted == 0) {
			throw new EntityNotFoundException("No existing vehicle found for [id=%d]".formatted(vehicleId));
		}
	}

	public void assertVehicleNotExists(final String plate, final String vin) {
		boolean vehicleExists = db.fetchExists(
				db.selectOne()
				  .from(VEHICLE_DB_TABLE)
				  .where(VEHICLE_DB_TABLE.PLATE.eq(plate))
				  .or(VEHICLE_DB_TABLE.VIN.eq(vin)));

		if (vehicleExists) {
			throw new EntityAlreadyExistsException(
					"Vehicle already exists for [plate=%s, vin=%s]".formatted(plate, vin));
		}
	}

	// Cannot be replaced this with Records.mapping	as select and insert/update has different mapping semantics
	private VehicleEntity toVehicleEntity(final Record record) {
		return VehicleEntity.builder()
		                    .id(record.getValue(VEHICLE_DB_TABLE.ID))
		                    .type(record.getValue(VEHICLE_DB_TABLE.TYPE))
		                    .year(record.getValue(VEHICLE_DB_TABLE.YEAR))
		                    .make(record.getValue(VEHICLE_DB_TABLE.MAKE))
		                    .model(record.getValue(VEHICLE_DB_TABLE.MODEL))
		                    .plate(record.getValue(VEHICLE_DB_TABLE.PLATE))
		                    .vin(record.getValue(VEHICLE_DB_TABLE.VIN))
		                    .nickname(record.getValue(VEHICLE_DB_TABLE.NICKNAME))
		                    .build();
	}
}
