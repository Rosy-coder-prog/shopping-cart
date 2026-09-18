package com.example.shopping_cart.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // 告訴 Hibernate 這是一個資料表對應的類別
@Table(name = "Member") // 指定對應的表名
public class Member {

	@Id // PRIMARY KEY，一張表只有一個主鍵
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "memberID") // 欄位資料對應
	private Integer memberID;
	// Integer 數字 12312313

	/*
	 * true → 正常會員（可以登入）
	 * false → 已停權（不能登入，但資料還在）
	 */
	@Column(name = "active")
	private Boolean active = true;

	@Column(name = "mailbox")
	private String mailbox;
	// String 文字
	@Column(name = "password")
	private String password;
	// 當new出Member物件空盒子需要這個
	@Column(name = "membername")
	private String membername;

	@Column(name = "role")
	private String role;

	public Member() {

	}

	// 建立Member空盒子可以直接對應存值
	//新會員註冊的時候，active 永遠是 true，要更動時自己在改值
	public Member(Integer memberID, String mailbox, String password, String membername, String role) {
		this.memberID = memberID;
		this.mailbox = mailbox;
		this.password = password;
		this.membername = membername;
		this.role = role;
	}

	// this 就是 ->我自己的物件
	public Integer getMemberID() {
		return this.memberID;
	}

	public void setMemberID(Integer memberID) {
		this.memberID = memberID;// 我的物件 = 外面傳進來的物件
	}

	public String getMailbox() {
		return this.mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMembername() {
		return membername;
	}

	public void setMembername(String membername) {
		this.membername = membername;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
