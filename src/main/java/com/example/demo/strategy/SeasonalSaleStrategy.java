package com.example.demo.strategy;

import org.springframework.stereotype.Component;

/** Seasonal sale takes 20% off the list price. */
@Component
public class SeasonalSaleStrategy implements DiscountStrategy {

    private static final double RATE = 0.20;

    @Override
    public double applyDiscount(double originalPrice) {
        return originalPrice - (originalPrice * RATE);
    }
}
