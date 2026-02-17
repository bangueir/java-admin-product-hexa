package com.hackerrank.sample.domain.model;

import java.util.Objects;

public class ProductDomain {
    private Long id;
    private String title;
    private float price;
    private String currencyId;
    private int availableQuantity;
    private String condition;
    private boolean freeShipping;
    private String description;
    private String pictureUrl;

    public ProductDomain() {
    }

    public ProductDomain(Long id, String title, float price, String currencyId, 
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

    // Métodos de negocio del dominio
    public boolean isNew() {
        return "NEW".equals(condition);
    }

    public boolean hasStock() {
        return availableQuantity > 0;
    }

    public float calculateTotalValue() {
        return price * availableQuantity;
    }

    // Getters y Setters
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDomain that = (ProductDomain) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
