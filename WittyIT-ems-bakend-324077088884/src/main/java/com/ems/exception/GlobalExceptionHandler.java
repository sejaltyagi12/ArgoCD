package com.ems.exception;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleAccessDeniedException(ConstraintViolationException ex, WebRequest request) {
		String errors = "";
		int i = 0;
		for(ConstraintViolation e : ex.getConstraintViolations()) {
			errors = errors.concat(++i + ". " + e.getMessage());
		}
		return new ResponseEntity<Object>(new ServiceRuntimeException(errors), HttpStatus.BAD_REQUEST);
	}
}
