package com.example.demo.strategy;

import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * Strategy Pattern - Context class.
 *
 * DIP: depends only on the DiscountStrategy abstraction (and on Spring's
 * Map-of-beans injection), never on a concrete strategy class. Which
 * concrete strategy actually runs is decided at request time by the
 * Product's discountType field, not hard-coded here.
 */
@Component
public class DiscountContext {

    public static final String NONE = "NONE";
    public static final String MEMBER = "MEMBER";
    public static final String SEASONAL = "SEASONAL";

    private final Map<String, DiscountStrategy> strategies;

    public DiscountContext(NoDiscountStrategy noDiscountStrategy,
                            MemberDiscountStrategy memberDiscountStrategy,
                            SeasonalSaleStrategy seasonalSaleStrategy) {
        this.strategies = Map.of(
                NONE, noDiscountStrategy,
                MEMBER, memberDiscountStrategy,
                SEASONAL, seasonalSaleStrategy
        );
    }

    /**
     * Looks up the strategy that matches the given discountType and applies
     * it. Falls back to NoDiscountStrategy for null/unknown values so a bad
     * or missing discountType never breaks price calculation.
     */
    public double calculatePrice(String discountType, double originalPrice) {
        DiscountStrategy strategy = strategies.getOrDefault(discountType, strategies.get(NONE));
        return strategy.applyDiscount(originalPrice);
    }
}
