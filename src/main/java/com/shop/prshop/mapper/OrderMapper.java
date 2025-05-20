package com.shop.prshop.mapper;

import com.shop.prshop.Cart;
import com.shop.prshop.CartItem;
import com.shop.prshop.dto.OrderDto;
import com.shop.prshop.model.Item;
import com.shop.prshop.model.order.Order;
import com.shop.prshop.model.order.OrderItem;
import com.shop.prshop.model.user.User;
import com.shop.prshop.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {


    public static OrderDto mapToOrderDto(User user) {
        return OrderDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }
    public static Order mapToOrder(OrderDto orderDto) {
        return Order.builder()
                .firstName(orderDto.getFirstName())
                .lastName(orderDto.getLastName())
                .email(orderDto.getEmail())
                .city(orderDto.getCity())
                .street(orderDto.getStreet())
                .homeNumber(orderDto.getHomeNumber())
                .postCode(orderDto.getPostCode())
                .phoneNumber(orderDto.getPhoneNumber())
                .created(LocalDateTime.now())
                .build();
    }

    public static List<OrderItem> mapToOrderItemList(Cart cart, Order order) {
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem ci : cart.getCartItems()) {
            Item item = ci.getItem();
            orderItems.add(new OrderItem(ci.getItem().getId(), ci.getCounter(), ci.getPrice(), item.getFullName(), item.getImage(), order.getOrderId()));
        }
        return orderItems;
    }
}
