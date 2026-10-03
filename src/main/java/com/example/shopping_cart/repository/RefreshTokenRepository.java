package com.example.shopping_cart.repository;


import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.shopping_cart.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
	
//	有沒有這個TokenHash
//	SELECT * FROM RefreshToken WHERE tokenHash = ?
	Optional<RefreshToken> findByTokenHash(String tokenHash);

	//查詢不是作廢(還有效)
//	SELECT * FROM RefreshToken WHERE memberID = ? AND revoked = 0
//	Member_MemberID	member 欄位的 memberID（_ 代表進入關聯物件）
	@Query("SELECT r FROM RefreshToken r WHERE r.member.memberID = :memberID AND r.revoked = FALSE")
	List<RefreshToken> findByMember_MemberIDAndRevokedFalse(Integer memberID);

//	刪除到期時間在「現在」之前的紀錄
//	DELETE FROM RefreshToken WHERE expiresAt < ?
	void deleteByExpiresAtBefore(Instant now);
}
