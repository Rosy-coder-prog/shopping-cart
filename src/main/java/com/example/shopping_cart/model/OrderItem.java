package com.example.shopping_cart.model;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Order_item")
public class OrderItem {
	
	@EmbeddedId
	private OrderItemId orderItemId;
	
	@ManyToOne
	@JoinColumn(name = "orderID",insertable = false, updatable = false )
	private Orders orders;
	
	@ManyToOne
	@JoinColumn(name = "productID",insertable = false,updatable = false)
	private Product product;
	
	@Column(name = "order_quantity")
	private Integer orderQuantity;
	
	@Column(name = "purchase_price")
	private BigDecimal purchasePrice;
	
	public OrderItem() {
		
	}
	
	public OrderItem(OrderItemId orderItemId,Orders orders,
			Product product,Integer orderQuantity,BigDecimal purchasePrice) {
		this.orderItemId =orderItemId;
		this.orders = orders;
		this.product =product;
		this.orderQuantity= orderQuantity;
		this.purchasePrice=purchasePrice;
	}

	public OrderItemId getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(OrderItemId orderItemId) {
		this.orderItemId = orderItemId;
	}

	public Orders getOrders() {
		return orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	
}
