package com.rabo.paymentinitiation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidException(String message) {
		super(message);
	}

}
