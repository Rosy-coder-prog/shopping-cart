package com.example.shopping_cart.security;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenCleanupScheduled {

	private final RefreshTokenService refreshTokenService;

	public TokenCleanupScheduled(RefreshTokenService refreshTokenService) {
		this.refreshTokenService = refreshTokenService;
	}
	
//	讓Spring在指定時間自動呼叫這個方法
//	秒 分 時 日 月 星期
//	0  0  3  *  *  *
	@Scheduled(cron = "0 0 3 * * *")
	public void cleanup() {
		refreshTokenService.cleanUpExpiredToken();
	}
	
}
