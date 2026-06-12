package com.petromirdzhunev.users.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import com.petromirdzhunev.users.controller.api.UsersApi;
import com.petromirdzhunev.users.controller.model.LoginRequestPayload;
import com.petromirdzhunev.users.controller.model.LoginResponsePayload;
import com.petromirdzhunev.users.controller.model.UserCreationRequestPayload;
import com.petromirdzhunev.users.controller.model.UserCreationResponsePayload;
import com.petromirdzhunev.users.controller.model.UserResponsePayload;
import com.petromirdzhunev.users.entity.AuthUser;
import com.petromirdzhunev.users.mapper.UserPayloadMapper;
import com.petromirdzhunev.users.repository.AuthUserRepository;
import com.petromirdzhunev.users.service.AuthenticationService;
import com.petromirdzhunev.users.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

	private final UserService userService;
	private final UserPayloadMapper userPayloadMapper;
	private final AuthenticationService authenticationService;
	private final AuthenticationManager authenticationManager;
	private final AuthUserRepository authUserRepository;

	@Override
	public LoginResponsePayload login(LoginRequestPayload loginRequest) {
		String username = loginRequest.getUsername();

		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword()));

		return new LoginResponsePayload().token(authenticationService.jwtToken(authentication));
	}

	@Override
	public UserResponsePayload authenticatedUser() {
		final AuthUser authUser = userService.authenticatedUser();
		return userPayloadMapper.fromAuthUser(authUser);
	}

	@Override
	public UserCreationResponsePayload createUser(final UserCreationRequestPayload userCreationRequestPayload) {
		final AuthUser authUser = userPayloadMapper.toAuthUser(userCreationRequestPayload);
		authUserRepository.assertAuthUserNotExists(authUser.getEmail());
		final AuthUser insertAuthUser = authUserRepository.insertAuthUser(authUser);
		return new UserCreationResponsePayload().id(insertAuthUser.getId());
	}

	@Override
	public void deleteUser(final Long authUserId) {
		authUserRepository.deleteAuthUser(authUserId);
	}


}
