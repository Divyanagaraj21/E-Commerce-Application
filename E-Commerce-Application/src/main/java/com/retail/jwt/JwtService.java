package com.retail.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.lang.Maps;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${myapp.jwt.secret}")
	private String secret;

	@Value("${myapp.jwt.access.expiration}")
	private long accessExpiry;

	@Value("${myapp.jwt.refresh.expiration}")
	private long refreshExpiry;

	public String generateAccessToken(String name,String role)
	{
		return generateToken(name,role, accessExpiry);
	}

	public String generateRefreshToken(String name,String role)
	{
		return generateToken(name,role,refreshExpiry);
	}

	private String generateToken(String name,String role ,Long expiration)
	{
		return Jwts.builder()
				.setClaims(Maps.of("userRole",role).build())
				.setSubject(name)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+expiration))
				.signWith(getSignatureKey(), SignatureAlgorithm.HS256)
				.compact();

	}

	private Key getSignatureKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}
	
	public String getUsername(String token) {
		return parseJwtClaims(token).getSubject();
	}
	
	public String getUserRole(String token)
	{
		return parseJwtClaims(token).get("userRole", String.class);
	}

	private Claims parseJwtClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(getSignatureKey())
			.build()    //return type of build() jwtparser
			.parseClaimsJws(token)
			.getBody();
	}
	

}
