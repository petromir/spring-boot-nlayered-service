package com.petromirdzhunev.config;

import java.io.IOException;
import java.util.regex.Pattern;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.petromirdzhunev.user.service.AuthenticationService;
import com.petromirdzhunev.user.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String BEARER_TOKEN_PREFIX = "Bearer ";
	// Process paths under /api except /api/users/login
	private static final Pattern API_ENDPOINTS_PATTERN = Pattern.compile("^/api(?!/users/login$).*$");
	private final AuthenticationService authenticationService;
	private final UserService userService;

    @Override
    public void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain filterChain) throws ServletException, IOException {
        String jwt = extractJWT(request);
	    authenticationService.validateJwtToken(jwt);

	    UserDetails userDetails = this.userService.userDetailsById(authenticationService.userId(jwt));
	    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
			    userDetails.getAuthorities());
	    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	    SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return !API_ENDPOINTS_PATTERN.matcher(path).matches();
	}

    private String extractJWT(final HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TOKEN_PREFIX)) {
            return bearerToken.substring(BEARER_TOKEN_PREFIX.length());
        }
        return null;
    }
}