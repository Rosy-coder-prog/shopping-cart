package com.example.shopping_cart.dto;

public class MemberResponseDTO {

	private Integer memberID;
	private String membername;

	
	
	public MemberResponseDTO() {
				
	}
	public MemberResponseDTO(Integer memberID,String membername) {		
		this.memberID = memberID;
		this.membername = membername;
	}
	
	public Integer getMemberID() {
		return memberID;
	}
	public void setMemberID(Integer memberID) {
		this.memberID = memberID;
	}
	public String getMembername() {
		return membername;
	}
	public void setMembername(String membername) {
		this.membername = membername;
	}

	
	
	
}
