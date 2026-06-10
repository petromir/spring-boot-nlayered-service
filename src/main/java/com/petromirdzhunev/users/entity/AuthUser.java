package com.petromirdzhunev.users.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthUser implements UserDetails {
	Long id;
	String email;
	String password;
	String firstName;
	String lastName;
	List<AuthUserRole> roles;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream().map(authUserRole -> new SimpleGrantedAuthority(authUserRole.code())).toList();
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}
}
