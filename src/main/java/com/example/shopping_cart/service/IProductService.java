package com.example.shopping_cart.service;

import java.util.List;

import com.example.shopping_cart.model.Product;

public interface IProductService {
	
//前台
//查看所有商品
//查看單一商品
	
	List<Product> getAllProducts();
	
	Product getProductById(Integer productID);
	

}
