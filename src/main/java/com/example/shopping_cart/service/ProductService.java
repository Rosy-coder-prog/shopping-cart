package com.example.shopping_cart.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopping_cart.exception.BusinessException;
import com.example.shopping_cart.model.Product;
import com.example.shopping_cart.repository.ProductRepository;

@Service
public class ProductService implements IProductService {

	@Autowired
	private ProductRepository productRepository;

	
	/*
	  前台
	 */
	// 查看所有商品

	@Override
	public List<Product> getAllProducts() {
		return productRepository.findByDeletedFalse();
		
//		全撈出來，效能差
//	return	productRepository.findAll().stream()
//			.filter(p -> !p.getDeleted())//過濾掉Deleted=true
//			.collect(Collectors.toList());
//		
	}

	
	// 查看單一商品

	@Override
	public Product getProductById(Integer productID) {

//		Optional 就是一個「可能有、可能沒有」盒子
		Optional<Product> result = productRepository.findById(productID);
		// 先找錯誤
		// 看有沒有這個商品
		if (!result.isPresent()) {
			throw new BusinessException(404,"沒有此商品");
		}
				
		Product product  =result.get();
//		if (!product.getDeleted()) → 「如果沒下架」→ 正常商品被擋掉
		if(product.getDeleted()) {
			throw new BusinessException(404,"商品已下架");
		}
		
		return product;
	}
	
	
	
	

	/*
    後台
	 */
	
	//查看商品
	public Product getProductByIdForAdmin(Integer productID){
		Optional<Product> result   = productRepository.findById(productID);
		if (!result.isPresent()) {
			throw new BusinessException(404,"沒有此商品");
		}
		return result.get();
	}

	// 新增商品
	@Override
	public Product addProduct(String productname, BigDecimal price, Integer inventoryQuantity) {

//		先擋名稱一樣的
		Optional<Product> checkproductname = productRepository.findByProductname(productname);
		if (checkproductname.isPresent()) {
			throw new BusinessException(400,"商品名稱已存在");
		}
//		建立物件存值
		Product product = new Product();
		product.setInventoryQuantity(inventoryQuantity);
		product.setPrice(price);
		product.setProductname(productname);
//		存進資料庫
		productRepository.save(product);

		return product;

	}

	// 修改商品
	@Override
	public Product updateProduct(Integer productID, String productname, BigDecimal price, Integer inventoryQuantity) {
//		檢查有沒有這個商品
		Optional<Product> checkproduct = productRepository.findById(productID);
		if (!checkproduct.isPresent()) {
			throw new BusinessException(404,"沒有此商品");
		}

		Product product = checkproduct.get();
		
		
		product.setInventoryQuantity(inventoryQuantity);
		product.setPrice(price);
		product.setProductname(productname);
//檢查名稱是否有重複，並且排除自己
		Optional<Product> checkproductname = productRepository.findByProductname(productname);
//		拿出來比對ID
		if (checkproductname.isPresent() && !checkproductname.get().getProductID().equals(productID)) {
			throw new BusinessException(400,"商品名稱已存在");
			
		}
		productRepository.save(product);

		return product;
		
	}

	// 刪除商品(軟刪除)
	@Override
	public void deleteProduct(Integer productID) {
		
		Optional<Product> checkproduct = productRepository.findById(productID);
		if (!checkproduct.isPresent()) {
			throw new BusinessException(404,"沒有此商品");
		}
		
		Product product = checkproduct.get();
		
		product.setDeleted(true);
		productRepository.save(product);
		
		

	}

}
