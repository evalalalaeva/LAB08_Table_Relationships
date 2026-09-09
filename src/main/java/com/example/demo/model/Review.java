package com.example.demo.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * SRP: represents a single customer review and nothing else.
 *
 * This is the OWNING ("Many") side of the 1:N relationship with Product:
 * the foreign key (product_id) always lives on the Many side, which is
 * why the @JoinColumn annotation is here rather than on Product.
 *
 * OCP in action: this table can grow independently (new reviews added at
 * any time) without ever requiring a change to the Product entity.
 */
@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "product")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String reviewer;

    @Column
    private Integer rating; // 1-5

    @Column(length = 1000)
    private String comment;

    @Column(name = "review_date")
    private LocalDate reviewDate;

    // FK always lives on the "Many" side
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
