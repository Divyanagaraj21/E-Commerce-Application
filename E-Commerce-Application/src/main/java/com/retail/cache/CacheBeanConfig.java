package com.retail.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.retail.model.User;

@Configuration
public class CacheBeanConfig {
	@Bean
	CacheStore<String> otpCache(){
		return new CacheStore<String>(5);
	}
	
	@Bean
	CacheStore<User> userCache(){
		return new CacheStore<User>(30);
	}

	
}
