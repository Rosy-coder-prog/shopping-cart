package com.example.shopping_cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.shopping_cart.model.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {

//	findAll() → 查全部
//	findById() → 用 ID 查
//	save() → 新增或更新
//	deleteById() → 刪除
}
