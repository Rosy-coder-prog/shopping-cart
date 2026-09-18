package com.example.shopping_cart.dto;

public class MemberResponseDTO {
//避免重要資訊洩漏
	private Integer memberID;
	private String membername;
    private String role;
	
	
	public MemberResponseDTO() {
				
	}
	public MemberResponseDTO(Integer memberID,String membername,String role) {		
		this.memberID = memberID;
		this.membername = membername;
		this.role =role;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
    
	
	
	
	
}
