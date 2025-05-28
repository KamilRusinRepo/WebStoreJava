package com.shop.prshop;

import com.shop.prshop.dto.OrderDto;
import com.shop.prshop.mapper.OrderMapper;
import com.shop.prshop.model.order.Order;
import com.shop.prshop.model.order.OrderItem;
import com.shop.prshop.model.user.User;
import com.shop.prshop.repository.OrderItemRepository;
import com.shop.prshop.repository.OrderRepository;
import com.shop.prshop.repository.UserRepository;
import com.shop.prshop.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @MockBean
    private Cart cart;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderItemRepository orderItemRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testSaveOrder() {
        OrderDto dto = new OrderDto();
        Order order = new Order();
        List<OrderItem> orderItems = List.of(new OrderItem());

        try (MockedStatic<OrderMapper> mock = mockStatic(OrderMapper.class)) {
            mock.when(() -> OrderMapper.mapToOrder(dto)).thenReturn(order);
            mock.when(() -> OrderMapper.mapToOrderItemList(cart, order)).thenReturn(orderItems);

            orderService.saveOrder(dto);

            verify(orderRepository).save(order);
            verify(orderItemRepository).saveAll(orderItems);
            verify(cart).clearCart();
        }
    }

    @Test
    void testOrderLoginInChecker_authenticatedUser() {
        String email = "user@example.com";

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(email);
        SecurityContext sc = mock(SecurityContext.class);
        when(sc.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(sc);

        User user = new User();
        user.setEmail(email);
        OrderDto expectedDto = new OrderDto();

        try (MockedStatic<OrderMapper> mock = mockStatic(OrderMapper.class)) {
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
            mock.when(() -> OrderMapper.mapToOrderDto(user)).thenReturn(expectedDto);

            OrderDto result = orderService.orderLoginInChecker();

            assertEquals(expectedDto, result);
        }
    }

    @Test
    void testFindUserOrders() {
        String email = "user@example.com";
        Order order = new Order();
        order.setOrderId(123L);
        List<Order> orders = List.of(order);
        List<OrderItem> items = List.of(new OrderItem());

        when(orderRepository.findOrderByEmail(email)).thenReturn(orders);
        when(orderItemRepository.findByOrderId(123L)).thenReturn(items);

        List<OrderItem> result = orderService.findUserOrders(email);

        assertEquals(1, result.size());
        verify(orderRepository).findOrderByEmail(email);
        verify(orderItemRepository).findByOrderId(123L);
    }
}
