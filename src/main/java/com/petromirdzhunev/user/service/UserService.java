package com.petromirdzhunev.user.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.petromirdzhunev.user.repository.AuthUserRepository;
import com.petromirdzhunev.user.entity.AuthUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final AuthUserRepository authUserRepository;

	public AuthUser authenticatedUser() {
		return (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public UserDetails userDetailsById(final Long userId) {
		return this.authUserRepository.authUser(userId);
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		return authUserRepository.authUser(username);
	}

}
