package com.retail.serviceImpl;

import java.util.Date;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.retail.cache.CacheStore;
import com.retail.enums.UserRole;
import com.retail.exception.InvalidOTPException;
import com.retail.exception.InvalidUserRoleException;
import com.retail.exception.OTPExpiredException;
import com.retail.exception.RegistrationSessionExpiredException;
import com.retail.exception.UserAlreadyExistByEmailException;
import com.retail.mail_service.MailService;
import com.retail.mail_service.MessageModel;
import com.retail.model.Customer;
import com.retail.model.Seller;
import com.retail.model.User;
import com.retail.repository.UserRepository;
import com.retail.requestdto.OTPRequest;
import com.retail.requestdto.UserRequest;
import com.retail.responsedto.UserResponse;
import com.retail.service.AuthService;
import com.retail.util.ResponseStructure;
import com.retail.util.SimpleResponseStructure;

import jakarta.mail.MessagingException;

@Service
public class AuthServiceImpl implements AuthService {

	private ResponseStructure<UserResponse> responseStructure;
	private UserRepository userRepo;
	private CacheStore<String> otpCache;
	private CacheStore<User> userCache;
	private SimpleResponseStructure simpleResponse;
	private MailService mailService;

	public AuthServiceImpl(ResponseStructure<UserResponse> responseStructure, UserRepository userRepo,
			CacheStore<String> otpCache, CacheStore<User> userCache, SimpleResponseStructure simpleResponse,
			MailService mailService) {
		super();
		this.responseStructure = responseStructure;
		this.userRepo = userRepo;
		this.otpCache = otpCache;
		this.userCache = userCache;
		this.simpleResponse = simpleResponse;
		this.mailService = mailService;
	}

	@Override
	public ResponseEntity<SimpleResponseStructure> userRegistration(UserRequest userRequest) {
		if(userRepo.existsByEmail(userRequest.getEmail()))
			throw new UserAlreadyExistByEmailException("Failed to register user");
		User user=mapToChildentity(userRequest);
		String otp=generateOTP();

		otpCache.add(user.getEmail(), otp);
		userCache.add(user.getEmail(), user);

		//send mail with otp
		try {
			sendOTP(user,otp);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok(simpleResponse.setStatus(HttpStatus.ACCEPTED.value())
				.setMessage("verify OTP sent throug mail to complete | "+ "OTP expires in 1minute"));

	}

	private void sendOTP(User user, String otp) throws MessagingException {
		MessageModel messageModel=new MessageModel();
		messageModel.setTo(user.getEmail());
		messageModel.setSubject("verify your OTP");
		messageModel.setText("<p> Hi, <br>"
				+"Thanks for your interest in E-Commerce-Application"
				+" Please verify your mail Id using the OTP given below.</p>"
				+"<br>"
				+"<h1>"+otp+"</h1>"
				+"<br>"
				+"<p>Please ignore if its not yoy</p>"
				+"<br>"
				+"with best regards"
				+"<h3>E-Commerce-Application</h3>");
		mailService.sendMailMessage(messageModel);
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
	public ResponseEntity<ResponseStructure<UserResponse>> verifyOTP(OTPRequest otpRequest) {

		System.out.println(otpRequest.getEmail());
		System.out.println(otpRequest.getOtp());
		if(otpCache.get(otpRequest.getEmail())==null) 
			throw new OTPExpiredException("the otp time is expired");

		if(!otpCache.get(otpRequest.getEmail()).equals(otpRequest.getOtp()))
			throw new InvalidOTPException("OTP is Invalided");

		User user=userCache.get(otpRequest.getEmail());
		if(user==null)
			throw new RegistrationSessionExpiredException("the user time out to registered");

		user.setEmailVerified(true);
		//userRepo.save(user);
		return ResponseEntity.status(HttpStatus.CREATED.value()).body(responseStructure.setStatus(HttpStatus.CREATED.
				value())
				.setData(mapToUserResponse(user))
				.setMessage("User Object is created Successfully"));	
	}




}
