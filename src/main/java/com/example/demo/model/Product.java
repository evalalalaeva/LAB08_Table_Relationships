package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * SRP: Product only holds core catalogue data (name, category, brand,
 * stock, price, discountType). Supplementary data that is not needed on
 * every query is delegated to {@link ProductDetail} (1:1), and the
 * unbounded list of customer feedback is delegated to {@link Review}
 * (1:N) - see OCP note below.
 *
 * OCP: new child data (e.g. a future "Review" style entity) can be added
 * as its own table/entity without modifying Product itself, as long as
 * it owns the foreign key back to Product.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"detail", "reviews"}) // avoid recursive/lazy-loading toString loops
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String category;

    @Column
    private String brand;

    @Column
    private Integer stock;

    @Column
    private Double price;

    /** Used by the Strategy Pattern (DiscountContext) to pick a DiscountStrategy at runtime. */
    @Column(name = "discount_type")
    private String discountType;

    // ── 1:1 with ProductDetail ──
    // Product is the OWNER of this relationship: it holds the FK column (detail_id).
    // cascade = ALL so saving/deleting a Product also saves/deletes its ProductDetail.
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "detail_id", referencedColumnName = "id")
    private ProductDetail detail;

    // ── 1:N with Review ──
    // Product is the INVERSE side (mappedBy = "product"); Review owns the FK (product_id).
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    /** Keeps both sides of the bidirectional 1:N relationship in sync. */
    public void addReview(Review review) {
        reviews.add(review);
        review.setProduct(this);
    }

    public void removeReview(Review review) {
        reviews.remove(review);
        review.setProduct(null);
    }
}
