package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Product;
import com.example.demo.model.Review;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.strategy.DiscountContext;

/**
 * SRP: this class knows business logic only (CRUD orchestration + price
 * calculation). It has no idea it is being called from an HTTP
 * controller - that separation is what ProductController is for.
 *
 * DIP: depends on the ProductRepository / ReviewRepository interfaces and
 * on DiscountContext, all injected through the constructor - never
 * instantiated with `new` inside this class.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final DiscountContext discountContext;

    public ProductService(ProductRepository productRepository,
                           ReviewRepository reviewRepository,
                           DiscountContext discountContext) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.discountContext = discountContext;
    }

    // ── CRUD ──

    public List<Product> findAll() {
        return productRepository.findAllWithReviews();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findByIdWithReviews(id);
    }

    @Transactional
    public Product save(Product product) {
        // Keep the 1:1 relationship consistent in both directions before persisting.
        if (product.getDetail() != null) {
            product.getDetail().setProduct(product);
        }
        return productRepository.save(product);
    }

    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    // ── Reviews (1:N) ──

    @Transactional
    public Product addReview(Long productId, Review review) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
        product.addReview(review);
        return productRepository.save(product);
    }

    public List<Review> findReviewsByProduct(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    // ── Strategy Pattern usage ──

    /** Delegates to DiscountContext so ProductService never knows *how* a discount is computed. */
    public double calculateFinalPrice(Product product) {
        if (product.getPrice() == null) {
            return 0.0;
        }
        return discountContext.calculatePrice(product.getDiscountType(), product.getPrice());
    }
}
