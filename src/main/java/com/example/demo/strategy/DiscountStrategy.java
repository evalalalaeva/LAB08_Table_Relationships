package com.example.demo.strategy;

/**
 * Strategy Pattern - the common contract every discount algorithm must
 * follow.
 *
 * ISP: this interface exposes exactly one method, so implementers are
 * never forced to implement behaviour they don't need.
 *
 * OCP: adding a brand-new discount rule (e.g. "ClearanceStrategy") only
 * means adding a new class that implements this interface - none of the
 * existing strategies, the context, or the service need to change.
 */
public interface DiscountStrategy {

    /**
     * @param originalPrice the product's list price
     * @return the price after this strategy's discount has been applied
     */
    double applyDiscount(double originalPrice);
}
