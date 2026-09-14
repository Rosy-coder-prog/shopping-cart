package com.example.shopping_cart.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopping_cart.model.Product;
import com.example.shopping_cart.repository.ProductRepository;

@Service
public class ProductService implements IProductService {

	@Autowired
	private ProductRepository productRepository;
	
	//查看所有商品
	
	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}
	
	//查看單一商品	

	@Override
	public Product getProductById(Integer productID) {
		
//		Optional 就是一個「可能有、可能沒有」盒子
		Optional<Product> result =productRepository.findById(productID);
		//先找錯誤
		//看有沒有這個商品
		if(!result.isPresent()) {
			throw new RuntimeException("沒有此商品");			
		}
		//有，拿出來
		return result.get();
	}

}
