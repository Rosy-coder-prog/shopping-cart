package com.example.shopping_cart.controller;

import com.example.shopping_cart.repository.MemberRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopping_cart.dto.ProductDTO;
import com.example.shopping_cart.model.Member;
import com.example.shopping_cart.model.Product;
import com.example.shopping_cart.service.IProductService;

@RestController
@RequestMapping("/Product")
public class ProductController {

	
	@Autowired
	private IProductService iProductService;

	@Autowired
	private MemberRepository memberRepository;
	
	//前台
	//查看所有商品
	@GetMapping("/findall")
	public List<Product> getAllProducts(){
		return iProductService.getAllProducts();
	}
	//查看單一商品
	@GetMapping("/{productID}")
	public Product getProduct(@PathVariable Integer productID) {
		return iProductService.getProductById(productID);
	}
	
//	後台
	
	// 新增商品
	@PostMapping("/add/{memberID}")
	public Product getAddProduct(@PathVariable Integer memberID,@RequestBody ProductDTO productDTO) {
		//共用方法檢查權限
		checkAdmin(memberID);
//		從DTO取值
		return iProductService.addProduct(
				productDTO.getProductname(),
				productDTO.getPrice(),
				productDTO.getInventoryQuantity()
				);
	}
	
	
	
	//修改商品
//	查看購物車	Put	/update/{memberID}	
//	@PathVariable → 從網址路徑取值
//	{memberID} → 網址裡的變數
//	Put /update/1
//              ↑ 這個 1 就是 memberID
	@PutMapping("/update/{memberID}")
	public Product getUpdateProduct (@PathVariable Integer memberID,@RequestBody ProductDTO productDTO) {

		//共用方法檢查權限
		checkAdmin(memberID);
      
      return iProductService.updateProduct(
//    		  拿出DTO裡面的參數
    		  productDTO.getProductID(),
    		  productDTO.getProductname(),
    		  productDTO.getPrice(),
    		  productDTO.getInventoryQuantity()
    		  );
		
	}
	
	//刪除商品
	@DeleteMapping("/delete/{memberID}/{productID}")
	public void getDeleteProduct(@PathVariable Integer memberID,@PathVariable Integer productID) {
		
		checkAdmin(memberID);
//		使用void不用return
		 iProductService.deleteProduct(productID);
	}
	
	
	
	
	
	
//	後台共用方法(檢查會員和權限)
//	不用回傳，錯了會跳出錯誤
	private void checkAdmin(Integer memberID) {
//		檢查這個會員有沒有
	      Optional<Member>	memberResult = memberRepository.findById(memberID);
	      if(!memberResult.isPresent()) {
	    	  throw new RuntimeException("會員不存在");
	      }
//	      檢查角色權限
	      Member member = memberResult.get();
	      if(!"admin".equals(member.getRole())) {
	    	  throw new RuntimeException("角色沒有權限");   	  
	      }
	      
	}
}
