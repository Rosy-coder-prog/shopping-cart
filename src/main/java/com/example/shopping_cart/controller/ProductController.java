package com.example.shopping_cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopping_cart.model.Product;
import com.example.shopping_cart.service.IProductService;

@RestController
@RequestMapping("/Product")
public class ProductController {

	@Autowired
	private IProductService iProductService;
	
	
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
}
