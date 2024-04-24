package com.retail.exception;

public class InvalidPasswordException extends RuntimeException {
	private String message;
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}
	public InvalidPasswordException(String message) {
		super();
		this.message = message;
	}
	

}
