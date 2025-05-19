package com.shop.prshop.model;

import jakarta.persistence.*;
import lombok.Builder;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;

import java.math.BigDecimal;

@Entity
@Table(name = "item")
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "make")
    private String make;

    @Column(name = "model")
    private String model;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "image")
    private String image;

    public Item(Long id, String type, String make, String model, String fullName, BigDecimal price, String image) {
        this.id = id;
        this.type = type;
        this.make = make;
        this.model = model;
        this.fullName = fullName;
        this.price = price;
        this.image = image;

    }
    public Item(String type, String make, String model, String fullName, BigDecimal price, String image) {
       this.type = type;
       this.make = make;
       this.model = model;
       this.fullName = fullName;
       this.price = price;
       this.image = image;

    }

    public Item() {

    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
