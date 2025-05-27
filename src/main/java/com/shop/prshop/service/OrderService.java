package com.shop.prshop.service;

import com.shop.prshop.Cart;
import com.shop.prshop.CartItem;
import com.shop.prshop.controller.OrderController;
import com.shop.prshop.dto.OrderDto;
import com.shop.prshop.mapper.OrderMapper;
import com.shop.prshop.model.order.Order;
import com.shop.prshop.model.order.OrderItem;
import com.shop.prshop.model.user.User;
import com.shop.prshop.repository.OrderItemRepository;
import com.shop.prshop.repository.OrderRepository;
import com.shop.prshop.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final Cart cart;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    public OrderService(Cart cart, OrderRepository orderRepository, OrderItemRepository orderItemRepository, UserRepository userRepository) {
        this.cart = cart;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
    }

    public void saveOrder(OrderDto orderDto) {
        Order order = OrderMapper.mapToOrder(orderDto);
        String email = order.getEmail();
        Optional<User> optUser= userRepository.findByEmail(email);
        if(optUser.isPresent()) {
            User user = optUser.get();
            order.setUser(user);
            user.getOrders().add(order);
            userRepository.save(user);
        }
        orderRepository.save(order);
        orderItemRepository.saveAll(OrderMapper.mapToOrderItemList(cart, order));
        cart.clearCart();
    }

    public OrderDto orderLoginInChecker(){
        OrderDto orderDto = new OrderDto();
        User user;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            logger.info("email: " + email);
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if(optionalUser.isPresent()) {
                user = optionalUser.get();
                orderDto = OrderMapper.mapToOrderDto(user);
            }
        }

        return orderDto;
    }

    public List<OrderItem> findUserOrders(String email) {
        List<Order> orders = orderRepository.findOrderByEmail(email);
        List<OrderItem> orderItemsList = new ArrayList<>();
        for(Order newOrder : orders) {
            orderItemsList.addAll(orderItemRepository.findByOrderId(newOrder.getOrderId()));
        }
        return orderItemsList;
    }
}
