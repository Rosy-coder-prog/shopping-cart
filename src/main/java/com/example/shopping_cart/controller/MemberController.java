package com.example.shopping_cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopping_cart.dto.MemberDTO;
import com.example.shopping_cart.dto.MemberResponseDTO;
import com.example.shopping_cart.service.IMemberService;

@RestController
@RequestMapping("/Member")
public class MemberController {
	
	@Autowired
	private IMemberService iMemberService;
	
//	註冊會員
	@PostMapping("/register")
	public String registerMember(@RequestBody MemberDTO memberDTO) {
		iMemberService.registerMember(memberDTO.getMailbox(),memberDTO.getPassword() , memberDTO.getMembername());
		return "註冊成功";
	}
	
	
	
//	登入會員

	@PostMapping("/login")
	public MemberResponseDTO loginMember(@RequestBody MemberDTO memberDTO) {
		return	iMemberService.loginMember(memberDTO.getMailbox(), memberDTO.getPassword());
		
	}
	
}
