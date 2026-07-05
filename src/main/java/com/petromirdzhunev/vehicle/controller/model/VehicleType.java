package com.petromirdzhunev.vehicle.controller.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;


import java.util.*;
import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets VehicleType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.23.0")
public enum VehicleType {
  
  CAR("CAR"),
  
  TRUCK("TRUCK"),
  
  MOTORCYCLE("MOTORCYCLE"),
  
  SUV("SUV"),
  
  VAN("VAN"),
  
  OTHER("OTHER");

  private final String value;

  VehicleType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static VehicleType fromValue(String value) {
    for (VehicleType b : VehicleType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

