package com.example.shopping_cart.dto;

import java.math.BigDecimal;

public class ProductDTO {

	private Integer productID;
	private String productname;
	private BigDecimal price;
	private Integer inventoryQuantity;
	
	public ProductDTO() {
		
	}

	public ProductDTO(Integer productID, String productname, BigDecimal price, Integer inventoryQuantity) {		
		this.productID = productID;
		this.productname = productname;
		this.price = price;
		this.inventoryQuantity = inventoryQuantity;
	}

	public Integer getProductID() {
		return productID;
	}

	public void setProductID(Integer productID) {
		this.productID = productID;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getInventoryQuantity() {
		return inventoryQuantity;
	}

	public void setInventoryQuantity(Integer inventoryQuantity) {
		this.inventoryQuantity = inventoryQuantity;
	}
	
	
}
