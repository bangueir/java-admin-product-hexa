package com.hackerrank.sample.application.port.input.usecase;

import java.util.List;

import com.hackerrank.sample.domain.model.ProductDomain;

public interface GetAllProductsUseCase {
    List<ProductDomain> getAllProducts();
}
