package com.example.shopping_cart.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopping_cart.dto.CartResponseDTO;
import com.example.shopping_cart.model.Cart;
import com.example.shopping_cart.model.CartId;
import com.example.shopping_cart.model.Product;
import com.example.shopping_cart.repository.CartRepository;
import com.example.shopping_cart.repository.ProductRepository;

@Service
//implements ICartService實作這份規格書
public class CartServiceImpl implements ICartService{
//	增加商品到購物車
//	取消購物車裡的商品
//	增加 商品數量
//	讀取購物車裡有什麼

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
//	增加商品到購物車
	@Override
	public void addToCart(Integer memberID,Integer productID) {
		/* 先查這個商品存不存在
		   檢查資料庫有沒有這個商品		
		*/
		//檢查購物車有沒有這個商品
		//如果有-> +1 ，如果沒有->新增一筆，數量設為1
		//存進資料庫
		
		//商品存不存在
		Optional<Product> result = productRepository.findById(productID);
//		isPresent() → 找到了嗎?
//		先把錯誤的情況處理掉
		if(!result.isPresent()) {
			throw new RuntimeException("商品不存在");		
		}
		//從容器取得商品
		Product takeProduct	=result.get();
		
		//CartId 的建構子接收的是兩個 Integer
		//傳進來的參數當成鑰匙
		CartId cartId=new CartId(memberID,productID);
		
//		購物車有沒有
		Optional<Cart> checkCart = cartRepository.findById(cartId);
		
//		有沒有找到這個商品
//		isPresent() → 透過窗戶看一眼「裡面有沒有東西」
//		get() → 打開門把東西拿出來，才能用它
		if(checkCart.isPresent()) {
			 // 從容器拿出購物車項目
			Cart takeCart =checkCart.get();
			
			takeCart.setCartQuantity(takeCart.getCartQuantity() +1);
			//存回資料庫
			cartRepository.save(takeCart);
		}else {
//			沒有找到建立一個購物車
			Cart newCart = new Cart();
			newCart.setCartId(cartId);
			newCart.setCartQuantity(1);
			cartRepository.save(newCart);
		}
	}

//	刪除購物車裡的商品
	@Override
	public List<CartResponseDTO> removeFromCart(Integer memberID,Integer productID) {
		CartId cartId=new CartId(memberID,productID);
		Optional<Cart> checkCart = cartRepository.findById(cartId);
//		一樣防呆，檢查購物車有沒有這個商品
		if(!checkCart.isPresent()) {
			throw new RuntimeException("購物車沒有此商品");
		}
		//刪除
		cartRepository.deleteById(cartId);
		//讀取最新的狀態		
		return getCartItems(memberID);
		
	}

//	修改商品數量(改成指定的數量)
	@Override
	public List<CartResponseDTO> updateQuantity(Integer memberID,Integer productID, Integer cartQuantity) {
		CartId cartId=new CartId(memberID,productID);
		Optional<Cart> checkCart = cartRepository.findById(cartId);
//		先處理錯誤
		if(!checkCart.isPresent()) {
			throw new RuntimeException("購物車沒有此商品");
		}
		
		Cart takeCart = checkCart.get();
//		存變數不加()，加了()變方法
		 takeCart.setCartQuantity(cartQuantity);
		
		cartRepository.save(takeCart);
		
		return getCartItems(memberID);
		
		
	}

//	讀取購物車裡有什麼
	@Override
	public List<CartResponseDTO> getCartItems(Integer memberID) {
//		宣告方法的時候要寫型態，呼叫的時候只傳值
		/*
		這邊建立盒子目的是為了傳前端需要的值，findByCartIdMemberID方法從會員ID讀取購物車有甚麼東西，
		並且它回傳的是List<Cart>，裡面有會員物件，所以我們要從List<Cart>挑我們需要的資料
		*/
		List<Cart> CartList = cartRepository.findBycartIdMemberID(memberID);
		
		//建立空清單
		List<CartResponseDTO> result = new ArrayList<>();
		
		for(Cart cart:CartList) {
//			直接取得，不用set
		CartResponseDTO cartResponseDTO = new CartResponseDTO(
				cart.getCartId().getMemberID(),
				cart.getCartId().getProductID(),
				cart.getCartQuantity(),
				cart.getProduct().getProductname(),
				cart.getProduct().getPrice()
				
				);
		
		result.add(cartResponseDTO);
		}
		
		
		
		return result;
	}

	
}

//CartResponseDTO dto = new CartResponseDTO();
//dto.setMemberID(cart.getCartId().getMemberID());
//dto.setProductID(cart.getCartId().getProductID());
//dto.setCartQuantity(cart.getCartQuantity());
//dto.setProductname(cart.getProduct().getProductname());
//dto.setPrice(cart.getProduct().getPrice());
