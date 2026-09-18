package com.example.shopping_cart.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.shopping_cart.dto.ErrorResponse;

//攔截所有 Controller 丟出的例外
@RestControllerAdvice
public class GlobalExceptionHandler {
//	專門處理 BusinessException 這種例外
	@ExceptionHandler(BusinessException.class)
		public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e){
//		建立回傳格式
		ErrorResponse errorResponse = new ErrorResponse(e.getCode(),e.getMessage());
		// 回傳給前端，HTTP 狀態碼 = code，body = errorResponse
		return ResponseEntity.status(e.getCode()).body(errorResponse);
	}
	/*
	Service 丟出例外：
	throw new BusinessException(404, "商品不存在");
	        ↓
	GlobalExceptionHandler 攔截到：
	handleBusinessException(e)
	        ↓
	建立 ErrorResponse：
	{ code: 404, message: "商品不存在" }
	        ↓
	前端收到：
	HTTP 狀態碼 404
	{ "code": 404, "message": "商品不存在" }
	
	*/
	/*
	400	請求有問題	購物車是空的、商品名稱重複
	401	身份驗證失敗	密碼錯誤、未登入
	403	沒有權限	不是 admin
	404	找不到	商品不存在、會員不存在
	409	資料衝突	信箱已被註冊
	*/
}
