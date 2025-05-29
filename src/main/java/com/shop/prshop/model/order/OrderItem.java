package com.shop.prshop.model.order;

import com.shop.prshop.controller.AdminController;
import com.shop.prshop.model.Item;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name="order_items")
public class OrderItem {

    private static final Logger logger = LoggerFactory.getLogger(OrderItem.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "id")
    private Item itemId;

    @Column(name = "amount")
    private int amount;

    @Column(name = "sum")
    private BigDecimal price;

    @Column(name = "item_full_name")
    private String itemFullName;

    @Column(name = "item_image")
    private String itemImage;

    @Column(name = "order_date")
    private String orderDate;

    @Column(name = "order_id")
    private Long orderId;

    public OrderItem() {

    }

    public OrderItem(Item itemId, int amount, BigDecimal price, String itemFullName, String itemImage, Long orderId) {
        this.itemId = itemId;
        this.amount = amount;
        this.price = price;
        this.itemFullName = itemFullName;
        this.itemImage = itemImage;
        this.orderDate = formatOrderDate(LocalDateTime.now());
        this.orderId = orderId;
    }

    private String formatOrderDate(LocalDateTime now) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = now.format(formatter);
        return formattedDateTime;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }


    public Item getItemId() {
        return itemId;
    }

    public void setItemId(Item itemId) {
        this.itemId = itemId;
    }

    public int getAmount() {
        return amount;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getItemFullName() {
        return itemFullName;
    }

    public void setItemFullName(String itemFullName) {
        this.itemFullName = itemFullName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

}
