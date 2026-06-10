package com.petromirdzhunev.config;

import java.io.IOException;
import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.petromirdzhunev.users.infra.http.server.controller.exception.BaseHttpExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private final BaseHttpExceptionHandler baseHttpExceptionHandler;

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
            final AuthenticationException authException) throws IOException {
	   baseHttpExceptionHandler.writeExceptionToResponse(authException, HttpStatus.UNAUTHORIZED, response);
    }
}