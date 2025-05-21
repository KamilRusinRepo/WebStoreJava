package com.shop.prshop.service;

import com.shop.prshop.model.Item;
import com.shop.prshop.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class HomeService {

    private final ItemRepository itemRepository;

    @Autowired
    public HomeService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    public List<Item> findItemsTop6ByMake(String make) {
        return itemRepository.findTop6ByItemMake(make).stream().limit(6).toList();
    }

    public List<Item> findItemsByCategory(String category) {
        return itemRepository.findByCategory(category);
    }



    public List<Item> findItemsForBrandPages(String make, String sort) {
        if(sort.equals("none")) {
            return itemRepository.findByMake(make);

        }
        else if(sort.equals("price")){
            return itemRepository.findByMakeAndOrderByPrice(make);
        }
        else {
            return itemRepository.findByMakeAndOrderBy(make, sort);
        }
    }

    public List<Item> findItemsForCategoryPages(String category, String sort) {
        if(sort.equals("none")) {
            return itemRepository.findByCategory(category);
        }
        else if(sort.equals("price")) {
            return itemRepository.findByCategoryAndOrderByPrice(category);
        }
        else {
            return itemRepository.findByCategoryOrderBy(category, sort);
        }
    }
}
