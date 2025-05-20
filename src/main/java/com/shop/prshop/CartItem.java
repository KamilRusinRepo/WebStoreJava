package com.shop.prshop;

import com.shop.prshop.model.Item;

import java.math.BigDecimal;

public class CartItem {

    private Item item;
    private int counter;
    private BigDecimal price;

    public CartItem(Item item) {
        this.item = item;
        this.counter = 1;
        this.price = item.getPrice();
    }

    public void increaseCounter() {
        counter++;
        price = item.getPrice().multiply(new BigDecimal(counter));
    }

    public void decreaseConter() {
        if(counter > 0 ) {
            counter--;
            price = item.getPrice().multiply(new BigDecimal(counter));
        }
    }

    public boolean hasZeroItems() {
        return counter == 0;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
