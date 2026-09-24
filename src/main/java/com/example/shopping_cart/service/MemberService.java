package com.example.shopping_cart.service;


import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.shopping_cart.dto.LoginResponseDTO;
import com.example.shopping_cart.dto.MemberResponseDTO;
import com.example.shopping_cart.exception.BusinessException;
import com.example.shopping_cart.model.Member;
import com.example.shopping_cart.repository.MemberRepository;
import com.example.shopping_cart.security.JwtService;
import com.example.shopping_cart.security.RefreshTokenService;

@Service
public class MemberService implements IMemberService {
	
//	private final注入後不能被改
	/*
	 * 用final不讓值被改
	 * 可以不用資料庫就單元測試
	 * 沒賦值就會報錯
	 */
	private final MemberRepository memberRepository;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenService refreshTokenService;
	
    public MemberService(MemberRepository memberRepository,
    		             JwtService jwtService,
    		             PasswordEncoder passwordEncoder,
    		             RefreshTokenService refreshTokenService) {
    	this.memberRepository =memberRepository;
    	this.jwtService =jwtService; 
    	this.passwordEncoder=passwordEncoder;
    	this.refreshTokenService=refreshTokenService;
    }
	
	
//	註冊會員
	@Override
	public void registerMember(String mailbox, String password,String membername) {
//		先查信箱有沒有重複，有就擋掉，沒有就繼續
		
		Optional<Member>  result= memberRepository.findByMailbox(mailbox);
//		存在擋掉
		if(result.isPresent()) {
			throw new BusinessException(409,"信箱已被註冊");
		}
//		暱稱錯誤排除
//		擋住壞的用 ||：是這個或那個 → 擋，放行好的用 &&：是這個而且那個 → 放
//		&&是要兩個都是true才會執行，代表出現空白或空字串就會被放行
//		null 檢查永遠放左邊，左邊 == null 是 true，|| 已經確定整個結果是 true，右邊不執行
		if(membername == null || membername.isBlank()) {
			throw new BusinessException(400,"會員名稱不能為空");
		}
		
//		建立一個Member物件取值
		  Member newmember = new Member();
		  newmember.setMailbox(mailbox);
		  newmember.setMembername(membername);
		  newmember.setPassword(passwordEncoder.encode(password));
		  //後端還是要給值
		  newmember.setRole("user");
		  memberRepository.save(newmember);		
	}
	
//	登入會員
	@Override
	public LoginResponseDTO loginMember(String mailbox, String password) {
		
//		驗證身分 → 產生憑證 → 包裝回應
		
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
//      檢查密碼，第一個是使用者輸入的明碼，第二個是資料庫的雜湊
//		matches 是 PasswordEncoder 介面的方法，用來比對明碼和雜湊是否相符
		if(!passwordEncoder.matches(password, member.getPassword())) {
			throw new BusinessException(401,"密碼錯誤");
		}
//		檢查會員狀態
//		if(member.getActive().equals(false))
//		if是true成立，停權的人是false，反轉會變成true
		if (!member.getActive()) {
			throw new BusinessException(403,"會員已停權");
		}
		
		
		
//		產生token
		String accessToken = jwtService.generateAccessToken(member.getMemberID()
				, member.getRole());
//		建立會員DTO
		MemberResponseDTO memberResponseDTO = new MemberResponseDTO(member.getMemberID(),
				member.getMembername(),member.getRole());	
		
		String rawRefreshToken = refreshTokenService.createRefreshToken(member);
//		傳給前端，只傳需要的值
		return new LoginResponseDTO(accessToken,memberResponseDTO,rawRefreshToken);
				
				
		
	}
	


}
