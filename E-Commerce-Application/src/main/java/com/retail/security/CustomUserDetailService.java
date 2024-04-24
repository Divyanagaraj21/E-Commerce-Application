package com.retail.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.retail.repository.UserRepository;
@Service
public class CustomUserDetailService implements UserDetailsService{
	private UserRepository userRepo;

	public CustomUserDetailService(UserRepository userRepo) {
		super();
		this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepo.findByUsername(username).map(CustomUserDetail::new)
				.orElseThrow();
	}

	
}

