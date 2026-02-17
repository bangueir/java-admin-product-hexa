package com.hackerrank.sample.application.port.input.usecase;

import com.hackerrank.sample.domain.model.ProductDomain;

public interface UpdateProductUseCase {
    ProductDomain updateProduct(Long id, ProductDomain product);
}
