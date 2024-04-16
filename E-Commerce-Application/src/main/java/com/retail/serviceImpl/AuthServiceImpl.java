package com.retail.serviceImpl;

import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.retail.cache.CacheStore;
import com.retail.enums.UserRole;
import com.retail.exception.InvalidOTPException;
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
	private CacheStore<String> otpCache;
	private CacheStore<User> userCache;



	public AuthServiceImpl(ResponseStructure<UserResponse> responseStructure, UserRepository userRepo,
			CacheStore<String> otpCache, CacheStore<User> userCache) {
		super();
		this.responseStructure = responseStructure;
		this.userRepo = userRepo;
		this.otpCache = otpCache;
		this.userCache = userCache;
	}

	@Override
	public ResponseEntity<String> userRegistration(UserRequest userRequest) {
		if(userRepo.existsByEmail(userRequest.getEmail()))
			throw new UserAlreadyExistByEmailException("Failed to register user");
		User user=mapToChildentity(userRequest);
		String otp=generateOTP();

		otpCache.add("otp", otp);
		userCache.add("user", user);

		return ResponseEntity.ok(otp);

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
		return String.valueOf(new Random().nextInt(999999));
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
		user.setDeleted(false);
		user.setEmailVerified(false);
		return (T) user;

	}


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

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> verifyOTP(String otp) {
		if(!otpCache.get("otp").equals(otp))
			throw new InvalidOTPException("Verification Failed");
		User user=userCache.get("user");
		user.setEmailVerified(true);
		//		User user=userRepo.save(mapToUserResponse(user));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(responseStructure.setStatus(HttpStatus.CREATED.value())
				.setMessage("User Registered Successfully")
				.setData(mapToUserResponse(userCache.get("user"))));	
	}



}
