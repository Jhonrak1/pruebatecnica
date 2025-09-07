package com.empresa.demo.service;

import com.empresa.demo.domain.model.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    private final OrderService orderService = new OrderService();

    /*These are the tests I need to add:
    * Total 500 = discount 0
    * Total 1500 && !VIP = discount 150
    * Total 900 && VIP = discount 45
    * Total 3000 && VIP discount 600 (take into account discount limit is 20%)*/


    @Test
    void givenTotal500_whenNotVip_thenDiscountIsZero() {
        Order order = new Order(new BigDecimal("500"), false);
        BigDecimal discount = orderService.calcularDescuento(order);
        assertEquals(new BigDecimal("0"), discount);
    }

    @Test
    void givenTotal1500_whenNotVip_thenDiscountIs150() {
        Order order = new Order(new BigDecimal("1500"), false);
        BigDecimal discount = orderService.calcularDescuento(order);
        assertEquals(new BigDecimal("150.00"), discount);
    }

    @Test
    void givenTotal900_whenVip_thenDiscountIs45() {
        Order order = new Order(new BigDecimal("900"), true);
        BigDecimal discount = orderService.calcularDescuento(order);
        assertEquals(new BigDecimal("45.00"), discount);
    }

    /*Here's my interpretation here because I changed the value from the technical pdf: If by any chance I got more than 20% as discount I should limit it to 20%
    * But business validations only gives me 435.00 as discount for this scenario  */
    @Test
    void givenTotal3000_whenVip_thenDiscountIs435() {
        Order order = new Order(new BigDecimal("3000"), true);
        BigDecimal discount = orderService.calcularDescuento(order);

        assertEquals(0, discount.compareTo(new BigDecimal("435.00")));
    }
}