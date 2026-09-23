package com.example.shopping_cart.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.shopping_cart.model.Member;
import com.example.shopping_cart.model.RefreshToken;
import com.example.shopping_cart.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;
	private final  long refreshTokenExpiration;
	
	public RefreshTokenService(
			RefreshTokenRepository refreshTokenRepository,
//			讀設定擋@Value("${...}")
			@Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {
		this.refreshTokenRepository = refreshTokenRepository;
		this.refreshTokenExpiration =refreshTokenExpiration;
	}
	
	public String createRefreshToken(Member member) {
//		int[] numbers = new int[5];   5 個 int 的陣列，初始值全是 0
//		一個 byte 是 8 bits，32 個 byte = 256 bits，跟SHA-256輸出一樣長
//		byte 的範圍是 -128 到 127
//		建立一個能裝 32 個 byte 的空容器		
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
		
		try {
//			MessageDigest	Java 內建的雜湊工具 class
//			.getInstance("SHA-256")	「給我一個會算 SHA-256 的雜湊工具」
//			MessageDigest 是抽象類別，不能直接 new。getInstance 是它的靜態方法
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
//			rawToken.getBytes(StandardCharsets.UTF_8)	把字串轉成 byte 陣列
//	digest.digest(...)對這個 byte 陣列算 SHA-256，回傳雜湊結果（也是 byte 陣列)用這個雜湊工具，算出雜湊值
			byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
//			StringBuilder 是可以一直拼接的字串容器
			StringBuilder sb = new StringBuilder();
			for (byte b : hash) {
//			%02x：把每個byte轉成2位的十六進位，不足補 0，轉成十六進位，不滿 2 位就前面補
//				String.format把值按照指定格式轉成字串
//				sb.append(...)把轉好的 2 個字元加到 StringBuilder 後面
//				0	不夠位數時用 0 來補，2	最少要 2 位
				
				sb.append(String.format("%02x", b));
			}
//			toString() 是產生一個新的 String 物件
//			把它目前的內容複製成一個 String 回傳
			String tokenHash = sb.toString();
			Instant expiresAt = Instant.now().plusMillis(refreshTokenExpiration);
			RefreshToken refreshToken = new RefreshToken(member, tokenHash,expiresAt);
			refreshTokenRepository.save(refreshToken);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);			
		}
//		Instant.now()現在，plusMillis 加上毫秒數，refreshTokenExpiration設定擋的值		
        return rawToken;
	}
}
