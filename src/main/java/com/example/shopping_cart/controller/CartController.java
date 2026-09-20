package com.example.shopping_cart.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopping_cart.dto.CartDTO;
import com.example.shopping_cart.dto.CartResponseDTO;

import com.example.shopping_cart.service.ICartService;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/cart") 
public class CartController {
	
	@Autowired
	private ICartService iCartService;

	
//	加入購物車	
	@PostMapping("/add")

	public String addToCart(@AuthenticationPrincipal Integer memberID,@RequestBody CartDTO cartDTO){
		iCartService.addToCart(memberID, cartDTO.getProductID());
		return "加入成功";
		
	}
	
//	移除商品	DELETE	/cart/remove
	@DeleteMapping("/remove")
	public List<CartResponseDTO> removeFromCart(@AuthenticationPrincipal Integer memberID,@RequestBody CartDTO cartDTO) {
		
		return iCartService.removeFromCart(memberID,cartDTO.getProductID());
	}
	
//	修改數量	PUT	/cart/update	
	@PutMapping("/update")
	public List<CartResponseDTO> updateQuantity(@AuthenticationPrincipal Integer memberID,@RequestBody CartDTO cartDTO) {
		
		
		return iCartService.updateQuantity(memberID, cartDTO.getProductID(),cartDTO.getCartQuantity());
	}
	
//	查看購物車	GET	/cart/{memberID}	
//	@PathVariable → 從網址路徑取值
//	{memberID} → 網址裡的變數
//	GET /cart/1
//              ↑ 這個 1 就是 memberID
	@GetMapping
	public List<CartResponseDTO> getCartItems(@AuthenticationPrincipal Integer memberID) {
//		回傳的是 Cart Entity，所以需要控制回傳給前端的東西
		return iCartService.getCartItems(memberID);
//		getCartItems → 查資料庫 + 轉換成 DTO
//		removeFromCart → 刪除 + 呼叫 getCartItems 拿最新清單
//		updateQuantity → 修改 + 呼叫 getCartItems 拿最新清單
	}	
}
