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
	
	//換發token
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
	
	//登出
	@PostMapping("/logout")
//	HttpServletRequest request，Cookie/Authorization/Content-Type/Body
	public void logout(HttpServletRequest request,HttpServletResponse response) {
//		從 cookie 取出 rawToken
//		Cookie: refreshToken=Uu8pAwK3N7x...
//		        ↑ 名稱        ↑ 值（rawToken）
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


		 refreshTokenService.revokeByRawToken(rawToken);
		 
//		 HTTP沒有「刪除cookie」的指令，只能用「設一個同名的、已過期的 cookie」來覆蓋
//		                               名稱         值
		 Cookie cookie = new Cookie("refreshToken",null);
		 /*
		 建立一個新的 Cookie 物件去覆蓋舊的。如果新的沒設 HttpOnly，
		 而舊的有，瀏覽器會當成兩個不同的 cookie（屬性不同），舊的就刪不掉
		 */
//		 只有我（瀏覽器）能用，禁止 JS 存取
		 cookie.setHttpOnly(true);
//		 登入和登出路徑都一樣，這樣不會被當成另一個cookie
		 cookie.setPath("/api/auth");
//		 0登出時立刻刪除
		 cookie.setMaxAge(0);
		 response.addCookie(cookie);
	}
	
}
