package com.shop.prshop;

import com.shop.prshop.controller.HomeController;
import com.shop.prshop.model.Item;
import com.shop.prshop.service.CartService;
import com.shop.prshop.service.HomeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class HomeControllerTest {
    private MockMvc mockMvc;

    @Mock
    private HomeService homeService;

    @Mock
    private CartService cartService;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    Item apple_1 = Item.builder()
            .id(2L)
            .price(new BigDecimal(20))
            .type("phone")
            .make("apple")
            .model("16")
            .fullName("iphone 16")
            .image("https://www.google.com/aclk?sa=l&ai=DChcSEwjmg47D89iIAxViqWgJHapzAzQYABABGgJ3Zg&co=1&ase=2&gclid=EAIaIQobChMI5oOOw_PYiAMVYqloCR2qcwM0EAQYASABEgL0C_D_BwE&sig=AOD64_3siCvisXQdX_MGgd0TjCaYt5WxaQ&ctype=5&q=&nis=4&ved=2ahUKEwiYlYnD89iIAxXCR_EDHRRkIEUQwg8oAXoECAgQCw&adurl=")
            .build();

    Item apple_2 = Item.builder()
            .id(3L)
            .price(new BigDecimal(30))
            .type("phone")
            .make("apple")
            .model("15")
            .fullName("iphone 15")
            .image("https://prod-api.mediaexpert.pl/api/images/gallery/thumbnails/images/58/5860372/Smartfon-APPLE-iPhone-15-Pro-Tytan-naturalny-1.jpg")
            .build();

    Item samsung_1 = Item.builder()
            .id(1L)
            .price(new BigDecimal(10))
            .type("phone")
            .make("samsung")
            .model("s24")
            .fullName("samsung galaxy s24")
            .image("https://www.google.com/imgres?q=samsung%20s24&imgurl=https%3A%2F%2Fimages.samsung.com%2Fis%2Fimage%2Fsamsung%2Fp6pim%2Fpl%2F2401%2Fgallery%2Fpl-galaxy-s24-sm-s921bzadeue-thumb-539473760%3F%24GNB_CARD_FULL_M_PNG_PNG%24&imgrefurl=https%3A%2F%2Fwww.samsung.com%2Fpl%2Fsmartphones%2Fgalaxy-s24%2Fbuy%2F&docid=MSzC0qRVmKzOBM&tbnid=okftmgFZqKfnIM&vet=12ahUKEwjtuNOD89iIAxW-BNsEHSIfDRkQM3oECBkQAA..i&w=330&h=330&hcb=2&itg=1&ved=2ahUKEwjtuNOD89iIAxW-BNsEHSIfDRkQM3oECBkQAA")
            .build();

    Item samsung_2 = Item.builder()
            .id(4L)
            .price(new BigDecimal(50))
            .type("phone")
            .make("samsung")
            .model("s23")
            .fullName("samsung galaxy s23")
            .image("https://cdn.x-kom.pl/i/setup/images/prod/big/product-new-big,,2023/3/pr_2023_3_15_9_29_37_752_00.jpg")
            .build();

    @Test
    public void home_success() throws Exception {

        when(homeService.findItemsTop6ByMake("Apple")).thenReturn(Collections.singletonList(apple_1));
        when(homeService.findItemsTop6ByMake("Samsung")).thenReturn(Collections.singletonList(samsung_1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("apple", hasSize(1)))
                .andExpect(model().attribute("apple", hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("price", is(new BigDecimal(20))),
                                hasProperty("type", is("phone")),
                                hasProperty("make", is("apple")),
                                hasProperty("model", is("16")),
                                hasProperty("fullName", is("iphone 16")),
                                hasProperty("image", is("https://www.google.com/aclk?sa=l&ai=DChcSEwjmg47D89iIAxViqWgJHapzAzQYABABGgJ3Zg&co=1&ase=2&gclid=EAIaIQobChMI5oOOw_PYiAMVYqloCR2qcwM0EAQYASABEgL0C_D_BwE&sig=AOD64_3siCvisXQdX_MGgd0TjCaYt5WxaQ&ctype=5&q=&nis=4&ved=2ahUKEwiYlYnD89iIAxXCR_EDHRRkIEUQwg8oAXoECAgQCw&adurl="))
                        )
                )))
                .andExpect(model().attribute("samsung", hasSize(1)))
                .andExpect(model().attribute("samsung", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("price", is(new BigDecimal(10))),
                                hasProperty("type", is("phone")),
                                hasProperty("make", is("samsung")),
                                hasProperty("model", is("s24")),
                                hasProperty("fullName", is("samsung galaxy s24")),
                                hasProperty("image", is("https://www.google.com/imgres?q=samsung%20s24&imgurl=https%3A%2F%2Fimages.samsung.com%2Fis%2Fimage%2Fsamsung%2Fp6pim%2Fpl%2F2401%2Fgallery%2Fpl-galaxy-s24-sm-s921bzadeue-thumb-539473760%3F%24GNB_CARD_FULL_M_PNG_PNG%24&imgrefurl=https%3A%2F%2Fwww.samsung.com%2Fpl%2Fsmartphones%2Fgalaxy-s24%2Fbuy%2F&docid=MSzC0qRVmKzOBM&tbnid=okftmgFZqKfnIM&vet=12ahUKEwjtuNOD89iIAxW-BNsEHSIfDRkQM3oECBkQAA..i&w=330&h=330&hcb=2&itg=1&ved=2ahUKEwjtuNOD89iIAxW-BNsEHSIfDRkQM3oECBkQAA"))
                        )
                )));
    }

    @Test
    public void applePage_success() throws Exception {

        List<Item> appleList = new ArrayList<>(Arrays.asList(apple_1, apple_2));

        when(homeService.findItemsForBrandPages("Apple", "none")).thenReturn(appleList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/applepage")
                        .param("sort", "none"))
                .andExpect(status().isOk())
                .andExpect(view().name("applePage"))
                .andExpect(model().attribute("items", appleList))
                .andExpect(model().attribute("sort", "none"));
    }

    @Test
    public void addItem_success() throws Exception {
        Long itemId = 1L;
        String path = "/applepage";
        List<Item> cart = new ArrayList<>(Arrays.asList(apple_1, apple_2, samsung_1));

        when(cartService.getAllItems()).thenReturn(cart);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/add/{itemId}", itemId)
                        .param("path", path))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(path));

        verify(cartService).addItemToCart(itemId);
    }

    @Test
    public void showAccountPage_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/acountPage"))
                .andExpect(status().isOk())
                .andExpect(view().name("acountpage"));
    }

    @Test
    public void showProductPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/productpage"))
                .andExpect(status().isOk())
                .andExpect(view().name("productPage"));
    }

    @Test
    public void samsungPage_success() throws Exception {
        List<Item> samsungList = new ArrayList<>(Arrays.asList(samsung_1, samsung_2));

        when(homeService.findItemsForBrandPages("Samsung", "none")).thenReturn(samsungList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/samsungpage")
                        .param("sort", "none"))
                .andExpect(status().isOk())
                .andExpect(view().name("samsungPage"))
                .andExpect(model().attribute("items", samsungList))
                .andExpect(model().attribute("sort", "none"));
    }

    @Test
    public void showPhonesPage_success() throws Exception {
        List<Item> phonesList = new ArrayList<>(Arrays.asList(apple_2, samsung_2));

        when(homeService.findItemsForCategoryPages("Phone", "price")).thenReturn(phonesList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/phonespage")
                        .param("sort", "price"))
                .andExpect(status().isOk())
                .andExpect(view().name("phonesPage"))
                .andExpect(model().attribute("items", phonesList))
                .andExpect(model().attribute("sort", "price"));
    }

    @Test
    public void showLaptopsPage_success() throws Exception {
        Item laptop = Item.builder()
                .id(1L)
                .price(new BigDecimal(1000))
                .type("Laptop")
                .make("Apple")
                .model("Macbook Pro 16")
                .fullName("Apple Macbook Pro 16")
                .image("https://tanimacbook.pl/wp-content/uploads/2023/11/apple-macbook-pro-14-gwiezdna-szarosc-1.webp")
                .build();

        when(homeService.findItemsForCategoryPages("Laptop", "none")).thenReturn(Collections.singletonList(laptop));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/laptopspage"))
                .andExpect(status().isOk())
                .andExpect(view().name("laptopsPage"))
                .andExpect(model().attribute("items", Arrays.asList(laptop)))
                .andExpect(model().attribute("sort", "none"));
    }

    @Test
    public void showTablets_success() throws Exception {
        Item tablet = Item.builder()
                .id(1L)
                .price(new BigDecimal(1500))
                .type("tablet")
                .make("Apple")
                .model("iPad Pro 11'")
                .fullName("Apple iPad Pro 11'")
                .image("https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcTjw2SlZI2i2_Qe-9sE5RfdaycyoydMLi41dLE2gXMz-02XTc8y67Ve4avn_LXGdLebC-7891OwP4iQqK7rBC7MnUmBakh9oXOWlx1yIc8YZ4Bh8hRgVvceeVqobw&usqp=CAc")
                .build();

        when(homeService.findItemsForCategoryPages("Tablet", "none")).thenReturn(Collections.singletonList(tablet));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tabletspage"))
                .andExpect(status().isOk())
                .andExpect(view().name("tabletsPage"))
                .andExpect(model().attribute("items", Arrays.asList(tablet)))
                .andExpect(model().attribute("sort", "none"));
    }

    @Test
    public void showHeadphonesPage_success() throws Exception {
        Item headphone = Item.builder()
                .id(1L)
                .price(new BigDecimal(900))
                .type("Headphones")
                .make("Apple")
                .model("AirPods Pro 2")
                .fullName("Apple AirPods Pro 2")
                .image("https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcQs_ChNbfH4aCziSrPaaQCvvFxMAAeVlNnPXyYNl8hZt2LF2CCUMhfdXyVFovds2eFikGOs8EGfdou_-J2sngMbvnIv0QyrGvzUN2KiRZfkoyQCIh_VeV07wPF8GkV-3N_mhF5c3w&usqp=CAc")
                .build();

        when(homeService.findItemsForCategoryPages("Headphones", "none")).thenReturn(Collections.singletonList(headphone));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/headphonespage"))
                .andExpect(status().isOk())
                .andExpect(view().name("headphonesPage"))
                .andExpect(model().attribute("items", Arrays.asList(headphone)))
                .andExpect(model().attribute("sort", "none"));
    }

    @Test
    public void showWatchesPage_success() throws Exception {
        Item watch = Item.builder()
                .id(1L)
                .price(new BigDecimal(1200))
                .type("Watch")
                .make("Apple")
                .model("Watch Ultra 2")
                .fullName("Apple Watch Ultra 2")
                .image("https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcQpTzXM11iYQbrAjI67JTBngpIp5iGNe8zLZCJkJxJ6_n12hO_p4TgjU47-4VXZXzyFOBB7MqR9kKB8PakDh5gk8WMatSj5ptGwamvBVwqLpP4_OnnmfQBmRYlK_Z82Fg&usqp=CAc")
                .build();

        when(homeService.findItemsForCategoryPages("Watch", "none")).thenReturn(Collections.singletonList(watch));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/watchespage"))
                .andExpect(status().isOk())
                .andExpect(view().name("watchesPage"))
                .andExpect(model().attribute("items", Arrays.asList(watch)))
                .andExpect(model().attribute("sort", "none"));
    }

    @Test
    public void showAccessoriesPage_success() throws Exception {
        Item accessories = Item.builder()
                .id(1L)
                .price(new BigDecimal(90))
                .type("Accessories")
                .make("Apple")
                .model("Charger")
                .fullName("Apple Charger")
                .image("https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcT-xjYBvCORPn0CAVYVvBL0yvBxm9bzpQXAk7Ccq6Gy_BRYagCLfqjD4IHHYvU3fHughiaTzZyMhSZOqLtGnXvZFdHMLgyNNzy9soUhP6moiqf8DIr8uL588s1vJhAS2dkK6e4Kvw&usqp=CAc")
                .build();

        when(homeService.findItemsForCategoryPages("Accessories", "none")).thenReturn(Collections.singletonList(accessories));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/accessoriespage"))
                .andExpect(status().isOk())
                .andExpect(view().name("accessoriesPage"))
                .andExpect(model().attribute("items", Arrays.asList(accessories)))
                .andExpect(model().attribute("sort", "none"));
    }
}
