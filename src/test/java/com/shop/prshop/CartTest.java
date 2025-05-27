package com.shop.prshop;

import com.shop.prshop.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CartTest {

    private Cart cart;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        item1 = new Item();
        item1.setId(1L);
        item1.setPrice(BigDecimal.valueOf(10));

        item2 = new Item();
        item2.setId(2L);
        item2.setPrice(BigDecimal.valueOf(20));
    }

    @Test
    void testAddItem() {
        cart.addItem(item1);
        assertEquals(1, cart.getCounter());
        assertEquals(BigDecimal.valueOf(10), cart.getSum());
    }

    @Test
    void testAddSameItemTwice() {
        cart.addItem(item1);
        cart.addItem(item1);
        assertEquals(2, cart.getCounter());
        assertEquals(BigDecimal.valueOf(20), cart.getSum());
    }

    @Test
    void testAddTwoDifferentItems() {
        cart.addItem(item1);
        cart.addItem(item2);
        assertEquals(2, cart.getCounter());
        assertEquals(BigDecimal.valueOf(30), cart.getSum());
    }

    @Test
    void testRemoveItemDecreasesCount() {
        cart.addItem(item1);
        cart.addItem(item1);
        cart.removeItem(item1);

        assertEquals(1, cart.getCounter());
        assertEquals(BigDecimal.valueOf(10), cart.getSum());
    }

    @Test
    void testRemoveItemCompletely() {
        cart.addItem(item1);
        cart.removeItem(item1);

        assertEquals(0, cart.getCounter());
        assertEquals(BigDecimal.ZERO, cart.getSum());
        assertTrue(cart.getCartItems().isEmpty());
    }

    @Test
    void testClearCart() {
        cart.addItem(item1);
        cart.addItem(item2);
        cart.clearCart();

        assertEquals(0, cart.getCounter());
        assertEquals(BigDecimal.ZERO, cart.getSum());
        assertTrue(cart.getCartItems().isEmpty());
    }

    @Test
    void testRemoveAllItems() {
        cart.addItem(item1);
        cart.addItem(item1);
        cart.removeAllItems(item1);

        assertEquals(0, cart.getCounter());
        assertEquals(BigDecimal.ZERO, cart.getSum());
    }
}