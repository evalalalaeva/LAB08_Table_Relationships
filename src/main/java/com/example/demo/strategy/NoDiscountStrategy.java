package com.example.demo.strategy;

import org.springframework.stereotype.Component;

/** Default strategy: price is returned unchanged. */
@Component
public class NoDiscountStrategy implements DiscountStrategy {

    @Override
    public double applyDiscount(double originalPrice) {
        return originalPrice;
    }
}
