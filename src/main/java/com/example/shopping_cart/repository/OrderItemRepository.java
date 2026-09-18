package com.example.shopping_cart.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopping_cart.model.OrderItem;
import com.example.shopping_cart.model.OrderItemId;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {

	
}
