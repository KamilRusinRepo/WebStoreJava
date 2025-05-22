package com.shop.prshop.controller;

import com.shop.prshop.dto.OrderDto;
import com.shop.prshop.model.order.Order;
import com.shop.prshop.model.order.OrderItem;
import com.shop.prshop.repository.OrderItemRepository;
import com.shop.prshop.repository.OrderRepository;
import com.shop.prshop.service.CartService;
import com.shop.prshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class OrderController {

    private final CartService cartService;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    @Autowired
    public OrderController(CartService cartService, OrderService orderService, OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @GetMapping("/")
    public String getCartView() {
        return "cartview";
    }


    @GetMapping("/increase/{itemId}")
    public String increaseItem(@PathVariable("itemId") Long itemId) {
        cartService.addItemToCart(itemId);
        return "cartview";
    }

    @GetMapping("/decrease/{itemId}")
    public String decreaseItem(@PathVariable("itemId") Long itemId) {
        cartService.decreaseItemFromCart(itemId);
        return "cartview";
    }

    @GetMapping("/removeItem/{itemId}")
    public String removeItem(@PathVariable("itemId") Long itemId) {
        cartService.removeItemFromCart(itemId);
        return "cartview";
    }

    @GetMapping("/Checkout")
    public String showCheckout(Model model) {
        OrderDto orderDto = orderService.orderLoginInChecker();
        model.addAttribute("orderDto", orderDto);

        return "checkout";
    }

    @PostMapping("/saveorder")
    public String saveOrder(OrderDto orderDto) {
        orderService.saveOrder(orderDto);
        return "redirect:/";
    }

    @GetMapping("/getUserOrders")
    public String showUserOrders(Principal principal, Model model) {
        String email = principal.getName();
        List<Order> orders = orderRepository.findOrderByEmail(email);
        List<OrderItem> orderItemsList = new ArrayList<>();
        for(Order newOrder : orders) {
            orderItemsList.addAll(orderItemRepository.findByOrderId(newOrder.getOrderId()));
        }
        model.addAttribute("orders", orderItemsList);
        return "userOrders";
    }
}
