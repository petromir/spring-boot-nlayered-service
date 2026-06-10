package com.petromirdzhunev.users.controller.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UserResponsePayload
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.22.0")
public class UserResponsePayload {

  private @Nullable String firstName;

  private @Nullable String lastName;

  /**
   * Gets or Sets roles
   */
  public enum RolesEnum {
    USER("USER"),
    
    ADMIN("ADMIN");

    private final String value;

    RolesEnum(String value) {
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
    public static RolesEnum fromValue(String value) {
      for (RolesEnum b : RolesEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<RolesEnum> roles = new ArrayList<>();

  public UserResponsePayload firstName(@Nullable String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Get firstName
   * @return firstName
   */
  
  @JsonProperty("firstName")
  public @Nullable String getFirstName() {
    return firstName;
  }

  @JsonProperty("firstName")
  public void setFirstName(@Nullable String firstName) {
    this.firstName = firstName;
  }

  public UserResponsePayload lastName(@Nullable String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * Get lastName
   * @return lastName
   */
  
  @JsonProperty("lastName")
  public @Nullable String getLastName() {
    return lastName;
  }

  @JsonProperty("lastName")
  public void setLastName(@Nullable String lastName) {
    this.lastName = lastName;
  }

  public UserResponsePayload roles(List<RolesEnum> roles) {
    this.roles = roles;
    return this;
  }

  public UserResponsePayload addRolesItem(RolesEnum rolesItem) {
    if (this.roles == null) {
      this.roles = new ArrayList<>();
    }
    this.roles.add(rolesItem);
    return this;
  }

  /**
   * Get roles
   * @return roles
   */
  
  @JsonProperty("roles")
  public List<RolesEnum> getRoles() {
    return roles;
  }

  @JsonProperty("roles")
  public void setRoles(List<RolesEnum> roles) {
    this.roles = roles;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserResponsePayload userResponsePayload = (UserResponsePayload) o;
    return Objects.equals(this.firstName, userResponsePayload.firstName) &&
        Objects.equals(this.lastName, userResponsePayload.lastName) &&
        Objects.equals(this.roles, userResponsePayload.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName, roles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserResponsePayload {\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
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

