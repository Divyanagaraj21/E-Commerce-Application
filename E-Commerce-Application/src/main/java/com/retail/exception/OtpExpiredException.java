package com.retail.exception;

public class OtpExpiredException extends RuntimeException {
	private String message;
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}
	public OtpExpiredException(String message) {
		super();
		this.message = message;
	}
	

}
