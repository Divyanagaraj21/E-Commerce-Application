package com.retail.exception;

public class RegistrationSessionExpiredException extends RuntimeException {

	private String message;
	@Override
	public String getMessage() {
		return message;
	}
	public RegistrationSessionExpiredException(String message) {
		super();
		this.message = message;
	}
	
}
