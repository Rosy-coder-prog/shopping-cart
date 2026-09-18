package com.example.shopping_cart.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopping_cart.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	//找(不知道有沒有)商品名稱
	Optional<Product> findByProductname(String productname);
		
//	搜尋還上架(false)的商品	
	List<Product> findByDeletedFalse();
}
