package com.hackerrank.sample.mapper;

import com.hackerrank.sample.dto.ProductDto;
import com.hackerrank.sample.enums.CurrencyTypes;
import com.hackerrank.sample.enums.ProductContiditions;
import com.hackerrank.sample.model.Product;

public class ProductMapper {

    private ProductMapper() {
    }

    public static ProductDto toProductDto(Product product) {
        if (product == null) {
            return null;
        }

        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setPrice(product.getPrice());
        dto.setCurrencyId(product.getCurrencyId() != null ? product.getCurrencyId().name() : null);
        dto.setAvailableQuantity(product.getAvailableQuantity());
        dto.setCondition(product.getCondition() != null ? product.getCondition().name() : null);
        dto.setFreeShipping(product.isFreeShipping());
        dto.setDescription(product.getDescription());
        dto.setPictureUrl(product.getPictureUrl());
        return dto;
    }

    public static Product toProduct(ProductDto dto) {
        if (dto == null) {
            return null;
        }

        Product product = new Product();
        product.setId(dto.getId());
        product.setTitle(dto.getTitle());
        product.setPrice(dto.getPrice());
        if (dto.getCurrencyId() != null) {
            product.setCurrencyId(CurrencyTypes.valueOf(dto.getCurrencyId()));
        }
        product.setAvailableQuantity(dto.getAvailableQuantity());
        if (dto.getCondition() != null) {
            product.setCondition(ProductContiditions.valueOf(dto.getCondition()));
        }
        product.setFreeShipping(dto.isFreeShipping());
        product.setDescription(dto.getDescription());
        product.setPictureUrl(dto.getPictureUrl());
        return product;
    }
}
