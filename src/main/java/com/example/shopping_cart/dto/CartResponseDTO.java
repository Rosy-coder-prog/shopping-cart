package com.example.shopping_cart.dto;

import java.math.BigDecimal;

public class CartResponseDTO {

//	控制前端接受的資料
	private Integer memberID;
	private Integer productID;	
	private Integer cartQuantity;
	private String productname;
	private BigDecimal price;
	
	public CartResponseDTO() {
		
	}
	public CartResponseDTO(Integer memberID,Integer productID,
			Integer cartQuantity,String productname,BigDecimal price) {	
		this.memberID =memberID;
		this.productID =productID;
		this.cartQuantity =cartQuantity;
		this.productname =productname;
		this.price =price;
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
	
	
	
}
