package com.retail.service;

import org.springframework.http.ResponseEntity;

import com.retail.requestdto.UserRequest;
import com.retail.responsedto.UserResponse;
import com.retail.util.ResponseStructure;

public interface AuthService {

	ResponseEntity<ResponseStructure<UserResponse>> userRegistration(UserRequest userRequest);

}
