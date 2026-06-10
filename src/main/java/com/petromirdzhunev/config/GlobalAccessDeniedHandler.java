package com.petromirdzhunev.config;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;


import com.petromirdzhunev.users.infra.http.server.controller.exception.BaseHttpExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalAccessDeniedHandler implements AccessDeniedHandler {

	private final BaseHttpExceptionHandler baseHttpExceptionHandler;

	@Override
	public void handle(final HttpServletRequest request,
			final HttpServletResponse response,
			final AccessDeniedException accessDeniedException) throws IOException {
		baseHttpExceptionHandler.writeExceptionToResponse(accessDeniedException, HttpStatus.FORBIDDEN, response);
	}
}