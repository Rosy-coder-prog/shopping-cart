package com.example.shopping_cart.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopping_cart.dto.MemberResponseDTO;
import com.example.shopping_cart.exception.BusinessException;
import com.example.shopping_cart.model.Member;
import com.example.shopping_cart.repository.MemberRepository;

@Service
public class MemberService implements IMemberService {
	


	@Autowired
	private MemberRepository memberRepository;
//	註冊會員
	@Override
	public void registerMember(String mailbox, String password,String membername) {
//		先查信箱有沒有重複，有就擋掉，沒有就繼續
		
		Optional<Member>  result= memberRepository.findByMailbox(mailbox);
//		存在擋掉
		if(result.isPresent()) {
			throw new BusinessException(409,"信箱已被註冊");
		}
		
//		建立一個Member物件取值
		  Member newmember = new Member();
		  newmember.setMailbox(mailbox);
		  newmember.setMembername(membername);
		  newmember.setPassword(password);
		  //後端還是要給值
		  newmember.setRole("user");
		  memberRepository.save(newmember);		
	}
	
//	登入會員
	@Override
	public MemberResponseDTO loginMember(String mailbox, String password) {
//		先查信箱有沒有這個註冊過信箱，有就擋掉，沒有就繼續
//		檢查密碼有沒有正確，錯誤擋掉，正確放行
		
//		Optional 
//		就是一個「可能有、可能沒有」盒子，用在方法裡面
		Optional<Member> result = memberRepository.findByMailbox(mailbox);
//		我直接看
		if(!result.isPresent()) {
			throw new BusinessException(401,"未註冊的信箱");
			
		}
//		取出來看	
		Member member = result.get();
//檢查密碼
		if(!member.getPassword().equals(password)) {
			throw new BusinessException(401,"密碼錯誤");
		}
//		檢查會員狀態
//		if(member.getActive().equals(false))
//		if是true成立，停權的人是false，反轉會變成true
		if (!member.getActive()) {
			throw new BusinessException(403,"會員已停權");
		}
//		傳給前端，只傳需要的值
		return new MemberResponseDTO(member.getMemberID(),member.getMembername(),member.getRole());
		
	}

}
