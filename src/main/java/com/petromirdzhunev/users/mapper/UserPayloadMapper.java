package com.petromirdzhunev.users.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import com.petromirdzhunev.users.controller.model.UserCreationRequestPayload;
import com.petromirdzhunev.users.controller.model.UserResponsePayload;
import com.petromirdzhunev.users.entity.AuthUser;
import com.petromirdzhunev.users.entity.AuthUserRole;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserPayloadMapper {

	private final PasswordEncoder passwordEncoder;

	public AuthUser toAuthUser(final UserCreationRequestPayload userCreationPayload) {
		return AuthUser.builder()
		           .firstName(userCreationPayload.getFirstName())
		           .lastName(userCreationPayload.getLastName())
		           .email(userCreationPayload.getEmail())
		           .password(passwordEncoder.encode(userCreationPayload.getPassword()))
		           .roles(userCreationPayload.getRoles()
		                                     .stream()
		                                     .map(rolesEnum -> new AuthUserRole(rolesEnum.getValue()))
		                                     .toList())
		           .build();

	}

	public UserResponsePayload fromAuthUser(final AuthUser authUser) {
		return new UserResponsePayload()
				.firstName(authUser.getFirstName())
				.lastName(authUser.getLastName())
				.roles(authUser.getRoles().stream()
				               // FIXME: Return the name as well
				               .map(authUserRole -> UserResponsePayload.RolesEnum.valueOf(authUserRole.code()))
				               .toList());
	}
}
