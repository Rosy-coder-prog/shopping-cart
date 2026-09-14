package com.example.shopping_cart.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Orders")
public class Orders {
	
	@Id
	@GeneratedValue
	@Column(name = "orderID")
	private Integer orderID;
	
	@ManyToOne
	@JoinColumn(name = "memberID")
	private Member member; //關連到關聯到另一張表，應該是Member物件
	
	@Column(name ="amount")
	private BigDecimal amount;
	
	@Column(name = "order_status")
	private String orderStatus; //存的是文字（「處理中」「已完成」
	
	@Column(name ="order_time")
	private LocalDateTime orderTime; //當下時間
	
	public Orders() {
		
	}
	public Orders(Integer orderID,Member member,
			BigDecimal amount,String orderStatus,LocalDateTime orderTime) {
		this.orderID = orderID;
		this.member = member;
		this.amount =amount;
		this.orderStatus = orderStatus;
		this.orderTime = orderTime;
				
	}
	public Integer getOrderID() {
		return orderID;
	}
	public void setOrderID(Integer orderID) {
		this.orderID = orderID;
	}
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
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

	
}
