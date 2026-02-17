package com.hackerrank.sample.infrastructure.adapter.input.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductRestDto {

    private Long id;

    @NotBlank(message = "Title must not be null or blank")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be greater than 0")
    private Float price;

    @NotBlank(message = "Currency must not be null or blank")
    @Size(min = 3, max = 3, message = "Currency must be exactly 3 characters")
    @Pattern(regexp = "COP|ARS|USD|EUR", message = "Currency must be one of: COP, ARS, USD, EUR")
    private String currencyId;

    @NotNull(message = "Available quantity must not be null")
    @Min(value = 0, message = "Available quantity must not be negative")
    private Integer availableQuantity;

    @NotBlank(message = "Condition must not be null or blank")
    @Size(min = 3, max = 20, message = "Condition must be between 3 and 20 characters")
    @Pattern(regexp = "NEW|USED", message = "Condition must be one of: NEW, USED")
    private String condition;

    @NotNull(message = "Free shipping must not be null")
    private Boolean freeShipping;

    @NotBlank(message = "Description must not be null or blank")
    @Size(min = 5, max = 1000, message = "Description must be between 5 and 1000 characters")
    private String description;

    @NotBlank(message = "Picture URL must not be null or blank")
    @Size(min = 5, max = 500, message = "Picture URL must be between 5 and 500 characters")
    private String pictureUrl;

    public ProductRestDto() {
    }

    public ProductRestDto(Long id, String title, Float price, String currencyId, 
                          Integer availableQuantity, String condition, Boolean freeShipping, 
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Boolean isFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(Boolean freeShipping) {
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
