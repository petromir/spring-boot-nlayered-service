SET search_path = public, pg_catalog;

CREATE TABLE vehicle (
    id        bigserial    PRIMARY KEY,
    type      varchar(32)  NOT NULL,
    year      integer      NOT NULL,
    make      varchar(128) NOT NULL,
    model     varchar(128) NOT NULL,
    -- Both fields are individually globally unique in the real world:
    -- - VIN — manufacturer-assigned, one per vehicle ever built
    -- - plate — one per registered vehicle in jurisdiction
    plate     varchar(64)  NOT NULL,
    vin       varchar(64)  NOT NULL,
    nickname  varchar(128) NULL,
    created_at timestamp   NULL,
    created_by bigint      NULL,
    -- In Postgres each UNIQUE constraint already creates a B-tree index,
    CONSTRAINT vehicle_plate_uqc UNIQUE (plate),
    CONSTRAINT vehicle_vin_uqc   UNIQUE (vin),
    CONSTRAINT vehicle_type_chk  CHECK (type IN ('CAR', 'TRUCK', 'MOTORCYCLE', 'SUV', 'VAN', 'OTHER'))
);

CREATE INDEX vehicle_plate_idx ON vehicle USING btree(plate);
CREATE INDEX vehicle_vin_idx   ON vehicle USING btree(vin);
CREATE INDEX vehicle_type_idx  ON vehicle USING btree(type);
