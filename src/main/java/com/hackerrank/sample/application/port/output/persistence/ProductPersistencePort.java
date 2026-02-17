package com.hackerrank.sample.application.port.output.persistence;

import java.util.List;
import java.util.Optional;

import com.hackerrank.sample.domain.model.ProductDomain;

public interface ProductPersistencePort {
    ProductDomain save(ProductDomain product);
    Optional<ProductDomain> findById(Long id);
    List<ProductDomain> findAll();
    List<ProductDomain> findByTitleLikeIgnoreCase(String title);
    void deleteById(Long id);
    boolean existsById(Long id);
}
