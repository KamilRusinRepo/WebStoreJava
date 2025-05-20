package com.shop.prshop;

import com.shop.prshop.model.Item;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Cart {

    private List<CartItem> cartItems = new ArrayList<>();
    private int counter = 0;
    private BigDecimal sum = BigDecimal.ZERO;

    public void addItem(Item item) {
        getCartItemById(item).ifPresentOrElse(
                CartItem::increaseCounter,
                () -> cartItems.add(new CartItem(item))
        );
        recalculatePriceAndCounter();
    }

    public void removeItem(Item item) {
        Optional<CartItem> oCartItem = getCartItemById(item);

        if(oCartItem.isPresent()) {
            CartItem cartItem = oCartItem.get();
            cartItem.decreaseConter();
            if(cartItem.hasZeroItems()) {
                removeAllItems(item);
            }
            recalculatePriceAndCounter();
        }
    }

    public void recalculatePriceAndCounter() {
        sum = cartItems.stream().map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        counter = cartItems.stream().mapToInt(CartItem::getCounter)
                .reduce(0,Integer::sum);
    }

    private Optional<CartItem> getCartItemById(Item item) {
        return cartItems.stream()
                .filter(i -> i.getItem().getId().equals(item.getId()))
                .findFirst();
    }

    public void removeAllItems(Item item) {
        cartItems.removeIf(i -> i.getItem().getId().equals(item.getId()));
        recalculatePriceAndCounter();
    }

    public void clearCart() {
        cartItems.clear();
        counter = 0;
        sum = BigDecimal.ZERO;
    }
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}
