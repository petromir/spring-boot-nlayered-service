package com.petromirdzhunev.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

	private final GlobalAuthenticationEntryPoint globalAuthenticationEntryPoint;
	private final GlobalAccessDeniedHandler globalAccessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
	    http.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(authorizeCustomizer -> authorizeCustomizer
					.requestMatchers("/api/users/login").permitAll()
					.requestMatchers("/health", "/metrics", "/prometheus").permitAll()
					// Static resources
					.requestMatchers("/*.html", "/*.js", "/*.css","/*.png", "/*.jpg", "/*.gif", "/*.svg",
							"/*.ico", "/assets/**").permitAll()
					.requestMatchers("/api/**").authenticated()
					.anyRequest().denyAll()) // Deny all other requests explicitly
			.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
					.accessDeniedHandler(globalAccessDeniedHandler)
					.authenticationEntryPoint(globalAuthenticationEntryPoint))
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	    return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}