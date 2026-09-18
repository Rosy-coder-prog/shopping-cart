package com.example.shopping_cart.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.shopping_cart.model.Product;

public interface IProductService {
	
//前台使用者
//查看所有商品
//查看單一商品
	
	List<Product> getAllProducts();
	
	Product getProductById(Integer productID);
	
//後台管理者
	//新增商品
	//修改商品
	//刪除商品
	
	//SQL表Integer productID會自動新增
	Product addProduct(String productname,BigDecimal price,Integer inventoryQuantity);
	
	Product updateProduct(Integer productID,String productname,BigDecimal price,Integer inventoryQuantity);
	
	void deleteProduct(Integer productID);
	
	
}
