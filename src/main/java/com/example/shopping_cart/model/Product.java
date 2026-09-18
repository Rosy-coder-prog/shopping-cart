package com.example.shopping_cart.model;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "productID")
	private Integer productID;
	
/*	false → 正常商品，前台看得到
	true ->軟刪除， 已刪除（前台看不到，但資料還在）
	*/
	@Column(name="deleted")
	private Boolean deleted =false;
	
	@Column(name = "productname")
	private String productname;
	
	@Column(name = "price")
	private BigDecimal price;
	
	@Column(name = "inventory_quantity")
	private Integer inventoryQuantity;
	
	public Product() {
		
	}
	//建立新盒子可以對應到的值
//	商品會一直保持false，除非要軟刪除才會改值
	public Product(Integer productID,String productname,BigDecimal price,Integer inventoryQuantity) {
		this.productID = productID;
		this.productname =productname;
		this.price = price;
		this.inventoryQuantity = inventoryQuantity;
	}
	//取得我自己值，只需打方法名稱，不需傳入甚麼參數
	public Integer getProductID() {
		return this.productID;
	}
	public void setProductID(Integer productID) {
		this.productID = productID; //外面傳進來的值變成我的值
	}
	
	public String getProductname() {
		return this.productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	
	public BigDecimal getPrice() {
		return this.price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public Integer getInventoryQuantity() {
		return this.inventoryQuantity;
	}
	public void setInventoryQuantity(Integer inventoryQuantity) {
		this.inventoryQuantity = inventoryQuantity;
	}
//	這邊是用get 不是is
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
	
	
}
