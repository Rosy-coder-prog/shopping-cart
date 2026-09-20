package com.example.shopping_cart.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//Bean
@Component
//extends OncePerRequestFilter，Spring 提供的抽象類別，保證每個請求只執行一次

public class JwtFilter extends OncePerRequestFilter {

	private final JwtService jwtService;

	public JwtFilter(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	
//	OncePerRequestFilter的抽象方法，每個請求都會執行它
//	子類別實作
	@Override
	protected void doFilterInternal(
//			request	請求物件，可以讀 header、參數、路徑
//			用來向伺服器證明「你是誰」以及「你有沒有權限做這件事」（也就是授權）
			HttpServletRequest request,
//			response	回應物件，可以寫狀態碼、內容
			HttpServletResponse response,
//			filterChain	代表「後面的 Filter 和 Controller」
			FilterChain filterChain) throws ServletException, IOException {

//		前端請求標頭(Header)=伺服器傳遞使用者的身份驗證憑證.我是誰(Authorization)+Bearer持有人
//				Bearer=	只要誰持有，伺服器就讓他授權通過
		String authHeader = request.getHeader("Authorization");
//		
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
//		在請求還沒到達目的地（Controller/API 門口）之前攔下它
//			放行請求
			filterChain.doFilter(request,response);
			return;
		}
//		substring，取子字串，從第 7 個字元開始，取到最後
//		JwtService.parseToken() 需要的是純 token 字串，把 Bearer 一起傳進去會被判定為格式錯誤
		String token = authHeader.substring(7);
		try {
			Integer memberID =jwtService.getMemberID(token);
			String  role = jwtService.getRole(token);
//			建立一個「已驗證身分」的物件
			UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(
//							principal，這個人是誰
							memberID,
//							credentials憑證（密碼）。已經用 token 驗證過了，不需要
							null,
//							authorities	權限清單	有哪些角色
//							toUpperCase() 轉大寫
//		List.of(...) 包成清單，因為一個人可以有多個角色，SimpleGrantedAuthority 是一個權限
							List.of(new SimpleGrantedAuthority("ROLE_"+ role.toUpperCase())));
//			Authentication 這個介面代表「目前請求的使用者是誰」
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
		}catch(JwtException e){
//			包含簽章錯誤、過期、格式錯誤
			SecurityContextHolder.clearContext();
		}
//		交給下一個Filter，驗證 token
		filterChain.doFilter(request, response);
	}
}
