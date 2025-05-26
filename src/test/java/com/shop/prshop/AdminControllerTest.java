package com.shop.prshop;

import com.shop.prshop.controller.AdminController;
import com.shop.prshop.model.Item;
import com.shop.prshop.model.order.Order;
import com.shop.prshop.model.order.OrderItem;
import com.shop.prshop.model.user.User;
import com.shop.prshop.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void addItem_shouldReturnAddItemView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/additem"))
                .andExpect(status().isOk())
                .andExpect(view().name("addItem"))
                .andExpect(model().attributeExists("item"));
    }

    @Test
    public void saveItem_shouldRedirectToAccountPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/save")
                        .flashAttr("item", new Item()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/acountPage"));

        verify(adminService).saveItem(any(Item.class));
    }

    @Test
    public void saveUpdatedItem_shouldRedirectToGivenPath() throws Exception {
        String path = "/some/path";
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/saveUpdatedItem")
                        .flashAttr("item", new Item())
                        .param("path", path))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(path));

        verify(adminService).saveItem(any(Item.class));
    }

    @Test
    public void deleteItem_shouldRedirectToGivenPath() throws Exception {
        Long itemId = 1L;
        String path = "/some/path";

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/delete/" + itemId)
                        .param("path", path))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(path));

        verify(adminService).deleteItem(itemId);
    }

    @Test
    public void showUpdateForm_shouldReturnUpdateFormView() throws Exception {
        Long itemId = 1L;
        String path = "/some/path";
        Item mockItem = new Item();
        mockItem.setId(itemId);

        when(adminService.findById(itemId)).thenReturn(Optional.of(mockItem));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/showUpdateForm/" + itemId)
                        .param("path", path))
                .andExpect(status().isOk())
                .andExpect(view().name("updateForm"))
                .andExpect(model().attributeExists("item"))
                .andExpect(model().attribute("item", mockItem))
                .andExpect(model().attribute("path", path));
    }

    @Test
    public void showUpdateForm_shouldReturnErrorPageWhenItemNotFound() throws Exception {
        Long itemId = 1L;
        String path = "/some/path";

        when(adminService.findById(itemId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/showUpdateForm/" + itemId)
                        .param("path", path))
                .andExpect(status().isOk())
                .andExpect(view().name("errorPage"))
                .andExpect(model().attributeExists("errorMsg"));
    }

    @Test
    public void showOrders_shouldReturnOrdersListView() throws Exception {
        List<Order> orders = Arrays.asList(new Order(), new Order());
        when(adminService.findAllOrders()).thenReturn(orders);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/showOrders"))
                .andExpect(status().isOk())
                .andExpect(view().name("ordersList"))
                .andExpect(model().attribute("orders", orders));
    }

    @Test
    public void showItems_shouldReturnItemsListView() throws Exception {
        List<Item> items = Arrays.asList(new Item(), new Item());
        when(adminService.findAllItems()).thenReturn(items);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/showItems"))
                .andExpect(status().isOk())
                .andExpect(view().name("itemsList"))
                .andExpect(model().attribute("items", items));
    }

    @Test
    public void showUsers_shouldReturnUsersListView() throws Exception {
        List<User> users = Arrays.asList(new User(), new User());
        when(adminService.findAllUsers()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/showUsers"))
                .andExpect(status().isOk())
                .andExpect(view().name("usersList"))
                .andExpect(model().attribute("users", users));
    }

    @Test
    public void showDetails_shouldReturnOrderDetailsView() throws Exception {
        Long orderId = 1L;
        List<OrderItem> orderItems = Arrays.asList(new OrderItem(), new OrderItem());
        when(adminService.findByOrderId(orderId)).thenReturn(orderItems);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/showDetails/" + orderId))
                .andExpect(status().isOk())
                .andExpect(view().name("orderDetails"))
                .andExpect(model().attribute("orderItems", orderItems));
    }
}
