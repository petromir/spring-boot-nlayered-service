package com.petromirdzhunev.users.entity;

import org.apache.commons.lang3.StringUtils;

public record AuthUserRole(Long id, String code, String name) {

	public AuthUserRole(final String code) {
		this(null, code, StringUtils.capitalize(code));
	}
}
