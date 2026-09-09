package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.reviews")
    List<Product> findAllWithReviews();

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.reviews WHERE p.id = :id")
    Optional<Product> findByIdWithReviews(Long id);
}