package com.thai27.trang_tin_tuc_v5_be.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
	}

	@ExceptionHandler(UnclearSortingDirectionException.class)
	public ResponseEntity<ErrorDetails> unclearSortingDirectionException(UnclearSortingDirectionException ex, WebRequest request) {
		return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
	}

	@ExceptionHandler(ChildDataStillExist.class)
	public ResponseEntity<ErrorDetails> childDataStillExist(Exception ex, WebRequest request) {
		return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
	}

	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<ErrorDetails> tokenExpiredException(Exception ex, WebRequest request) {
		return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), request);
	}

	@ExceptionHandler(UserInfoAlreadyExistException.class)
	public ResponseEntity<ErrorDetails> usernameAlreadyExist(Exception ex, WebRequest request) {
		return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
	}

	@ExceptionHandler(SignUpCodeExpiredException.class)
	public ResponseEntity<ErrorDetails> signUpCodeExpiredException(SignUpCodeExpiredException ex, WebRequest request) {
		return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorDetails> badRequestException(BadRequestException ex, WebRequest request) {
		return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ErrorDetails> conflictException(ConflictException ex, WebRequest request) {
		return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ErrorDetails> forbiddenException(ForbiddenException ex, WebRequest request) {
		return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), request);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorDetails> badCredentialsException(BadCredentialsException ex, WebRequest request) {
		return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDetails> methodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(this::formatFieldError).toList();
		ErrorDetails errorDetails = new ErrorDetails(
				Instant.now(),
				HttpStatus.BAD_REQUEST.value(),
				"Validation failed",
				request.getDescription(false),
				errors
		);
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorDetails> httpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
		return buildErrorResponse(HttpStatus.BAD_REQUEST, "Malformed request body", request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> globalExceptionHandler(Exception ex, WebRequest request) {
		return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
	}

	private ResponseEntity<ErrorDetails> buildErrorResponse(HttpStatus status, String message, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(
				Instant.now(),
				status.value(),
				message,
				request.getDescription(false)
		);
		return new ResponseEntity<>(errorDetails, status);
	}

	private String formatFieldError(FieldError fieldError) {
		return fieldError.getField() + ": " + fieldError.getDefaultMessage();
	}

}
