package com.example.shopping_cart.dto;



public class CartDTO {

//	前端送什麼，DTO 就長什麼樣
	private Integer memberID;
	private Integer productID;
	
	private Integer cartQuantity;
	
	public CartDTO() {
		
	}
	public CartDTO(Integer memberID,Integer productID,Integer cartQuantity) {
		this.memberID =memberID;
		this.productID = productID;
		this.cartQuantity = cartQuantity;
	}
	public Integer getMemberID() {
		return memberID;
	}
	public void setMemberID(Integer memberID) {
		this.memberID = memberID;
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
