package com.shop.prshop;

import com.shop.prshop.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CartItemTest {
    private Item item;

    @BeforeEach
    void setUp() {
        item = new Item();
        item.setId(1L);
        item.setPrice(BigDecimal.valueOf(25));
    }

    @Test
    void testCartItemInitialization() {
        CartItem cartItem = new CartItem(item);

        assertEquals(item, cartItem.getItem());
        assertEquals(1, cartItem.getCounter());
        assertEquals(BigDecimal.valueOf(25), cartItem.getPrice());
    }

    @Test
    void testIncreaseCounter() {
        CartItem cartItem = new CartItem(item);

        cartItem.increaseCounter();
        assertEquals(2, cartItem.getCounter());
        assertEquals(BigDecimal.valueOf(50), cartItem.getPrice());

        cartItem.increaseCounter();
        assertEquals(3, cartItem.getCounter());
        assertEquals(BigDecimal.valueOf(75), cartItem.getPrice());
    }

    @Test
    void testDecreaseCounter() {
        CartItem cartItem = new CartItem(item);
        cartItem.increaseCounter(); // 2

        cartItem.decreaseConter();
        assertEquals(1, cartItem.getCounter());
        assertEquals(BigDecimal.valueOf(25), cartItem.getPrice());

        cartItem.decreaseConter();
        assertEquals(0, cartItem.getCounter());
        assertEquals(BigDecimal.ZERO, cartItem.getPrice());
    }

    @Test
    void testDecreaseCounterDoesNotGoBelowZero() {
        CartItem cartItem = new CartItem(item);
        cartItem.decreaseConter(); // to 0
        cartItem.decreaseConter(); // shouldn't go negative

        assertEquals(0, cartItem.getCounter());
        assertEquals(BigDecimal.ZERO, cartItem.getPrice());
    }

    @Test
    void testHasZeroItems() {
        CartItem cartItem = new CartItem(item);
        assertFalse(cartItem.hasZeroItems());

        cartItem.decreaseConter();
        assertTrue(cartItem.hasZeroItems());
    }
}
