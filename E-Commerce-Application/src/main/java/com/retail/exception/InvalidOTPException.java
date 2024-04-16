package com.retail.exception;

public class InvalidOTPException extends RuntimeException {
	private String message;
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}
	public InvalidOTPException(String message) {
		super();
		this.message = message;
	}
	

}
