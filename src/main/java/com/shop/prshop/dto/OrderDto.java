package com.shop.prshop.dto;

import com.shop.prshop.model.order.OrderItem;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;


@Builder
public class OrderDto {
    String firstName;
    String lastName;
    String email;
    String city;
    String street;
    String homeNumber;
    String postCode;
    String phoneNumber;

    public OrderDto() {

    }

    public OrderDto(String firstName, String lastName, String email, String city, String street, String homeNumber, String postCode, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.city = city;
        this.street = street;
        this.homeNumber = homeNumber;
        this.postCode = postCode;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String sureName) {
        this.lastName = sureName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
