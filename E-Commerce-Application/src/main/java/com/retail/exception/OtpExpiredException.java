package com.retail.exception;

public class OTPExpiredException extends RuntimeException {
	private String message;
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}
	public OTPExpiredException(String message) {
		super();
		this.message = message;
	}
	

}
