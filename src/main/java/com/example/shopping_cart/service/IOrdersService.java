package com.example.shopping_cart.service;

import com.example.shopping_cart.dto.OrderResponseDTO;



public interface IOrdersService {
//	1. 把購物車裡的商品變成一筆「訂單」（orders）
//	2. 購物車裡每個商品變成「訂單明細」（order_item）
//	3. 每個商品的「庫存」要扣掉
//	4. 結帳完購物車要「清空」

//	只需要知道誰在結帳
	OrderResponseDTO checkout(Integer memberID); 
		
	
}
