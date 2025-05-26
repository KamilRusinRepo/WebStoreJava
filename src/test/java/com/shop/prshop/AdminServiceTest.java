package com.shop.prshop;

import com.shop.prshop.model.Item;
import com.shop.prshop.model.order.Order;
import com.shop.prshop.model.order.OrderItem;
import com.shop.prshop.model.user.User;
import com.shop.prshop.repository.ItemRepository;
import com.shop.prshop.repository.OrderItemRepository;
import com.shop.prshop.repository.OrderRepository;
import com.shop.prshop.repository.UserRepository;
import com.shop.prshop.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AdminServiceTest {
    @Autowired
    private AdminService adminService;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private OrderItemRepository orderItemRepository;

    @Test
    void testSaveItem() {
        Item item = new Item();
        item.setId(1L);
        item.setMake("Test");

        adminService.saveItem(item);
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void testDeleteItem_ItemExists() {
        Item item = new Item();
        item.setId(1L);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        adminService.deleteItem(1L);
        verify(itemRepository).delete(item);
    }

    @Test
    void testDeleteItem_ItemNotExists() {
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        adminService.deleteItem(999L);
        verify(itemRepository, never()).delete(any());
    }

    @Test
    void testFindById() {
        Item item = new Item();
        item.setId(1L);
        item.setMake("Test");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Optional<Item> result = adminService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getMake());
    }

    @Test
    void testFindAllOrders() {
        Order order = new Order();
        order.setOrderId(1L);
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<Order> result = adminService.findAllOrders();
        assertEquals(1, result.size());
    }

    @Test
    void testFindAllItems() {
        Item item = new Item();
        item.setId(1L);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> result = adminService.findAllItems();
        assertEquals(1, result.size());
    }

    @Test
    void testFindAllUsers() {
        User user = new User();
        user.setFirstName("admin");

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = adminService.findAllUsers();
        assertEquals(1, result.size());
        assertEquals("admin", result.get(0).getFirstName());
    }

    @Test
    void testFindByOrderId() {
        Order order = new Order();
        order.setOrderId(1L);
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(1L);
        when(orderItemRepository.findByOrderId(1L)).thenReturn(List.of(orderItem));

        List<OrderItem> result = adminService.findByOrderId(1L);
        assertEquals(1, result.size());
    }
}
