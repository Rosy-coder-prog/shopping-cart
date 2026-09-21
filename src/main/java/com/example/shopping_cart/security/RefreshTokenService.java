package com.example.shopping_cart.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.shopping_cart.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;
	private final  long refreshTokenExpiration;
	
	public RefreshTokenService(
			RefreshTokenRepository refreshTokenRepository,
//			讀設定擋@Value("${...}")
			@Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {
		this.refreshTokenRepository = refreshTokenRepository;
		this.refreshTokenExpiration =refreshTokenExpiration;
	}
}
