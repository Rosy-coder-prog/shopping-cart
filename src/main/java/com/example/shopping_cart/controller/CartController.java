package com.example.shopping_cart.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/Cart") //API 都以 /cart 開頭
public class CartController {
	
	@Autowired
	private ICartService iCartService;

	
//	加入購物車	POST	/cart/add
	@PostMapping("/add")
//	加入購物車不需回傳
	public String addToCart(@RequestBody CartDTO cartDTO){
		iCartService.addToCart(cartDTO.getMemberID(), cartDTO.getProductID());
		return "加入成功";
		
	}
	
//	移除商品	DELETE	/cart/remove
	@DeleteMapping("/remove")
	public List<CartResponseDTO> removeFromCart(@RequestBody CartDTO cartDTO) {
		
		return iCartService.removeFromCart(cartDTO.getMemberID(),cartDTO.getProductID());
	}
	
//	修改數量	PUT	/cart/update	
	@PutMapping("/update")
	public List<CartResponseDTO> updateQuantity(@RequestBody CartDTO cartDTO) {
		
		
		return iCartService.updateQuantity(cartDTO.getMemberID(), cartDTO.getProductID(),cartDTO.getCartQuantity());
	}
	
//	查看購物車	GET	/cart/{memberID}	
//	@PathVariable → 從網址路徑取值
//	{memberID} → 網址裡的變數
//	GET /cart/1
//              ↑ 這個 1 就是 memberID
	@GetMapping("/{memberID}")
	public List<CartResponseDTO> getCartItems(@PathVariable Integer memberID) {
//		回傳的是 Cart Entity，所以需要控制回傳給前端的東西
		return iCartService.getCartItems(memberID);
//		getCartItems → 查資料庫 + 轉換成 DTO
//		removeFromCart → 刪除 + 呼叫 getCartItems 拿最新清單
//		updateQuantity → 修改 + 呼叫 getCartItems 拿最新清單
	}	
}
