package com.example.shopping_cart.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class OrderItemId  implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private Integer orderID;
	private Integer productID;
	
	public OrderItemId() {
		
	}
	public OrderItemId(Integer orderID,Integer productID) {
		this.orderID=orderID;
		this.productID=productID;
	}
	public Integer getOrderID() {
		return orderID;
	}
	public void setOrderID(Integer orderID) {
		this.orderID = orderID;
	}
	public Integer getProductID() {
		return productID;
	}
	public void setProductID(Integer productID) {
		this.productID = productID;
	}
	
	

}
