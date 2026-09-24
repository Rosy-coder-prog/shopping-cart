package com.example.shopping_cart.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopping_cart.dto.LoginResponseDTO;
import com.example.shopping_cart.dto.MemberDTO;
import com.example.shopping_cart.security.RefreshTokenService;
import com.example.shopping_cart.service.IMemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/member")
public class MemberController {
	
	
	
	private final IMemberService iMemberService;
	private final RefreshTokenService refreshTokenService;
	
	public MemberController(RefreshTokenService refreshTokenService
			                ,IMemberService iMemberService ) {
		this.refreshTokenService = refreshTokenService;
		this.iMemberService=iMemberService;
		
	}
	
//	註冊會員
	@PostMapping("/register")
	public String registerMember(@RequestBody MemberDTO memberDTO) {
		iMemberService.registerMember(memberDTO.getMailbox(),memberDTO.getPassword() , memberDTO.getMembername());
		return "註冊成功";
	}
	
	
	
//	登入會員

	@PostMapping("/login")
	public LoginResponseDTO loginMember(@RequestBody MemberDTO memberDTO,
			//寫回應（cookie、狀態碼）
			HttpServletResponse response) {
		
		LoginResponseDTO loginResponseDTO =	iMemberService.loginMember(memberDTO.getMailbox(), memberDTO.getPassword());
//		record 的 getter 是 rawRefreshToken()，不是 getRawRefreshToken()
//		new Cookie("refreshToken", 值)	名稱是 refreshToken，之後前端的 cookie 裡會看到這個名字
		Cookie cookie = new Cookie("refreshToken",loginResponseDTO.rawRefreshToken());
//		setHttpOnly(true)	JS 讀不到
		cookie.setHttpOnly(true);
//		只有 /api/auth 開頭的請求才帶這個 cookie
//		購物車、商品這些 API 不需要 refresh token，
//		只有 /api/auth/refresh 和 /api/auth/logout 需要。限制路徑後，其他請求不會多帶一個 cookie
		cookie.setPath("/api/auth");
//		7 天（秒為單位，不是毫秒）
		cookie.setMaxAge(7 * 24 * 60 * 60);
//		把 cookie 加進回應的 Set-Cookie header
		response.addCookie(cookie);
		return loginResponseDTO;
	}
	
}
