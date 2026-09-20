package com.example.shopping_cart.controller;


import java.util.List;


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
import com.example.shopping_cart.model.Product;
import com.example.shopping_cart.service.IProductService;

@RestController
@RequestMapping("/api/product")
public class ProductController {

	
	@Autowired
	private IProductService iProductService;


	
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
//	已經由 hasRole("ADMIN") 確認
	@PostMapping("/add")
	public Product getAddProduct(@RequestBody ProductDTO productDTO) {
		
//		從DTO取值
		return iProductService.addProduct(
				productDTO.getProductname(),
				productDTO.getPrice(),
				productDTO.getInventoryQuantity()
				);
	}
	
	
	
	//修改商品
//	已經由 hasRole("ADMIN") 確認
	@PutMapping("/update")
	public Product getUpdateProduct (@RequestBody ProductDTO productDTO) {

		
      
      return iProductService.updateProduct(
//    		  拿出DTO裡面的參數
    		  productDTO.getProductID(),
    		  productDTO.getProductname(),
    		  productDTO.getPrice(),
    		  productDTO.getInventoryQuantity()
    		  );
		
	}
	
	//刪除商品
//	已經由 hasRole("ADMIN") 確認
	@DeleteMapping("/delete/{productID}")
	public void getDeleteProduct(@PathVariable Integer productID) {
		
		
//		使用void不用return
		 iProductService.deleteProduct(productID);
	}
	
}
