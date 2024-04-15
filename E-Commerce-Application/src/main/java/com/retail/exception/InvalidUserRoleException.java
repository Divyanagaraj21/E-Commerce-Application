package com.retail.exception;

public class InvalidUserRoleException extends RuntimeException {
	private String message;

	public InvalidUserRoleException(String message) {
		super();
		this.message = message;
	}
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}

}
