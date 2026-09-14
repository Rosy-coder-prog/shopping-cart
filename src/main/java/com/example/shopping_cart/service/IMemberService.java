package com.example.shopping_cart.service;

import com.example.shopping_cart.dto.MemberResponseDTO;

public interface IMemberService {

//	註冊會員
//	登入會員
	
	void registerMember(String  mailbox,String  password,String membername);
	//登入只輸要帳號.密碼驗證，其他資料從Member物件取得
	MemberResponseDTO loginMember(String  mailbox,String  password);
}
