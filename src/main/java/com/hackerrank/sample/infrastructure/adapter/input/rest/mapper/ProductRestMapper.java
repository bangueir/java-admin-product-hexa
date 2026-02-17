package com.hackerrank.sample.infrastructure.adapter.input.rest.mapper;

import org.springframework.stereotype.Component;

import com.hackerrank.sample.domain.model.ProductDomain;
import com.hackerrank.sample.infrastructure.adapter.input.rest.dto.ProductRestDto;

@Component
public class ProductRestMapper {

    public ProductRestDto toRestDto(ProductDomain domain) {
        if (domain == null) {
            return null;
        }

        ProductRestDto dto = new ProductRestDto();
        dto.setId(domain.getId());
        dto.setTitle(domain.getTitle());
        dto.setPrice(domain.getPrice());
        dto.setCurrencyId(domain.getCurrencyId());
        dto.setAvailableQuantity(domain.getAvailableQuantity());
        dto.setCondition(domain.getCondition());
        dto.setFreeShipping(domain.isFreeShipping());
        dto.setDescription(domain.getDescription());
        dto.setPictureUrl(domain.getPictureUrl());
        return dto;
    }

    public ProductDomain toDomain(ProductRestDto dto) {
        if (dto == null) {
            return null;
        }

        ProductDomain domain = new ProductDomain();
        domain.setId(dto.getId());
        domain.setTitle(dto.getTitle());
        domain.setPrice(dto.getPrice());
        domain.setCurrencyId(dto.getCurrencyId());
        domain.setAvailableQuantity(dto.getAvailableQuantity());
        domain.setCondition(dto.getCondition());
        domain.setFreeShipping(dto.isFreeShipping());
        domain.setDescription(dto.getDescription());
        domain.setPictureUrl(dto.getPictureUrl());
        return domain;
    }
}
