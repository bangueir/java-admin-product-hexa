package com.hackerrank.sample.infrastructure.adapter.output.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private float price;

    @Column(nullable = false, length = 3)
    private String currencyId;

    @Column(nullable = false)
    private int availableQuantity;

    @Column(name = "product_condition", nullable = false, length = 20)
    private String condition;

    @Column(nullable = false)
    private boolean freeShipping;

    @Column(length = 1024)
    private String description;

    @Column(length = 1024)
    private String pictureUrl;

    public ProductEntity() {
    }

    public ProductEntity(Long id, String title, float price, String currencyId, 
                        int availableQuantity, String condition, boolean freeShipping, 
                        String description, String pictureUrl) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.currencyId = currencyId;
        this.availableQuantity = availableQuantity;
        this.condition = condition;
        this.freeShipping = freeShipping;
        this.description = description;
        this.pictureUrl = pictureUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public boolean isFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(boolean freeShipping) {
        this.freeShipping = freeShipping;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
