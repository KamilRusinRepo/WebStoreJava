package com.shop.prshop.controller;


import com.shop.prshop.model.Item;
import com.shop.prshop.model.order.Order;
import com.shop.prshop.model.user.User;
import com.shop.prshop.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    public final AdminService adminService;
    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/additem")
    public String addItem(Model model) {
        Item item = new Item();
        model.addAttribute("item", item);
        return "additem";
    }

    @PostMapping("/save")
    public String saveItem(@ModelAttribute("item") Item item) {
        adminService.saveItem(item);

        return "redirect:/acountPage";
    }

    @PostMapping("/saveUpdatedItem")
    public String saveUpdatedItem(@ModelAttribute("item") Item item, @RequestParam("path") String path) {
        adminService.saveItem(item);

        return "redirect:" + path;
    }

    @PostMapping("/delete/{itemId}")
    public String deleteItem(@PathVariable("itemId") Long id, @RequestParam String path) {
        adminService.deleteItem(id);

        return "redirect:" + path;
    }

    @GetMapping("/showUpdateForm/{itemId}")
    public String showUpdateForm(@PathVariable("itemId") Long id, @RequestParam String path, Model model) {

        Optional<Item> optionalItem = adminService.findById(id);

        if(optionalItem.isPresent()) {
            Item itemToUpdate = optionalItem.get();
            model.addAttribute("item", itemToUpdate);
            model.addAttribute("path", path);
        }
        else {
            model.addAttribute("errorMsg", "Item with id:" + id + "not found");
            return "errorPage";
        }

        return "updateForm";
    }

    @GetMapping("/showOrders")
    public String showOrders(Model model) {
        List<Order> orders = adminService.findAllOrders();
        model.addAttribute("orders", orders);
        return "ordersList";
    }

    @GetMapping("/showItems")
    public String showItems(Model model) {
        List<Item> items = adminService.findAllItems();
        model.addAttribute("items", items);
        return "itemsList";
    }

    @GetMapping("/showUsers")
    public String showUsers(Model model) {
        List<User> users =  adminService.findAllUsers();
        model.addAttribute("users", users);
        return "usersList";
    }

    @GetMapping("/showDetails/{itemId}")
    public String showDetails(@PathVariable("itemId") Long id, Model model) {
        model.addAttribute("orderItems", adminService.findByOrderId(id));
        return "orderDetails";
    }

}
