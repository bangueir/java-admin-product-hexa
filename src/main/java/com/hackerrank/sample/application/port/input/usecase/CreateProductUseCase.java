package com.hackerrank.sample.application.port.input.usecase;

import com.hackerrank.sample.domain.model.ProductDomain;

public interface CreateProductUseCase {
    ProductDomain createProduct(ProductDomain product);
}
