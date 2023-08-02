package com.isteer.springsecurity.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.isteer.springsecurity.response.CustomErrorResponse;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(value = { ConstraintException.class })
	public ResponseEntity<Object> handleConstraintException(ConstraintException exception) {
		long statusCode = exception.getStatus();
		List<String> exceptions = exception.getException();
		String message = exception.getMessage();

		CustomErrorResponse customResponse = new CustomErrorResponse(statusCode, message, exceptions);

		return new ResponseEntity<>(customResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = { DetailsNotFoundException.class })
	public ResponseEntity<Object> handleDetailsNotFoundException(DetailsNotProvidedException exception) {
		long statusCode = exception.getStatus();
		List<String> exceptions = exception.getException();
		String message = exception.getMessage();

		CustomErrorResponse customResponse = new CustomErrorResponse(statusCode, message, exceptions);

		return new ResponseEntity<>(customResponse, HttpStatus.NOT_FOUND);

	}


	@ExceptionHandler(value = { DetailsNotProvidedException.class })
	public ResponseEntity<Object> handleDetailsNotProvidedException(DetailsNotProvidedException exception) {
		long statusCode = exception.getStatus();
		List<String> exceptions = exception.getException();
		String message = exception.getMessage();

		CustomErrorResponse customResponse = new CustomErrorResponse(statusCode, message, exceptions);

		return new ResponseEntity<>(customResponse, HttpStatus.NOT_FOUND);

	}


	@Override
	@ExceptionHandler(value = { MethodNotAllowedException.class })
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException exception, HttpHeaders headers, HttpStatusCode status,
			WebRequest request) {

		List<String> exceptions = new ArrayList<>();

		exceptions.add(exception.getLocalizedMessage());

		CustomErrorResponse customResponse = new CustomErrorResponse(0, PAGE_NOT_FOUND_LOG_CATEGORY, exceptions);

		return new ResponseEntity<>(customResponse, HttpStatus.BAD_REQUEST);
	}


	@ExceptionHandler(value = { SqlSyntaxException.class })
	public ResponseEntity<Object> handleSqlSyntaxException(SqlSyntaxException exception) {
		long statusCode = exception.getStatus();
		List<String> exceptions = exception.getException();
		String message = exception.getMessage();

		CustomErrorResponse customResponse = new CustomErrorResponse(statusCode, message, exceptions);

		return new ResponseEntity<>(customResponse, HttpStatus.NOT_FOUND);

	}

}
