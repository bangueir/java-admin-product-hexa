package com.hackerrank.sample.infrastructure.adapter.output.persistence.mapper;

import org.springframework.stereotype.Component;

import com.hackerrank.sample.domain.model.ProductDomain;
import com.hackerrank.sample.infrastructure.adapter.output.persistence.jpa.entity.ProductEntity;

@Component
public class ProductPersistenceMapper {

    public ProductDomain toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }

        ProductDomain domain = new ProductDomain();
        domain.setId(entity.getId());
        domain.setTitle(entity.getTitle());
        domain.setPrice(entity.getPrice());
        domain.setCurrencyId(entity.getCurrencyId());
        domain.setAvailableQuantity(entity.getAvailableQuantity());
        domain.setCondition(entity.getCondition());
        domain.setFreeShipping(entity.isFreeShipping());
        domain.setDescription(entity.getDescription());
        domain.setPictureUrl(entity.getPictureUrl());
        return domain;
    }

    public ProductEntity toEntity(ProductDomain domain) {
        if (domain == null) {
            return null;
        }

        ProductEntity entity = new ProductEntity();
        entity.setId(domain.getId());
        entity.setTitle(domain.getTitle());
        entity.setPrice(domain.getPrice());
        entity.setCurrencyId(domain.getCurrencyId());
        entity.setAvailableQuantity(domain.getAvailableQuantity());
        entity.setCondition(domain.getCondition());
        entity.setFreeShipping(domain.isFreeShipping());
        entity.setDescription(domain.getDescription());
        entity.setPictureUrl(domain.getPictureUrl());
        return entity;
    }
}
