package com.example.shopping_cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopping_cart.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
