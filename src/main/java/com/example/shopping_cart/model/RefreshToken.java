package com.example.shopping_cart.model;



import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

//Entity 裡每個欄位都自動對應到資料表的一個欄位，不寫 @Column 也會對應
@Entity
@Table(name = "RefreshToken")
public class RefreshToken {
	
//	刷新憑證編號
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "refreshTokenID")
	private Integer refreshTokenID;
	
//	fetch = FetchType.LAZY->查RefreshToken的時候，要不要順便把關聯的Member也查出來。EAGER（積極）/LAZY（延遲）
//	@ManyToOne的預設是EAGER，所以要特別寫LAZY。一般建議關聯都設LAZY，需要時再查，這是 JPA 的常見最佳實踐
	@ManyToOne(fetch = FetchType.LAZY)
//	name = "xxx"->	欄位名稱跟資料表不同時指定
	@JoinColumn(name = "memberID",nullable = false)
	private Member member;
	
//	憑證雜湊值
//	nullable = false->不能是 null
//	unique = true->	不能重複
//	length = 64	字串長度
	@Column(name = "tokenHash", nullable = false ,unique = true,length = 64)
	private String tokenHash;
	
	//到期時間
//	Instant	時間軸上的一個絕對時刻
	@Column(name ="expiresAt",nullable = false)
	private Instant expiresAt;
	
	//是否作廢
//	有setter的話，任何程式碼都能 setRevoked(false) 把作廢的token救回來
	@Column(name ="revoked",nullable = false)
	private boolean revoked =false;
	
	//創建時間
//	updatable = false	新增後不能被 UPDATE 改掉
	@Column(name ="createdAt",nullable = false,updatable = false)
	private Instant createdAt = Instant.now();

//	protected 讓Hibernate用得到（它透過反射存取)，一般程式碼卻不能隨便呼叫
	protected RefreshToken() {
	
	}

//	refreshTokenID	資料庫自動編號（IDENTITY）
//	revoked	新的 token 一定是 false，欄位宣告時已給 = false
//	createdAt	一定是「現在」，欄位宣告時已給 = Instant.now()s
	public RefreshToken(Member member, String tokenHash, Instant expiresAt) {
		this.member = member;
		this.tokenHash = tokenHash;
		this.expiresAt = expiresAt;
	}

//	revoked變成true的方法
//	作廢是單向的：一旦作廢就不該復原
	public void revoke() {
		this.revoked = true;
	}
//	Boolean（包裝類別）/	boolean（基本型別），能null/不能null
//	是不是過期了
//	Instant.now()	現在這個時刻
//	.isAfter(expiresAt)	「現在」是不是在「到期時間」之後
	public boolean isExpired() {
//		現在是不是已經超過到期時間了
//		isAfter 本身就回傳 true 或 false
		return Instant.now().isAfter(expiresAt);
	}
//不用set存值，會被亂改
	public Integer getRefreshTokenID() {
		return refreshTokenID;
	}
//	用set代表把這張 refresh token 轉給另一個人
	public Member getMember() {
		return member;
	}
	public Instant getExpiresAt() {
		return expiresAt;
	}	
	public boolean isRevoked() {
		return revoked;
	}
	public String getTokenHash() {
		return tokenHash;
	}
	public Instant getCreatedAt() {
		return createdAt;
	}

}
