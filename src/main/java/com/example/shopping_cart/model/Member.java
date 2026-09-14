package com.example.shopping_cart.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity //告訴 Hibernate 這是一個資料表對應的類別
@Table(name = "Member") //指定對應的表名
public class Member {

	
	@Id //PRIMARY KEY，一張表只有一個主鍵
	@GeneratedValue
	@Column(name = "memberID") //欄位資料對應
	private Integer memberID;
	//Integer 數字 12312313
	
	@Column(name = "mailbox")
	private  String  mailbox;
	//String 文字
	@Column(name = "password")
	private  String  password;
	//當new出Member物件空盒子需要這個
	public Member() {
	
	}
	//建立Member空盒子可以直接對應存值
	public Member(Integer memberID, String  mailbox, String  password){
		this.memberID = memberID;
		this.mailbox = mailbox;
		this.password = password;
		
	}
	//this 就是 ->我自己的物件
	public Integer getMemberID() {
		 return this.memberID; 
	}
	public void setMemberID(Integer memberID) {
		this.memberID = memberID;//我的物件 = 外面傳進來的物件
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
	
	
}
