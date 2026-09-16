package com.example.shopping_cart.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDTO {
/*	
//	結帳回傳

	訂單編號
	總金額
	訂單狀態
	下單時間
	明細清單
	*/
	private Integer orderID;
	private BigDecimal amount;
	private String orderStatus;
	private LocalDateTime orderTime;
	private List<OrderItemResponseDTO> items;
	
	
	public OrderResponseDTO() {
		
	}


	

	public OrderResponseDTO(Integer orderID, BigDecimal amount, String orderStatus, LocalDateTime orderTime,
			List<OrderItemResponseDTO> items) {
		this.orderID = orderID;
		this.amount = amount;
		this.orderStatus = orderStatus;
		this.orderTime = orderTime;
		this.items = items;
	}




	public Integer getOrderID() {
		return orderID;
	}


	public void setOrderID(Integer orderID) {
		this.orderID = orderID;
	}


	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public String getOrderStatus() {
		return orderStatus;
	}


	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}


	public LocalDateTime getOrderTime() {
		return orderTime;
	}


	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
	}
	
	public List<OrderItemResponseDTO> getItems() {
		return items;
	}




	public void setItems(List<OrderItemResponseDTO> items) {
		this.items = items;
	}


	
	
	

}
