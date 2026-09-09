package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * SRP: holds only the supplementary/optional attributes of a product
 * (description, warranty, weight, dimensions, country of manufacture).
 * These are not needed by every query (e.g. the product list page), so
 * they are split out of {@link Product} into their own table.
 *
 * This is the INVERSE side of the 1:1 relationship - mappedBy = "detail"
 * tells Hibernate that Product.detail owns the foreign key (detail_id).
 */
@Entity
@Table(name = "product_details")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "product")
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String description;

    @Column
    private String warranty;

    @Column
    private Double weight;

    @Column
    private String dimensions;

    @Column(name = "manufactured_country")
    private String manufacturedCountry;

    @OneToOne(mappedBy = "detail")
    private Product product;
}
