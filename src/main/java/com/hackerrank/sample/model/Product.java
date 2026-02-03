package com.hackerrank.sample.model;

import com.hackerrank.sample.enums.CurrencyTypes;
import com.hackerrank.sample.enums.ProductContiditions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private float price;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyTypes currencyId = CurrencyTypes.COP;
    @Column(nullable = false)
    private int availableQuantity;
    @Enumerated(EnumType.STRING)
    @Column(name = "product_condition", nullable = false)
    private ProductContiditions condition = ProductContiditions.NEW;
    @Column(nullable = false)
    private boolean freeShipping;
    @Column(nullable = true)
    private String description;
    @Column(nullable = true)
    private String pictureUrl;

    public Product() {
    }

    public Product(Long id, String title, float price, CurrencyTypes currencyId, int availableQuantity,
            ProductContiditions condition, boolean freeShipping, String description, String pictureUrl) {
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

    public CurrencyTypes getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(CurrencyTypes currencyId) {
        this.currencyId = currencyId;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public ProductContiditions getCondition() {
        return condition;
    }

    public void setCondition(ProductContiditions condition) {
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
