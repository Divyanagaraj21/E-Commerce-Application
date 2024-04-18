package com.retail.cache;

import java.time.Duration;

import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.retail.exception.InvalidKeyException;
public class CacheStore<T> {
	
	private Cache<String, T> cache;

	public CacheStore(int maxAge) {
		this.cache=CacheBuilder.newBuilder()
		.expireAfterWrite(Duration.ofMinutes(maxAge))
		.concurrencyLevel(Runtime.getRuntime().availableProcessors())
		.build();
		
		}

	public void add(String key,T value)
	{
		cache.put(key, value);
	}
	
	public T get(String key)
	{
//		if( cache.getIfPresent(key)!=null)
			return cache.getIfPresent(key);
//		else
//			throw new InvalidKeyException("Invalid key");
	}
	
	public void remove(String key)
	{
		cache.invalidate(key);
	}
	

}
