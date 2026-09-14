package com.example.shopping_cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopping_cart.model.Cart;
import com.example.shopping_cart.model.CartId;

public interface CartRepository extends JpaRepository<Cart,CartId> {

//	findBy	我要查詢
//	Id	用 Cart 裡面的cartId 屬性（cartId）
//	MemberID	再用 CartId 裡面的 memberID 欄位
//	Spring 不會看參數名稱，它只看方法名稱來決定產生什麼 SQL
//	用意是因為主鍵cartId裡面有兩個參數，你需要哪個要寫出來
//	findBycartIdMemberID =SELECT * FROM cart WHERE memberID = 1
	
//	對應的是資料庫
	public List<Cart>  findBycartIdMemberID(Integer memberID);
	
//	清空購物車
	void deleteByCartIdMemberID(Integer memberID);
}
