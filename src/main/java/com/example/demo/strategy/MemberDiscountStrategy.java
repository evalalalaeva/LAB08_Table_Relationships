package com.example.demo.strategy;

import org.springframework.stereotype.Component;

/** Members get a flat 10% discount. */
@Component
public class MemberDiscountStrategy implements DiscountStrategy {

    private static final double RATE = 0.10;

    @Override
    public double applyDiscount(double originalPrice) {
        return originalPrice - (originalPrice * RATE);
    }
}
