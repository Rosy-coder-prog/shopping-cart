package com.example.shopping_cart.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.shopping_cart.model.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {

//	findAll() → 查全部
//	findById() → 用 ID 查
//	save() → 新增或更新
//	deleteById() → 刪除
	
//	登入用信箱查會員，用信箱查會員，findById 做不到（主鍵是 memberID 不是 mailbox）
//	可能有、可能沒有，用 Optional
//	interface 的方法一定是 public
	 Optional<Member> findByMailbox(String  mailbox);
	
}
