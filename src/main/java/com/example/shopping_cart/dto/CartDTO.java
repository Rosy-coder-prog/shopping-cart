package com.example.shopping_cart.dto;



public class CartDTO {

//	前端送什麼，DTO 就長什麼樣
	//前端送的 JSON 形狀跟 Entity 不一樣
	
	private Integer productID;
	
	private Integer cartQuantity;
	
	public CartDTO() {
		
	}
	public CartDTO(Integer productID,Integer cartQuantity) {
		
		this.productID = productID;
		this.cartQuantity = cartQuantity;
	}
	
	public Integer getProductID() {
		return productID;
	}
	public void setProductID(Integer productID) {
		this.productID = productID;
	}
	public Integer getCartQuantity() {
		return cartQuantity;
	}
	public void setCartQuantity(Integer cartQuantity) {
		this.cartQuantity = cartQuantity;
	}
	
	
}
