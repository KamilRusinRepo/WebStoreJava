package com.shop.prshop;

import com.shop.prshop.model.Item;
import com.shop.prshop.repository.ItemRepository;
import com.shop.prshop.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class CartServiceTest {
    @Autowired
    private CartService cartService;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private Cart cart;

    @Test
    void getAllItems_shouldReturnAllItems() {
        Item item1 = new Item();
        Item item2 = new Item();
        when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2));

        List<Item> result = cartService.getAllItems();

        assert result.size() == 2;
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void addItemToCart_shouldAddItemWhenFound() {
        Item item = new Item();
        item.setId(1L);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        cartService.addItemToCart(1L);

        verify(cart, times(1)).addItem(item);
    }

    @Test
    void addItemToCart_shouldDoNothingWhenItemNotFound() {
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        cartService.addItemToCart(999L);

        verify(cart, never()).addItem(any());
    }

    @Test
    void decreaseItemFromCart_shouldRemoveOneItemWhenFound() {
        Item item = new Item();
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        cartService.decreaseItemFromCart(1L);

        verify(cart, times(1)).removeItem(item);
    }

    @Test
    void decreaseItemFromCart_shouldDoNothingWhenItemNotFound() {
        when(itemRepository.findById(2L)).thenReturn(Optional.empty());

        cartService.decreaseItemFromCart(2L);

        verify(cart, never()).removeItem(any());
    }

    @Test
    void removeItemFromCart_shouldRemoveAllWhenFound() {
        Item item = new Item();
        when(itemRepository.findById(3L)).thenReturn(Optional.of(item));

        cartService.removeItemFromCart(3L);

        verify(cart, times(1)).removeAllItems(item);
    }

    @Test
    void removeItemFromCart_shouldDoNothingWhenNotFound() {
        when(itemRepository.findById(4L)).thenReturn(Optional.empty());

        cartService.removeItemFromCart(4L);

        verify(cart, never()).removeAllItems(any());
    }
}
