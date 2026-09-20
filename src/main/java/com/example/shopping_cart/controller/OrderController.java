package com.example.shopping_cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopping_cart.dto.OrderResponseDTO;
import com.example.shopping_cart.service.IOrdersService;


@RestController
@RequestMapping("/api/order")
public class OrderController {
	
	@Autowired
	private IOrdersService iOrdersService;

	//結帳
	@PostMapping("/checkout")
	public OrderResponseDTO check(@AuthenticationPrincipal Integer memberID) {
		return iOrdersService.checkout(memberID);
	}
}
