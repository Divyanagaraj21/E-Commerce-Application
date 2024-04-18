package com.retail.util;

import org.springframework.stereotype.Component;

@Component
public class ErrorStructure<T> {
	private int status;
	private String errorMessage;
	private T rootCause;
	public int getStatus() {
		return status;
	}
	public ErrorStructure<T> setStatus(int status) {
		this.status = status;
		return this;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	public ErrorStructure<T> setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		return this;
	}
	public T getRootCause() {
		return rootCause;
	}
	public ErrorStructure<T> setRootCause(T rootCause) {
		this.rootCause = rootCause;
		return this;
	}


}
