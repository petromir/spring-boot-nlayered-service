package com.petromirdzhunev.user.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.petromirdzhunev.config.JwtProperties;
import com.petromirdzhunev.user.entity.AuthUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class AuthenticationService {

    private final SecretKey secretKey;
	private final JwtProperties jwtProperties;

	public AuthenticationService(final JwtProperties jwtProperties) {
		this.secretKey = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
		this.jwtProperties = jwtProperties;
	}

	// Generate JWT Token
    public String jwtToken(final Authentication authentication) {
		AuthUser authUser = (AuthUser) authentication.getPrincipal();

        Instant now = Instant.now();
        return Jwts.builder()
                   .subject(Long.toString(authUser.getId()))
                   .issuedAt(Date.from(now))
                   .expiration(Date.from(now.plus(this.jwtProperties.tokenValidity(), ChronoUnit.SECONDS))) // Token
                   // valid for 2 hours
                   .signWith(this.secretKey)
                   .compact();
    }

    public void validateJwtToken(String token) {
	    Jwts.parser()
	        .verifyWith(this.secretKey)
	        .build()
	        .parseSignedClaims(token);
    }

    public String username(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public Long userId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.parseLong(claims.getSubject());
    }
}
