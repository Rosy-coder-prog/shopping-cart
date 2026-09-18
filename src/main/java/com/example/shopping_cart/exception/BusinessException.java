package com.example.shopping_cart.exception;

public class BusinessException extends RuntimeException {

//	code 欄位，用來存 HTTP 狀態碼（404、401、403）
	private int code;
	
	public BusinessException(int code,String message) {
		// 呼叫父類別 RuntimeException 的建構子
		// RuntimeException 本身就有存 message 的功能
		// 所以你不用自己寫 private String message
		// 用 getMessage() 就能拿到（從父類別繼承來的
		super(message); //把 message 傳給父類別 RuntimeException
		this.code =code;
	}
	
	public int getCode() {
		return code;
	}
//	不用 setter，因為錯誤就是 404，不會變，建立之後不改
}
