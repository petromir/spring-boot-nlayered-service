package com.petromirdzhunev.vehicles.controller.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.petromirdzhunev.vehicles.controller.model.VehicleType;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * VehicleRequestPayload
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.23.0")
public class VehicleRequestPayload {

  private VehicleType type;

  private Integer year;

  private String make;

  private String model;

  private String plate;

  private String vin;

  private @Nullable String nickname;

  public VehicleRequestPayload() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public VehicleRequestPayload(VehicleType type, Integer year, String make, String model, String plate, String vin) {
    this.type = type;
    this.year = year;
    this.make = make;
    this.model = model;
    this.plate = plate;
    this.vin = vin;
  }

  public VehicleRequestPayload type(VehicleType type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull @Valid 
  @JsonProperty("type")
  public VehicleType getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(VehicleType type) {
    this.type = type;
  }

  public VehicleRequestPayload year(Integer year) {
    this.year = year;
    return this;
  }

  /**
   * Get year
   * @return year
   */
  @NotNull 
  @JsonProperty("year")
  public Integer getYear() {
    return year;
  }

  @JsonProperty("year")
  public void setYear(Integer year) {
    this.year = year;
  }

  public VehicleRequestPayload make(String make) {
    this.make = make;
    return this;
  }

  /**
   * Get make
   * @return make
   */
  @NotNull 
  @JsonProperty("make")
  public String getMake() {
    return make;
  }

  @JsonProperty("make")
  public void setMake(String make) {
    this.make = make;
  }

  public VehicleRequestPayload model(String model) {
    this.model = model;
    return this;
  }

  /**
   * Get model
   * @return model
   */
  @NotNull 
  @JsonProperty("model")
  public String getModel() {
    return model;
  }

  @JsonProperty("model")
  public void setModel(String model) {
    this.model = model;
  }

  public VehicleRequestPayload plate(String plate) {
    this.plate = plate;
    return this;
  }

  /**
   * Get plate
   * @return plate
   */
  @NotNull 
  @JsonProperty("plate")
  public String getPlate() {
    return plate;
  }

  @JsonProperty("plate")
  public void setPlate(String plate) {
    this.plate = plate;
  }

  public VehicleRequestPayload vin(String vin) {
    this.vin = vin;
    return this;
  }

  /**
   * Get vin
   * @return vin
   */
  @NotNull 
  @JsonProperty("vin")
  public String getVin() {
    return vin;
  }

  @JsonProperty("vin")
  public void setVin(String vin) {
    this.vin = vin;
  }

  public VehicleRequestPayload nickname(@Nullable String nickname) {
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
    VehicleRequestPayload vehicleRequestPayload = (VehicleRequestPayload) o;
    return Objects.equals(this.type, vehicleRequestPayload.type) &&
        Objects.equals(this.year, vehicleRequestPayload.year) &&
        Objects.equals(this.make, vehicleRequestPayload.make) &&
        Objects.equals(this.model, vehicleRequestPayload.model) &&
        Objects.equals(this.plate, vehicleRequestPayload.plate) &&
        Objects.equals(this.vin, vehicleRequestPayload.vin) &&
        Objects.equals(this.nickname, vehicleRequestPayload.nickname);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, year, make, model, plate, vin, nickname);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VehicleRequestPayload {\n");
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

