package com.retail.service;

import org.springframework.http.ResponseEntity;

import com.retail.requestdto.AuthRequest;
import com.retail.requestdto.OTPRequest;
import com.retail.requestdto.UserRequest;
import com.retail.responsedto.AuthResponse;
import com.retail.responsedto.UserResponse;
import com.retail.util.ResponseStructure;
import com.retail.util.SimpleResponseStructure;

public interface AuthService {

	ResponseEntity<SimpleResponseStructure> userRegistration(UserRequest userRequest);

	ResponseEntity<ResponseStructure<UserResponse>> verifyOTP(OTPRequest otpRequest);

	ResponseEntity<ResponseStructure<AuthResponse>> userLogin(AuthRequest authRequest);

}
