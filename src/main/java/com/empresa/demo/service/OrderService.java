package com.empresa.demo.service;

import java.math.BigDecimal;
import java.math.MathContext;

import com.empresa.demo.domain.model.Order;
import org.springframework.stereotype.Service;

@Service
/*Added missing @Service annotation*/
public class OrderService {

    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL64; /*I researched this: it's recommended because it ensures consistency while doing the calculations*/
    private static final BigDecimal TEN_PERCENT = new BigDecimal("0.10");
    private static final BigDecimal FIVE_PERCENT = new BigDecimal("0.05");
    private static final BigDecimal MAX_DISCOUNT = new BigDecimal("0.20");
    /*I'm defining constants here to reuse as the service executes (Only in their respective service scope)*/

    public BigDecimal calcularDescuento(Order order) {

        if (order == null || order.getTotal() == null)
            throw new IllegalArgumentException("Neither order and order total must not be null");

        BigDecimal total = order.getTotal();
        BigDecimal discount = BigDecimal.ZERO;

        // Rule 1: Total > 1000, that's a 10% discount
        if (total.compareTo(BigDecimal.valueOf(1000)) > 0) discount = total.multiply(TEN_PERCENT, MATH_CONTEXT);

        // Rule 2: VIP? that's an additional 5% discount (This one's stackable with the previous one)
        if (order.isVip()) {
            BigDecimal vipBase = total.subtract(discount, MATH_CONTEXT);
            BigDecimal vipDiscount = vipBase.multiply(FIVE_PERCENT, MATH_CONTEXT);
            discount = discount.add(vipDiscount, MATH_CONTEXT);
        }

        // Rule 3: Accumulated discount cannot be greater than 20% In this case if it surpasses 20%, I'll take 20% as the final discount automatically
        BigDecimal maxAllowed = total.multiply(MAX_DISCOUNT, MATH_CONTEXT);
        if (discount.compareTo(maxAllowed) > 0) {
            discount = maxAllowed;
        }

        return discount;
    }
}
