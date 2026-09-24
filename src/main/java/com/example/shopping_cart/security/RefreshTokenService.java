package com.example.shopping_cart.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.shopping_cart.exception.BusinessException;
import com.example.shopping_cart.model.Member;
import com.example.shopping_cart.model.RefreshToken;
import com.example.shopping_cart.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {

//	final 要求建構子結束前必須賦值，之後不能改
	private final RefreshTokenRepository refreshTokenRepository;
	private final  long refreshTokenExpiration;
	
	public RefreshTokenService(
//			拿到資料庫操作工具
			RefreshTokenRepository refreshTokenRepository,
//			讀設定擋@Value("${...}")
//			@Value 讀進來的是一個 long 數字
			@Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {
		this.refreshTokenRepository = refreshTokenRepository;
		this.refreshTokenExpiration =refreshTokenExpiration;
	}
//	建立token
	public String createRefreshToken(Member member) {
//		int[] numbers = new int[5];   5 個 int 的陣列，初始值全是 0
//		一個 byte 是 8 bits，32 個 byte = 256 bits，跟SHA-256輸出一樣長
//		byte 的範圍是 -128 到 127
//		建立一個能裝 32 個 byte 的空容器	
		/*
		 *第一個 隨機值，用來產生 token，rawToken（Base64 字串）→ cookie
		 */
		byte[] bytes  =new byte[32];
//		SecureRandom：加密等級的亂數產生器
//		nextBytes 會把陣列裡的每個位置覆蓋成隨機值
		new SecureRandom().nextBytes(bytes);
/*		隨機的 byte 陣列裡有負數和不可見字元，不能直接當字串用
 *      0 到 31 這 32 個值叫做控制字元，它們不是給人看的，是給機器用的指令，叫「不可見字元」
 *      Base64 把它轉成只有英文、數字和少數符號的字串
 *      作用就是把這些「不能直接用的 byte」轉成「只有英文數字的字串
 *      Base64 的規則是：輸出長度必須是 4 的倍數，不夠就用 = 補
 */
//		 Base64，Java 內建的工具 class
//		Base64.getUrlEncoder()：轉成URL安全的字串（cookie裡不能有某些符號）
//		withoutPadding()：去掉結尾的 =，解碼時補回
//		.encodeToString(bytes) 把 byte 陣列編碼成字串
		String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
		
		String tokenHash = hashToken(rawToken);

//		Instant.now()現在，plusMillis 加上毫秒數，refreshTokenExpiration設定擋的值		
			Instant expiresAt = Instant.now().plusMillis(refreshTokenExpiration);
			RefreshToken refreshToken = new RefreshToken(member, tokenHash,expiresAt);
			refreshTokenRepository.save(refreshToken);
		

        return rawToken;
	}
	
	//驗證token
	public RefreshToken validateAndRotate(String rawToken) {
		String tokenHash = hashToken(rawToken);
		//有沒有這個token
		RefreshToken token = refreshTokenRepository.findByTokenHash(tokenHash)
//				找不到丟例外
				.orElseThrow(() -> new BusinessException(401,"無效的refresh Token"));
		//有異常登入
		if(token.isRevoked()) {
//			查詢有效
			List<RefreshToken> allTokens=refreshTokenRepository
					.findByMember_MemberIDAndRevokedFalse(token.getMember().getMemberID());
			for(RefreshToken t:allTokens) {
				t.revoke();
			}
			refreshTokenRepository.saveAll(allTokens);
			throw new BusinessException(401,"疑似異常存取，已登出所有裝置" );		
		}
		//過期
		if (token.isExpired()) {
			throw new BusinessException(401,"登入已過期，請重新登入");
		}
		//檢查會員是否停權
		    Member member = token.getMember();
		if (!member.getActive()) {
			throw new BusinessException(403,"會員已停權");
		}
//		作廢舊的
		token.revoke();
		refreshTokenRepository.save(token);
		return token;
	}
	
	//登出
	public void revokeByRawToken(String rawToken) {
		String tokenHash = hashToken(rawToken);
		Optional <RefreshToken> result = refreshTokenRepository.findByTokenHash(tokenHash);
	    if (result.isPresent()) {
	    	RefreshToken token = result.get();
	    	token.revoke();
	    	refreshTokenRepository.save(token);
		}
		
		
	}
	
	//共用方法
	private String hashToken(String rawToken) {
		try {
//			MessageDigest	Java 內建的雜湊工具 class
//			.getInstance("SHA-256")	「給我一個會算 SHA-256 的雜湊工具」
//			MessageDigest 是抽象類別，不能直接 new。getInstance 是它的靜態方法
			
//			放進 cookie 的 token
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
/*	getBytes 把字串轉成 byte 陣列，用 UTF-8 編碼規則
 *  Windows 預設編碼可能不是 UTF-8 ，不指定 UTF-8 ，
 *  可能部署到 Linux 的 Azure 上算的結果不一樣，直接指定同一個規則
	digest.digest(...)對這個 byte 陣列算 SHA-256，回傳雜湊結果（也是 byte 陣列)用這個雜湊工具，算出雜湊值
	第二個：SHA-256 的輸出，用來存資料庫，tokenHash（十六進位字串）→ 資料庫
 */
			byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
//			StringBuilder 是可以一直拼接的字串容器
			StringBuilder sb = new StringBuilder();
			for (byte b : hash) {
//	%02x：把每個byte轉成2位的十六進位，不足補 0，轉成十六進位，不滿 2 位就前面補
//	String.format把值按照指定格式轉成字串
//	sb.append(...)把轉好的 2 個字元加到 StringBuilder 後面
//	0	不夠位數時用 0 來補，2	最少要 2 位
//	byte 陣列不能直接存進資料庫的 VARCHAR 欄位。需要一種方式把它表示成純文字	
//	SHA-256 裡就代表輸出是 256 bits = 32 bytes， 1 Byte 可以拆成 2 個十六進位字元
//	每個 byte 固定 2 個字元，32 bytes 永遠是 64 字元。資料庫欄位設 VARCHAR(64) 剛剛好
				sb.append(String.format("%02x", b));
			}
//			toString() 是產生一個新的 String 物件
//			把它目前的內容複製成一個 String 回傳
			
//			 存進 RefreshToken 物件 → 存進資料庫
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
/*
 * 建立一個byte陣列裡面裝32個byte，利用安全隨機產生器覆蓋每個byte產生隨機值
 * 產生的隨機值會有不可見字元，宣告一個變數名稱，利用Base64(JAVA內建工具)，
 * 把不能直接用的byte轉成只有英文數字的字串，並且Base64輸出的長度必須是4的倍數，不足用=補
   接著用url編碼器轉成安全字串，因為cookie裡不能用某些符號，去掉=，最後把byte轉成字串
   
   宣告一個String變數名稱，使用MessageDigest(JAVA內建工具)裡面算SHA-256的雜湊工具
   把字串轉成 byte 陣列，用 UTF-8 編碼規則
   把轉成64字元的byte轉成String型別
   現在的時間秒加上七天的毫秒數
 */

