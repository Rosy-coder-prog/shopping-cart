package com.example.shopping_cart.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@Service
public class JwtService {
//	private 讓外界拿不到 secret
//	final 保證它在建構後不會被任何程式碼改掉
	
	private final SecretKey secretKey;
	private final long accessTokenExpiration;
//	static:屬於class，不屬於物件
	private static final String CLAIM_ROLE = "role";
	
	public JwtService(
//	@Value("${jwt.secret}")從application.properties讀取對應的值注入進來
//			名稱要跟設定檔完全一致
			@Value("${jwt.secret}") String secret,
	        @Value("${jwt.access-token-expiration}") long accessTokenExpiration) {		
		
//		Keys.hmacShaKeyFor(...)把byte陣列包成HMAC演算法用的金鑰物件。長度不足32bytes會在這裡拋例外
//secret.getBytes(standardCharsets.UTF_8)字串轉成 byte 陣列。指定UTF-8是為了確保在不同系統上結果一致
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.accessTokenExpiration=accessTokenExpiration;
	}
	
	public String generateAccessToken(Integer memberID,String role) {
		Date now= new Date();
		Date expiry = new Date(now.getTime() + accessTokenExpiration);
//		開始建立 token
		return Jwts.builder()
//				設定sub：這個token代表誰，JWT:payload
//	JWT標準欄位，規格要求是字串，所以把 Integer轉成 String。之後解析回來時要再轉回Integer
				.subject(String.valueOf(memberID))
//				自訂欄位，放角色，JWT:payload
				.claim(CLAIM_ROLE,role)
//				設定iat：簽發時間，JWT:payload
				.issuedAt(now)
//				設定exp：過期時間，JWT:payload
				.expiration(expiry)
//				用secretkey簽章，JWT:signature
				.signWith(secretKey)
//				組裝成 header.payload.signature 字串
				.compact();
				
	}
	
//	Claims是payload裡所有資料的集合	
//	Claims claims = parseToken(token);
//	String sub = claims.getSubject();              // "5"
//	String role = claims.get("role", String.class); // "user"
//	Date exp = claims.getExpiration();
	public Claims parseToken(String token) {
//		開始建立解析器
		return Jwts.parser()
//				指定用哪把 key 驗證簽章
				.verifyWith(secretKey)
//				建立解析器
				.build()
//				驗證並解析 ->重要
//				用 secretKey 對 header 和 payload 重新計算簽章，跟 token 附的比對
//				檢查 exp 是否已過期
//				都通過才回傳內容
				.parseSignedClaims(token)
//				取出 payload 部分
				.getPayload();
		
		
	}
//	parseToken 回傳的是整包 Claims，但呼叫的地方通常只要 memberID 或 role
	public Integer getMemberID(String token) {
//		用 Integer.valueOf(...) 轉回 Integer
//		sub，型別是 String
//		呼叫端可以這樣寫Integer memberID = jwtService.getMemberID(token);
//		不用Integer memberID = Integer.valueOf(jwtService.parseToken(token).getSubject());
		return Integer.valueOf(parseToken(token).getSubject());
	}
	
	public String getRole(String token) {
//		get("role", String.class) 取自訂欄位。第二個參數告訴 jjwt「我預期這是 String」，
//		型別不符會拋例外。如果用沒有型別參數的 get("role")，回傳的是 Object，還要自己轉型
		return parseToken(token).get(CLAIM_ROLE,String.class);
	}
}
