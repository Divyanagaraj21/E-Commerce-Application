package com.retail.exception;

public class InavlidUserCredentialException extends RuntimeException {
	private String message;
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}
	public InavlidUserCredentialException(String message) {
		super();
		this.message = message;
	}
	
}
