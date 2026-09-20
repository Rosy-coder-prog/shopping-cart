package com.example.shopping_cart.service;

import com.example.shopping_cart.dto.LoginResponseDTO;


public interface IMemberService {

//	註冊會員
//	登入會員
	
	void registerMember(String  mailbox,String  password,String membername);
	//回傳token和ID.名字.角色
	LoginResponseDTO loginMember(String  mailbox,String  password);
}
