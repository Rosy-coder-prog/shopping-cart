package com.example.shopping_cart.service;

import java.util.*;

import com.example.shopping_cart.dto.CartResponseDTO;



//	增加商品到購物車
//	取消購物車裡的商品
//	增加 商品數量
//	讀取購物車裡有什麼	
	public interface ICartService{
//		Interface 規格書，只說要做什麼，不說怎麼做
		void addToCart(Integer memberID,Integer productID);		
		void removeFromCart(Integer memberID,Integer productID);
		void updateQuantity(Integer memberID,Integer productID,Integer cartQuantity);
		//只要 memberID，回傳一整個清單
		//回傳購物車清單
		List<CartResponseDTO> getCartItems(Integer memberID);
	}

