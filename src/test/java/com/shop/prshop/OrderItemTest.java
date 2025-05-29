package com.shop.prshop;

import com.shop.prshop.model.Item;
import com.shop.prshop.model.order.OrderItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class OrderItemTest {
    @Test
    void testNoArgsConstructorAndSetters() {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(1L);
        orderItem.setItemId(new Item());
        orderItem.setAmount(3);
        orderItem.setPrice(BigDecimal.valueOf(99.99));
        orderItem.setItemFullName("Test Product");
        orderItem.setItemImage("image.jpg");
        orderItem.setOrderDate("2024-01-01 10:00");
        orderItem.setOrderId(5L);

        assertEquals(1L, orderItem.getOrderItemId());
        assertEquals(3, orderItem.getAmount());
        assertEquals(BigDecimal.valueOf(99.99), orderItem.getPrice());
        assertEquals("Test Product", orderItem.getItemFullName());
        assertEquals("image.jpg", orderItem.getItemImage());
        assertEquals("2024-01-01 10:00", orderItem.getOrderDate());
        assertEquals(5L, orderItem.getOrderId());
    }

    @Test
    void testAllArgsConstructor() {
        OrderItem orderItem = new OrderItem(
                new Item(),
                2,
                BigDecimal.valueOf(50.00),
                "Laptop Lenovo",
                "laptop.jpg",
                20L
        );

        assertEquals(2, orderItem.getAmount());
        assertEquals(BigDecimal.valueOf(50.00), orderItem.getPrice());
        assertEquals("Laptop Lenovo", orderItem.getItemFullName());
        assertEquals("laptop.jpg", orderItem.getItemImage());
        assertEquals(20L, orderItem.getOrderId());

        String orderDate = orderItem.getOrderDate();
        assertNotNull(orderDate);
        assertTrue(orderDate.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}"));
    }
}
