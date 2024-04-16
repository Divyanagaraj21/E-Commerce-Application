package com.retail.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retail.requestdto.OtpRequest;
import com.retail.requestdto.UserRequest;
import com.retail.responsedto.UserResponse;
import com.retail.service.AuthService;
import com.retail.util.ResponseStructure;
import com.retail.util.SimpleResponseStructure;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
	private AuthService userService;

	public AuthController(AuthService userService) {
		super();
		this.userService = userService;
	}
	
	@PostMapping("/users")
	public ResponseEntity<SimpleResponseStructure> userRegistration(@RequestBody UserRequest userRequest)
	{
		return userService.userRegistration(userRequest);
	}
	
	@PostMapping("/verify-email")
	public ResponseEntity<ResponseStructure<UserResponse>> verifyOTP(@RequestBody OtpRequest otpRequest)
	{
		return userService.verifyOTP(otpRequest);
	}
	
}
