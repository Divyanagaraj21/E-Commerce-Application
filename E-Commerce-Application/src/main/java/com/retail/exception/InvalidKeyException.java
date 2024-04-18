package com.retail.exception;

public class InvalidKeyException extends RuntimeException {
	private String message;
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}
	public InvalidKeyException(String message) {
		super();
		this.message = message;
	}

	
}
