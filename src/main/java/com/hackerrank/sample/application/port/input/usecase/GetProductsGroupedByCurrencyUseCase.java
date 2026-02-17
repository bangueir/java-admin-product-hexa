package com.hackerrank.sample.application.port.input.usecase;

import java.util.List;
import java.util.Map;

import com.hackerrank.sample.domain.model.ProductDomain;

public interface GetProductsGroupedByCurrencyUseCase {
    Map<String, List<ProductDomain>> getProductsGroupedByCurrency();
}
