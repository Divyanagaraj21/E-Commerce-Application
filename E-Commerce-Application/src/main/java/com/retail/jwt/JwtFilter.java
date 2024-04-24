package com.retail.jwt;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.retail.repository.AccessTokenRepository;
import com.retail.repository.RefreshTokenRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private AccessTokenRepository accessTokenRepository;
	private RefreshTokenRepository refreshTokenRepository;
	private JwtService jwtService;
	private PasswordEncoder passwordEncoder;



	public JwtFilter(AccessTokenRepository accessTokenRepository, RefreshTokenRepository refreshTokenRepository,
			JwtService jwtService) {
		super();
		this.accessTokenRepository = accessTokenRepository;
		this.refreshTokenRepository = refreshTokenRepository;
		this.jwtService = jwtService;
	}



	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String at=null;
		String rt=null;
		Cookie[] cookies = request.getCookies();

		if(cookies!=null) {
			for(Cookie cookie:cookies)
			{
				if(cookie.getName().equals("at"))
					at=cookie.getValue();
				if(cookie.getName().equals("rt"))
					rt=cookie.getValue();
			}
		}

		try {
			if(at!=null && rt!=null)
			{
				if(accessTokenRepository.existsByTokenAndIsBlocked(at,true)&& refreshTokenRepository.existsByTokenAndIsBlocked(rt,true))throw new RuntimeException();
				String username=jwtService.getUsername(at);
				String userRole=jwtService.getUserRole(at);
				Set<SimpleGrantedAuthority> authority = Collections.singleton(new SimpleGrantedAuthority(userRole));
				if(username!=null && userRole!=null && SecurityContextHolder.getContext().getAuthentication()!=null)
				{
					UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(username,null,authority);
					authenticationToken.setDetails(new WebAuthenticationDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
		}
		catch (ExpiredJwtException ex) {
			logger.error("JWT token has expired: " + ex.getMessage());
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token has expired");
			return;
		} catch (JwtException ex) {
			logger.error("JWT token exception: " + ex.getMessage());
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token exception");
			return;
		}


		filterChain.doFilter(request, response);


	}

}
