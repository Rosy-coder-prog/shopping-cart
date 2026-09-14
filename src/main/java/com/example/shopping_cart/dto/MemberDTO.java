package com.example.shopping_cart.dto;

public class MemberDTO {

	
	private  String  mailbox;
	private  String  password;
	private String membername;
	
	
	public MemberDTO() {
	}
	public MemberDTO(String mailbox, String password, String membername) {
		this.mailbox = mailbox;
		this.password = password;
		this.membername = membername;
	}
	public String getMailbox() {
		return mailbox;
	}
	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}
	public String getPassword() {
		return password;
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
	
	
	
	
}
