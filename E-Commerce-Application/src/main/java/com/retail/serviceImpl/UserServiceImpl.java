package com.retail.serviceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.retail.enums.UserRole;
import com.retail.model.Customer;
import com.retail.model.User;
import com.retail.repository.UserRepository;
import com.retail.requestdto.UserRequest;
import com.retail.responsedto.UserResponse;
import com.retail.service.UserService;
import com.retail.util.ResponseStructure;

public class UserServiceImpl implements UserService {

	private ResponseStructure<UserResponse> responseStructure;
	private UserRepository userRepo;
	
	
	
	public UserServiceImpl(ResponseStructure<UserResponse> responseStructure, UserRepository userRepo) {
		super();
		this.responseStructure = responseStructure;
		this.userRepo = userRepo;
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> userRegistration(UserRequest userRequest) {
		if(userRequest.getUserRole()==UserRole.CUSTOMER)
		{
			Customer customer=mapToCustomerEntity(userRequest,new Customer());
			User user=userRepo.save(customer);
			return ResponseEntity.ok(responseStructure.setStatus(HttpStatus.OK.value())
					.setMessage("User Registered Successfully")
					.setData(mapToUserResponse(user)));
		}
		else return null;
	}

	private UserResponse mapToUserResponse(User user) {
		UserResponse userResponse=new UserResponse();
		userResponse.setUserId(user.getUserId());
		userResponse.setName(user.getDisplayName());
		userResponse.setUserName(user.getUsername());
		userResponse.setEmail(user.getEmail());
		userResponse.setDeleted(user.isDeleted());
		userResponse.setEmailVerified(user.isEmailVerified());
		userResponse.setUserRole(user.getUserRole());
		return userResponse;
	}

	private Customer mapToCustomerEntity(UserRequest userRequest, Customer customer) {
		customer.setDisplayName(userRequest.getName());
		customer.setEmail(userRequest.getEmail());
		customer.setPassword(userRequest.getPassword());
		String[] str=userRequest.getEmail().split("@");
		customer.setUsername(str[0]);
		return customer;
	}

}
