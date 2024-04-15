package com.retail.exception;

public class UserAlreadyExistByEmailException extends RuntimeException {
	private String message;

	@Override
	public String getMessage() {
		return message;
	}

	public UserAlreadyExistByEmailException(String message) {
		super();
		this.message = message;
	}
	
	

}
