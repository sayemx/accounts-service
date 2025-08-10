package com.sayem.accounts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ResourceNotFoundException extends RuntimeException {
	
	public ResourceNotFoundException(String resourcename, String fieldname, String valueName) {
		super(String.format("%s not found with the given input data %s : '%s", resourcename, fieldname, valueName));
	}
}
