package com.petromirdzhunev.vehicle.controller.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.petromirdzhunev.vehicle.controller.model.VehicleType;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * VehicleResponsePayload
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.23.0")
public class VehicleResponsePayload {

  private @Nullable Long id;

  private @Nullable VehicleType type;

  private @Nullable Integer year;

  private @Nullable String make;

  private @Nullable String model;

  private @Nullable String plate;

  private @Nullable String vin;

  private @Nullable String nickname;

  public VehicleResponsePayload id(@Nullable Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  
  @JsonProperty("id")
  public @Nullable Long getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(@Nullable Long id) {
    this.id = id;
  }

  public VehicleResponsePayload type(@Nullable VehicleType type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @Valid 
  @JsonProperty("type")
  public @Nullable VehicleType getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(@Nullable VehicleType type) {
    this.type = type;
  }

  public VehicleResponsePayload year(@Nullable Integer year) {
    this.year = year;
    return this;
  }

  /**
   * Get year
   * @return year
   */
  
  @JsonProperty("year")
  public @Nullable Integer getYear() {
    return year;
  }

  @JsonProperty("year")
  public void setYear(@Nullable Integer year) {
    this.year = year;
  }

  public VehicleResponsePayload make(@Nullable String make) {
    this.make = make;
    return this;
  }

  /**
   * Get make
   * @return make
   */
  
  @JsonProperty("make")
  public @Nullable String getMake() {
    return make;
  }

  @JsonProperty("make")
  public void setMake(@Nullable String make) {
    this.make = make;
  }

  public VehicleResponsePayload model(@Nullable String model) {
    this.model = model;
    return this;
  }

  /**
   * Get model
   * @return model
   */
  
  @JsonProperty("model")
  public @Nullable String getModel() {
    return model;
  }

  @JsonProperty("model")
  public void setModel(@Nullable String model) {
    this.model = model;
  }

  public VehicleResponsePayload plate(@Nullable String plate) {
    this.plate = plate;
    return this;
  }

  /**
   * Get plate
   * @return plate
   */
  
  @JsonProperty("plate")
  public @Nullable String getPlate() {
    return plate;
  }

  @JsonProperty("plate")
  public void setPlate(@Nullable String plate) {
    this.plate = plate;
  }

  public VehicleResponsePayload vin(@Nullable String vin) {
    this.vin = vin;
    return this;
  }

  /**
   * Get vin
   * @return vin
   */
  
  @JsonProperty("vin")
  public @Nullable String getVin() {
    return vin;
  }

  @JsonProperty("vin")
  public void setVin(@Nullable String vin) {
    this.vin = vin;
  }

  public VehicleResponsePayload nickname(@Nullable String nickname) {
    this.nickname = nickname;
    return this;
  }

  /**
   * Get nickname
   * @return nickname
   */
  
  @JsonProperty("nickname")
  public @Nullable String getNickname() {
    return nickname;
  }

  @JsonProperty("nickname")
  public void setNickname(@Nullable String nickname) {
    this.nickname = nickname;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VehicleResponsePayload vehicleResponsePayload = (VehicleResponsePayload) o;
    return Objects.equals(this.id, vehicleResponsePayload.id) &&
        Objects.equals(this.type, vehicleResponsePayload.type) &&
        Objects.equals(this.year, vehicleResponsePayload.year) &&
        Objects.equals(this.make, vehicleResponsePayload.make) &&
        Objects.equals(this.model, vehicleResponsePayload.model) &&
        Objects.equals(this.plate, vehicleResponsePayload.plate) &&
        Objects.equals(this.vin, vehicleResponsePayload.vin) &&
        Objects.equals(this.nickname, vehicleResponsePayload.nickname);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type, year, make, model, plate, vin, nickname);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VehicleResponsePayload {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("    make: ").append(toIndentedString(make)).append("\n");
    sb.append("    model: ").append(toIndentedString(model)).append("\n");
    sb.append("    plate: ").append(toIndentedString(plate)).append("\n");
    sb.append("    vin: ").append(toIndentedString(vin)).append("\n");
    sb.append("    nickname: ").append(toIndentedString(nickname)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(@Nullable Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }
}

