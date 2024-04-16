package com.retail.service;

import org.springframework.http.ResponseEntity;

import com.retail.requestdto.UserRequest;
import com.retail.responsedto.UserResponse;
import com.retail.util.ResponseStructure;

public interface AuthService {

	ResponseEntity<String> userRegistration(UserRequest userRequest);

	ResponseEntity<ResponseStructure<UserResponse>> verifyOTP(String otp);

}
