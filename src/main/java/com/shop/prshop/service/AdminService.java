package com.shop.prshop.service;

import com.shop.prshop.model.Item;
import com.shop.prshop.model.order.Order;
import com.shop.prshop.model.order.OrderItem;
import com.shop.prshop.model.user.Role;
import com.shop.prshop.model.user.User;
import com.shop.prshop.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    Logger logger = LoggerFactory.getLogger(AdminService.class);
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminService(ItemRepository itemRepository, OrderRepository orderRepository, UserRepository userRepository, OrderItemRepository orderItemRepository, RoleRepository roleRepository) {
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
        this.roleRepository = roleRepository;
    }

    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public void deleteItem(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if(optionalItem.isPresent()) {
            Item itemToDel = optionalItem.get();
            itemRepository.delete(itemToDel);
        }
        else {
            logger.info("Item with id: " + id + "not found!");
        }
    }

    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<OrderItem> findByOrderId(Long id) {
        return orderItemRepository.findByOrderId(id);
    }

    public void deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()) {
            User userToDel = optionalUser.get();
            userToDel.getRoles().clear();
            userRepository.delete(userToDel);
        }
        else {
            logger.info("User with id: " + id + "not found!");
        }
    }

    public void addAdminRole(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        Role role = roleRepository.findByName("ROLE_ADMIN");
        if(optionalUser.isPresent()) {
            User userToUpdate = optionalUser.get();
            userToUpdate.getRoles().add(role);
            userRepository.save(userToUpdate);
        }
        else {
            logger.info("User with id: " + id + "not found!");
        }
    }
}
