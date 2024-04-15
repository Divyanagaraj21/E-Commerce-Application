package com.retail.serviceImpl;

import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.retail.enums.UserRole;
import com.retail.exception.InvalidUserRoleException;
import com.retail.exception.UserAlreadyExistByEmailException;
import com.retail.model.Customer;
import com.retail.model.Seller;
import com.retail.model.User;
import com.retail.repository.UserRepository;
import com.retail.requestdto.UserRequest;
import com.retail.responsedto.UserResponse;
import com.retail.service.AuthService;
import com.retail.util.ResponseStructure;
@Service
public class AuthServiceImpl implements AuthService {

	private ResponseStructure<UserResponse> responseStructure;
	private UserRepository userRepo;



	public AuthServiceImpl(ResponseStructure<UserResponse> responseStructure, UserRepository userRepo) {
		super();
		this.responseStructure = responseStructure;
		this.userRepo = userRepo;
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> userRegistration(UserRequest userRequest) {
		if(userRepo.existsByEmail(userRequest.getEmail()))
			throw new UserAlreadyExistByEmailException("Failed to register user");
		User user=mapToChildentity(userRequest);
		String otp=generateOTP();


		//		if(userRequest.getUserRole()==UserRole.CUSTOMER)
		//		{
		//			Customer customer=mapToCustomerEntity(userRequest,new Customer());
		//			User user=userRepo.save(customer);
		//			return ResponseEntity.ok(responseStructure.setStatus(HttpStatus.OK.value())
		//					.setMessage("User Registered Successfully")
		//					.setData(mapToUserResponse(user)));
		//		}
		//		else 
		//		{
		//			Seller seller=mapToSellerEntity(userRequest,new Seller());
		//			User user=userRepo.save(seller);
		//			return ResponseEntity.ok(responseStructure.setStatus(HttpStatus.OK.value())
		//					.setMessage("User Registered Successfully")
		//					.setData(mapToUserResponse(user)));
		//		}
	}

	private String generateOTP() {
		return String.valueOf(new Random().nextInt(6));
	}

	private <T extends User> T mapToChildentity(UserRequest userRequest) {
		UserRole role=userRequest.getUserRole();
		User user=null;
		switch(role) {
		case SELLER -> user = new Seller();
		case CUSTOMER -> user = new Customer();
		default -> new InvalidUserRoleException("Failed to register user");

		}
		user.setDisplayName(userRequest.getName());
		user.setEmail(userRequest.getEmail());
		user.setPassword(userRequest.getPassword());
		//String[] str=userRequest.getEmail().split("@");
		//user.setUsername(str[0]);
		user.setUsername(userRequest.getEmail().split("@gmail.com")[0]);
		user.setUserRole(userRequest.getUserRole());
		return (T) user;

	}

	//	private Seller mapToSellerEntity(UserRequest userRequest, Seller seller) {
	//		seller.setDisplayName(userRequest.getName());
	//		seller.setEmail(userRequest.getEmail());
	//		seller.setPassword(userRequest.getPassword());
	//		String[] str=userRequest.getEmail().split("@");
	//		seller.setUsername(str[0]);
	//		seller.setUserRole(userRequest.getUserRole());
	//		return seller;
	//	}

	private UserResponse mapToUserResponse(User user) {
		UserResponse userResponse=new UserResponse();
		userResponse.setUserId(user.getUserId());
		userResponse.setDisplayName(user.getDisplayName());
		userResponse.setUserName(user.getUsername());
		userResponse.setEmail(user.getEmail());
		userResponse.setDeleted(user.isDeleted());
		userResponse.setEmailVerified(user.isEmailVerified());
		userResponse.setUserRole(user.getUserRole());
		return userResponse;
	}

	//	private Customer mapToCustomerEntity(UserRequest userRequest, Customer customer) {
	//		customer.setDisplayName(userRequest.getName());
	//		customer.setEmail(userRequest.getEmail());
	//		customer.setPassword(userRequest.getPassword());
	//		String[] str=userRequest.getEmail().split("@");
	//		customer.setUsername(str[0]);
	//		customer.setUserRole(userRequest.getUserRole());
	//		return customer;
	//	} 

}
