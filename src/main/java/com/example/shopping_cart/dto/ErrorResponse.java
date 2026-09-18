package com.example.shopping_cart.dto;


//回傳給前端
public class ErrorResponse {

	private int code;
	private String message;
	
	public ErrorResponse(int code, String message) {

		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
//	不用 setter，因為錯誤就是 404，不會變，訊息就是「商品不存在」
//	建立之後不改
	
}
