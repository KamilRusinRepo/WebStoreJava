package com.shop.prshop.service;

import com.shop.prshop.Cart;
import com.shop.prshop.model.Item;
import com.shop.prshop.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final ItemRepository itemRepository;
    private final Cart cart;

    @Autowired
    public CartService (ItemRepository itemRepository, Cart cart) {
        this.itemRepository = itemRepository;
        this.cart = cart;
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public void addItemToCart(Long itemId) {
        Optional<Item> cartItem = itemRepository.findById(itemId);
        if(cartItem.isPresent()) {
            Item item = cartItem.get();
            cart.addItem(item);
        }
    }

    public void decreaseItemFromCart(Long itemId) {
        Optional<Item> cartItem = itemRepository.findById(itemId);
        if(cartItem.isPresent()) {
            Item item = cartItem.get();
            cart.removeItem(item);
        }
    }

    public void removeItemFromCart(Long itemId) {
        Optional<Item> cartItem = itemRepository.findById(itemId);
        if(cartItem.isPresent()) {
            Item item = cartItem.get();
            cart.removeAllItems(item);
        }
    }

}
