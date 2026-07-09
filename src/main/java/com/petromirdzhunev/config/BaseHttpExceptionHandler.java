package com.petromirdzhunev.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.petromirdzhunev.domain.core.exception.EntityNotFoundException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class BaseHttpExceptionHandler extends ResponseEntityExceptionHandler {

	private final ObjectMapper objectMapper;

	private static final String ERRORS = "errors";
	private static final String TIMESTAMP = "timestamp";

    @Override
	protected final ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException exception,
			final HttpHeaders headers, final HttpStatusCode status, final WebRequest request) {
		log.warn(HttpStatus.BAD_REQUEST.toString(), exception);
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		problemDetail.setProperty(TIMESTAMP, truncatedIsoDateTime());
		if (exception.getBindingResult().hasFieldErrors()) {
			problemDetail.setProperty(ERRORS, getExceptionFieldValidationErrors(exception));
		} else {
			problemDetail.setProperty(ERRORS, getExceptionValidationErrors(exception));
		}

        return handleExceptionInternal(exception, problemDetail, headers, status, request);
    }

	@ExceptionHandler(ConstraintViolationException.class)
    public final ProblemDetail handleConstraintViolationException(final ConstraintViolationException exception) {
		log.warn(HttpStatus.BAD_REQUEST.toString(), exception);
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		problemDetail.setProperty(TIMESTAMP, truncatedIsoDateTime());
		problemDetail.setProperty(ERRORS, getExceptionFieldValidationErrors(exception));

		return problemDetail;
    }

	@ExceptionHandler(IllegalStateException.class)
	public final ProblemDetail handleIllegalStateException(final IllegalStateException exception) {
		log.warn(HttpStatus.BAD_REQUEST.toString(), exception);
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
		problemDetail.setProperty(TIMESTAMP, truncatedIsoDateTime());

		return problemDetail;
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public final ProblemDetail handleIllegalArgumentException(final IllegalArgumentException exception) {
		log.warn(HttpStatus.BAD_REQUEST.toString(), exception);
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
		problemDetail.setProperty(TIMESTAMP, truncatedIsoDateTime());

		return problemDetail;
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public final ProblemDetail handleEntityNotFoundException(final EntityNotFoundException exception) {
		log.warn(HttpStatus.NOT_FOUND.toString(), exception);
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
		problemDetail.setProperty(TIMESTAMP, truncatedIsoDateTime());

		return problemDetail;
	}

	@ExceptionHandler(AuthenticationException.class)
	public final ProblemDetail handleJwtException(final AuthenticationException exception) {
		log.warn(HttpStatus.UNAUTHORIZED.toString(), exception);
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
		problemDetail.setProperty(TIMESTAMP, truncatedIsoDateTime());

		return problemDetail;
	}

	@ExceptionHandler(AuthorizationDeniedException.class)
	public final ProblemDetail handleJwtException(final AuthorizationDeniedException exception) {
		log.warn(HttpStatus.FORBIDDEN.toString(), exception);
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, exception.getMessage());
		problemDetail.setProperty(TIMESTAMP, truncatedIsoDateTime());

		return problemDetail;
	}

	@ExceptionHandler(Exception.class)
    public final ProblemDetail handleUnknownException(final Exception exception) {
		log.error(HttpStatus.INTERNAL_SERVER_ERROR.toString(), exception);
		ProblemDetail problemDetail =
				ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
		problemDetail.setProperty(TIMESTAMP, truncatedIsoDateTime());

		return problemDetail;
    }

	protected ProblemDetail handleCustomException(final Exception exception, final HttpStatus httpStatus) {
		log.error(httpStatus.toString(), exception);
		ProblemDetail problemDetail =
				ProblemDetail.forStatusAndDetail(httpStatus, exception.getMessage());
		problemDetail.setProperty(TIMESTAMP, truncatedIsoDateTime());

		return problemDetail;
	}

	public ProblemDetail handleCustomException(final Exception exception, final HttpStatus httpStatus,
			List<String> errors) {
		ProblemDetail problemDetail = handleCustomException(exception, httpStatus);
		problemDetail.setProperty(ERRORS, errors);

		return problemDetail;
	}

	public void writeExceptionToResponse(final Exception exception, final HttpStatus httpStatus,
			final HttpServletResponse response) throws IOException {
		log.warn(httpStatus.toString(), exception);
		ProblemDetail problemDetail = handleCustomException(exception, httpStatus);

		response.setStatus(httpStatus.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		ServletOutputStream out = response.getOutputStream();
		objectMapper.writeValue(out, problemDetail);
		out.flush();
	}

	private List<String> getExceptionValidationErrors(final MethodArgumentNotValidException exception) {
		return exception.getBindingResult()
		                .getAllErrors().stream()
		                .map(error -> "'%s' %s".formatted(error.getObjectName(), error.getDefaultMessage()))
		                .toList();
	}

    private List<String> getExceptionFieldValidationErrors(final MethodArgumentNotValidException exception) {
		return exception.getBindingResult()
						.getFieldErrors().stream()
						.filter(fieldError -> fieldError.getDefaultMessage() != null)
						.map(fieldError -> "'%s.%s' %s".formatted(
								fieldError.getObjectName(),
								fieldError.getField(),
								fieldError.getDefaultMessage()))
						.toList();
    }

    private List<String> getExceptionFieldValidationErrors(final ConstraintViolationException exception) {
	    return exception.getConstraintViolations()
	                    .stream()
	                    .map(constraintViolation -> "'%s.%s' %s".formatted(
								constraintViolation.getRootBeanClass().getSimpleName(),
			                    constraintViolation.getPropertyPath().toString(),
			                    constraintViolation.getMessage()))
	                    .toList();
    }

	private static String truncatedIsoDateTime() {
		return LocalDateTime.now()
		                    .truncatedTo(ChronoUnit.SECONDS)
		                    .format(DateTimeFormatter.ISO_DATE_TIME);
	}


}