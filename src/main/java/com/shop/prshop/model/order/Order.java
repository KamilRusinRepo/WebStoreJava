package com.shop.prshop.model.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name="email")
    private String email;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "home_number")
    private String homeNumber;

    @Column(name = "post_code")
    private String postCode;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "created")
    private LocalDateTime created;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems;

    public Order() {

    }
    public Order(Long orderId, String firstName, String lastName, String email, String city, String street, String homeNumber, String postCode, String phoneNumber, LocalDateTime created, List<OrderItem> orderItems) {
        this.orderId = orderId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.city = city;
        this.street = street;
        this.homeNumber = homeNumber;
        this.postCode = postCode;
        this.phoneNumber = phoneNumber;
        this.created = created;
        this.orderItems = orderItems;
    }

    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {this.orderId = orderId;}

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public LocalDateTime getCreated() {
        return created;
    }
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
