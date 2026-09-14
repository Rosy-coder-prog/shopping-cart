package com.example.shopping_cart.dto;

import java.math.BigDecimal;



public class OrderItemResponseDTO {
/*
	商品名稱
	數量
	當下單價
	不需要 OrderItemId，是因為它就是包含在訂單裡了，不需要知道它ID
	*/
	
	private String productname;
	private Integer orderQuantity;
	private BigDecimal purchasePrice;
	
	
	
	public OrderItemResponseDTO() {
		
	}

	public OrderItemResponseDTO( String productname, Integer orderQuantity,
			BigDecimal purchasePrice) {		
		this.productname = productname;
		this.orderQuantity = orderQuantity;
		this.purchasePrice = purchasePrice;
	}

	

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Integer getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	
	
	
}
