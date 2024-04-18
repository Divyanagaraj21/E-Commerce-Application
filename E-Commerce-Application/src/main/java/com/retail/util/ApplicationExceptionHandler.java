package com.retail.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.retail.exception.InvalidOTPException;
import com.retail.exception.InvalidUserRoleException;
import com.retail.exception.OTPExpiredException;
import com.retail.exception.RegistrationSessionExpiredException;
import com.retail.exception.UserAlreadyExistByEmailException;

@RestControllerAdvice
public class ApplicationExceptionHandler {
	
	private ErrorStructure<String> structure;
	
	
	public ApplicationExceptionHandler(ErrorStructure<String> structure) {
		super();
		this.structure = structure;
	}


	@ExceptionHandler(InvalidOTPException.class)
	public ResponseEntity<ErrorStructure<String>> handleInvalidOTPException(InvalidOTPException ex)
	{
		return errorStructure(HttpStatus.BAD_REQUEST,ex.getMessage(),"Invalid OTP,enter the correct OTP");
	}
	
	@ExceptionHandler(InvalidUserRoleException.class)
	public ResponseEntity<ErrorStructure<String>> handleInvalidUserRoleException(InvalidUserRoleException ex)
	{
		return errorStructure(HttpStatus.BAD_REQUEST,ex.getMessage(),"Failed To Register User");
	}
	
	@ExceptionHandler(OTPExpiredException.class)
	public ResponseEntity<ErrorStructure<String>> handleOTPExpiredException(OTPExpiredException ex)
	{
		return errorStructure(HttpStatus.BAD_REQUEST,ex.getMessage(),"Verification Failed");
	}

	@ExceptionHandler(RegistrationSessionExpiredException.class)
	public ResponseEntity<ErrorStructure<String>> handleRegistrationSessionExpiredException(RegistrationSessionExpiredException ex)
	{
		return errorStructure(HttpStatus.BAD_REQUEST,ex.getMessage(),"Verifivation Failed");
	}

	@ExceptionHandler(UserAlreadyExistByEmailException.class)
	public ResponseEntity<ErrorStructure<String>> handleUserAlreadyExistByEmailException(UserAlreadyExistByEmailException ex)
	{
		return errorStructure(HttpStatus.BAD_REQUEST,ex.getMessage(),"user already exists by given EmailId");
	}



	private ResponseEntity<ErrorStructure<String>> errorStructure(HttpStatus status, String errorMessage,
			String rootCause) {

			return new ResponseEntity<ErrorStructure<String>>(structure.setStatus(status.value())
				.setErrorMessage(errorMessage)
				.setRootCause(rootCause),HttpStatus.BAD_REQUEST);
	}

}
