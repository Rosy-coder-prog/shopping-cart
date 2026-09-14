package com.example.shopping_cart.model;


import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Cart")
public class Cart {
	
	@EmbeddedId //兩個欄位組成的主鍵
	private CartId cartId;
	
	@ManyToOne
	//只是查資料的，不要重複管這個欄位
	@JoinColumn( name = "memberID",insertable = false, updatable = false )
	private Member member;
	
	@ManyToOne
	@JoinColumn( name = "productID",insertable = false,updatable = false)
	private Product product;
	
	@Column(name = "cart_quantity")
	private Integer cartQuantity;
	
	public Cart() {
		
	}
	
	public Cart(CartId cartId,Member member,Product product,Integer cartQuantity) {
		this.cartId=cartId;
		this.member = member;
		this.product = product;
		this.cartQuantity = cartQuantity;
	}
	
	public CartId getCartId() {
		return this.cartId;
	}
	public void setCartId(CartId id) {
		this.cartId = id;
	}
	
	public Member getMember() {
		return this.member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	
	public Product getProduct() {
		return this.product;
	}
    public void setProduct(Product product) {
    	this.product = product;
    }
    
    public Integer getCartQuantity() {
    	return this.cartQuantity;
    }
    public void setCartQuantity(Integer cartQuantity) {
    	this.cartQuantity = cartQuantity;
    }
    
    
}
