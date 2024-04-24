package com.retail.serviceImpl;

import java.time.Duration;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.retail.cache.CacheStore;
import com.retail.enums.UserRole;
import com.retail.exception.InavlidUserCredentialException;
import com.retail.exception.InvalidOTPException;
import com.retail.exception.InvalidPasswordException;
import com.retail.exception.InvalidUserRoleException;
import com.retail.exception.OTPExpiredException;
import com.retail.exception.RegistrationSessionExpiredException;
import com.retail.exception.UserAlreadyExistByEmailException;
import com.retail.jwt.JwtService;
import com.retail.mail_service.MailService;
import com.retail.mail_service.MessageModel;
import com.retail.model.AccessToken;
import com.retail.model.Customer;
import com.retail.model.RefreshToken;
import com.retail.model.Seller;
import com.retail.model.User;
import com.retail.repository.AccessTokenRepository;
import com.retail.repository.RefreshTokenRepository;
import com.retail.repository.UserRepository;
import com.retail.requestdto.AuthRequest;
import com.retail.requestdto.OTPRequest;
import com.retail.requestdto.UserRequest;
import com.retail.responsedto.AuthResponse;
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
	private AuthenticationManager authenticationManager;
	private JwtService jwtService;
	private ResponseStructure<AuthResponse> authResponseStructure;
	private AccessTokenRepository accessRepo;
	private RefreshTokenRepository refreshRepo;
	private PasswordEncoder passwordEncoder;

	@Value("${myapp.jwt.access.expiration}")
	private long accessExpiration;

	@Value("${myapp.jwt.refresh.expiration}")
	private long refreshExpiration;
	


	public AuthServiceImpl(ResponseStructure<UserResponse> responseStructure, UserRepository userRepo,
			CacheStore<String> otpCache, CacheStore<User> userCache, SimpleResponseStructure simpleResponse,
			MailService mailService, AuthenticationManager authenticationManager, JwtService jwtService,
			ResponseStructure<AuthResponse> authResponseStructure, AccessTokenRepository accessRepo,
			RefreshTokenRepository refreshRepo, PasswordEncoder passwordEncoder) {
		super();
		this.responseStructure = responseStructure;
		this.userRepo = userRepo;
		this.otpCache = otpCache;
		this.userCache = userCache;
		this.simpleResponse = simpleResponse;
		this.mailService = mailService;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.authResponseStructure = authResponseStructure;
		this.accessRepo = accessRepo;
		this.refreshRepo = refreshRepo;
		this.passwordEncoder = passwordEncoder;
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
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
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
		userRepo.save(user);
		return ResponseEntity.status(HttpStatus.CREATED.value()).body(responseStructure.setStatus(HttpStatus.CREATED.
				value())
				.setData(mapToUserResponse(user))
				.setMessage("User Object is created Successfully"));	
	}


	//user login

	@Override
	public ResponseEntity<ResponseStructure<AuthResponse>> userLogin(AuthRequest authRequest) {
		String username=authRequest.getUsername().split("@gmail.com")[0];
							Authentication authentication=authenticationManager.authenticate(
									new UsernamePasswordAuthenticationToken(username, authRequest.getPassword()));
							if(!authentication.isAuthenticated()) throw new InavlidUserCredentialException("failed to login");

							SecurityContextHolder.getContext().setAuthentication(authentication);

//		if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) throw new InavlidUserCredentialException("failed to login");

		// generate access and refresh token
		HttpHeaders header=new HttpHeaders();
		userRepo.findByUsername(username).ifPresent(user->{
			generateAccessToken(user,user.getUserRole().toString(),header);
			generateRefreshToken(user,user.getUserRole().toString(),header);
		});

		return userRepo.findByUsername(username).map(user->{
			return ResponseEntity.ok().headers(header).body(authResponseStructure.setStatus(HttpStatus.OK.value())
					.setMessage("user login successful")
					.setData(mapToAuthResponse(user)));
		}).orElseThrow();

	}


	private AuthResponse mapToAuthResponse(User user) {
		AuthResponse authResponse=new AuthResponse();
		authResponse.setUserId(user.getUserId());
		authResponse.setUsername(user.getUsername());
		authResponse.setUserRole(user.getUserRole());
		authResponse.setAccessExpiration(accessExpiration/1000);
		authResponse.setRefreshExpiration(refreshExpiration/1000);
		return authResponse;
	}



	private void generateRefreshToken(User user,String role, HttpHeaders header) {


		String token=jwtService.generateRefreshToken(user.getUsername(),role);
		header.add(HttpHeaders.SET_COOKIE, configureCookie("rt",token,refreshExpiration));
		RefreshToken rt=new RefreshToken();
		rt.setUser(user);
		rt.setToken(token);
		rt.setExpiration(refreshExpiration);
		rt.setBlocked(false);
		refreshRepo.save(rt);


	}


	private void generateAccessToken(User user,String role, HttpHeaders header) {

		String token=jwtService.generateAccessToken(user.getUsername(),role);
		header.add(HttpHeaders.SET_COOKIE, configureCookie("at",token,accessExpiration));
		AccessToken at=new AccessToken();
		at.setUser(user);
		at.setToken(token);
		at.setExpiration(accessExpiration);
		at.setBlocked(false);
		accessRepo.save(at);


	}

	private String configureCookie(String name, String value, long maxAge) {

		return ResponseCookie.from(name, value)
				.domain("localhost")
				.path("/")
				.httpOnly(true)
				.secure(false)
				.maxAge(Duration.ofMillis(maxAge))
				.sameSite("Lax")
				.build().toString();
	}






}
