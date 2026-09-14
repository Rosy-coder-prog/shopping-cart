package com.example.shopping_cart.model;



import java.io.Serializable;
import jakarta.persistence.Embeddable;


@Embeddable //兩個欄位組合成主鍵，JPA 不能只用 @Id，需要額外建一個主鍵類別
public class CartId implements Serializable{
	
	
	
	private static final long serialVersionUID = 1L;
	
		private Integer memberID;
		private Integer productID;
		
		public CartId() {
			
		}
		
		public CartId(Integer memberID ,Integer productID) {
			this.memberID = memberID;
			this.productID = productID;
		}
		
		public Integer getMemberID() {
			return this.memberID;
		}
		public void setMemberID(Integer memberID) {
			this.memberID = memberID;
		}
		
		public Integer getProductID() {
			return this.productID;
		}
		public void setProductID(Integer productID) {
			this.productID = productID;
		}
		
	}

