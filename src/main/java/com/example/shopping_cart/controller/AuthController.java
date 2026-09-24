package com.example.shopping_cart.controller;

import com.example.shopping_cart.repository.RefreshTokenRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopping_cart.dto.LoginResponseDTO;
import com.example.shopping_cart.dto.MemberResponseDTO;
import com.example.shopping_cart.exception.BusinessException;
import com.example.shopping_cart.model.Member;
import com.example.shopping_cart.model.RefreshToken;
import com.example.shopping_cart.security.JwtService;
import com.example.shopping_cart.security.RefreshTokenService;
import com.example.shopping_cart.service.IMemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	
	private final RefreshTokenService refreshTokenService;
	private final JwtService jwtService;
	private final IMemberService iMemberService;
	
	public AuthController(RefreshTokenService refreshTokenService, JwtService jwtService,
			IMemberService iMemberService) {
		this.refreshTokenService = refreshTokenService;
		this.jwtService = jwtService;
		this.iMemberService = iMemberService;
	}
	
	@PostMapping("/refresh")
//	request 讀 cookie、response 寫新的 cookie
	public LoginResponseDTO refresh(HttpServletRequest request,HttpServletResponse response) {
//		從 cookie 取出 rawToken
		String rawToken = null;
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if("refreshToken".equals(cookie.getName())) {
					rawToken = cookie.getValue();
				}
			}
		}
		if(rawToken == null) {
			throw new BusinessException(401, "未提供refresh token");
		}
//		驗證舊的token
		RefreshToken oldToken = refreshTokenService.validateAndRotate(rawToken);
//		從舊token拿到會員
		Member member = oldToken.getMember();
//		產生新的accessToken
		String accessToken = jwtService.generateAccessToken(member.getMemberID(),member.getRole());
		
		String newRawToken =refreshTokenService.createRefreshToken(member);
		
//		新的refreshtoken放進cookie
		Cookie cookie = new Cookie("refreshToken",newRawToken);
		cookie.setHttpOnly(true);
		cookie.setPath("/api/auth");
		cookie.setMaxAge(7 * 24 * 60 * 60);
		response.addCookie(cookie);
		
		MemberResponseDTO memberResponseDTO = new MemberResponseDTO(
				member.getMemberID(),member.getMembername(),member.getRole());
		return new LoginResponseDTO(accessToken, memberResponseDTO,newRawToken);
		
	}
	
	
	
}
