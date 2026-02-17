package com.hackerrank.sample.application.port.input.usecase;

import com.hackerrank.sample.domain.model.ProductDomain;

public interface GetProductUseCase {
    ProductDomain getProductById(Long id);
}
