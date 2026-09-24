package com.example.shopping_cart.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

//	 record：自動有建構子、getter、equals、toString，而且只能讀，不能改和「有參數建構子」是綁在一起的
//回傳給前端的 DTO 不需要 setter，用 record 最適合
//	欄位名稱描述「它是什麼」
	
	public record LoginResponseDTO(String accessToken, 
			                       MemberResponseDTO member,
//		@JsonIgnore  轉JSON 時跳過這個欄位」，前端看不到，但 Controller 拿得到
			                       @JsonIgnore String rawRefreshToken) { 
		
	}
	
	
	


/*
public class LoginResponseDTO {
	private final String accessToken;
欄位名稱描述「它是什麼」
	private final MemberResponseDTO member;

	public LoginResponseDTO(String accessToken, MemberResponseDTO member) {
		this.accessToken = accessToken;
		this.member = member;
	}

	public String getAccessToken() { return accessToken; }
	public MemberResponseDTO getMember() { return member; }
}
*/