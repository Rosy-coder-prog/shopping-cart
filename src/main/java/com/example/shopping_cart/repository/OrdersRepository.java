package com.example.shopping_cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopping_cart.model.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {

}
