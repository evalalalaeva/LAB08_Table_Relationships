package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ProductDetail;

/** ISP: a separate, small interface just for ProductDetail persistence. */
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
}
