package com.shop.prshop;

import com.shop.prshop.controller.OrderController;
import com.shop.prshop.dto.OrderDto;
import com.shop.prshop.model.order.Order;
import com.shop.prshop.model.order.OrderItem;
import com.shop.prshop.repository.OrderItemRepository;
import com.shop.prshop.repository.OrderRepository;
import com.shop.prshop.service.CartService;
import com.shop.prshop.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderService orderService;
    @Mock
    private Principal principal;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    Order order = Order.builder()
            .orderId(1L)
            .firstName("Jon")
            .lastName("Example")
            .email("jonexample@gmail.com")
            .city("Warsaw")
            .street("Emilii Platter")
            .homeNumber("10")
            .postCode("10-203")
            .phoneNumber("123456789")
            .build();
    @Test
    public void getCartView_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/cart/"))
                .andExpect(status().isOk())
                .andExpect(view().name("cartview"));
    }

    @Test
    public void increaseItem_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/cart/increase/{itemId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("cartview"));

        verify(cartService).addItemToCart(1L);
    }

    @Test
    public void decreaseItem_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/cart/decrease/{itemId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("cartview"));

        verify(cartService).decreaseItemFromCart(1L);
    }

    @Test
    public void removeItem_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/cart/removeItem/{itemId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("cartview"));

        verify(cartService).removeItemFromCart(1L);
    }

    @Test
    public void showCheckout_success() throws Exception {
        OrderDto orderDto = OrderDto.builder()
                .firstName("Jon")
                .lastName("Example")
                .email("johexample@gmail.com")
                .build();

        when(orderService.orderLoginInChecker()).thenReturn(orderDto);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/cart/Checkout"))
                .andExpect(status().isOk())
                .andExpect(view().name("checkout"))
                .andExpect(model().attribute("orderDto", orderDto));
    }

    @Test
    public void saveOrder_success() throws Exception {
        OrderDto orderDto = new OrderDto();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/cart/saveorder")
                        .flashAttr("orderDto", orderDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(orderService).saveOrder(orderDto);
    }

    @Test
    public void showUserOrders_success() throws Exception {
        String testEmail = "test@example.com";

        OrderItem item1 = new OrderItem();
        OrderItem item2 = new OrderItem();

        when(principal.getName()).thenReturn("test@example.com");
        when(orderRepository.findOrderByEmail(testEmail)).thenReturn(Collections.singletonList(order));
        when(orderItemRepository.findByOrderId(anyLong())).thenReturn(List.of(item1, item2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cart/getUserOrders")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("userOrders"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("orders", hasSize(2)));
    }
}
