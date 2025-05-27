package com.shop.prshop;

import com.shop.prshop.model.Item;
import com.shop.prshop.repository.ItemRepository;
import com.shop.prshop.service.HomeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class HomeServiceTest {
    @Autowired
    private HomeService homeService;

    @MockBean
    private ItemRepository itemRepository;

    @Test
    void shouldReturnTop6ItemsByMake() {
        String make = "Toyota";
        List<Item> items = List.of(new Item(), new Item(), new Item(), new Item(), new Item(), new Item(), new Item());

        when(itemRepository.findTop6ByItemMake(make)).thenReturn(items);

        List<Item> result = homeService.findItemsTop6ByMake(make);

        assertThat(result).hasSize(6);
        verify(itemRepository).findTop6ByItemMake(make);
    }

    @Test
    void shouldReturnItemsByCategory() {
        String category = "SUV";
        List<Item> items = List.of(new Item(), new Item());

        when(itemRepository.findByCategory(category)).thenReturn(items);

        List<Item> result = homeService.findItemsByCategory(category);

        assertThat(result).isEqualTo(items);
        verify(itemRepository).findByCategory(category);
    }

    @Test
    void shouldCallFindByMakeWhenSortNone() {
        String make = "BMW";
        when(itemRepository.findByMake(make)).thenReturn(List.of(new Item()));

        List<Item> result = homeService.findItemsForBrandPages(make, "none");

        assertThat(result).hasSize(1);
        verify(itemRepository).findByMake(make);
    }

    @Test
    void shouldCallFindByMakeAndOrderByPriceWhenSortIsPrice() {
        String make = "Audi";
        when(itemRepository.findByMakeAndOrderByPrice(make)).thenReturn(List.of(new Item()));

        List<Item> result = homeService.findItemsForBrandPages(make, "price");

        assertThat(result).hasSize(1);
        verify(itemRepository).findByMakeAndOrderByPrice(make);
    }

    @Test
    void shouldCallFindByMakeAndOrderByWhenSortIsOther() {
        String make = "Mercedes";
        String sort = "year";
        when(itemRepository.findByMakeAndOrderBy(make, sort)).thenReturn(List.of(new Item()));

        List<Item> result = homeService.findItemsForBrandPages(make, sort);

        assertThat(result).hasSize(1);
        verify(itemRepository).findByMakeAndOrderBy(make, sort);
    }

    @Test
    void shouldCallFindByCategoryAndOrderByPriceWhenSortIsPrice() {
        String category = "Coupe";
        when(itemRepository.findByCategoryAndOrderByPrice(category)).thenReturn(List.of(new Item()));

        List<Item> result = homeService.findItemsForCategoryPages(category, "price");

        assertThat(result).hasSize(1);
        verify(itemRepository).findByCategoryAndOrderByPrice(category);
    }

    @Test
    void shouldCallFindByCategoryOrderByWhenSortIsOther() {
        String category = "Coupe";
        String sort = "year";
        when(itemRepository.findByCategoryOrderBy(category, sort)).thenReturn(List.of(new Item()));

        List<Item> result = homeService.findItemsForCategoryPages(category, sort);

        assertThat(result).hasSize(1);
        verify(itemRepository).findByCategoryOrderBy(category, sort);
    }
}
