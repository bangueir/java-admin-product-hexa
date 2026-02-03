package com.hackerrank.sample.service;

import java.util.List;
import java.util.Map;

import com.hackerrank.sample.dto.ProductDto;

public interface ProductService {
    
    void deleteProductById(Long id);

    ProductDto createProduct(ProductDto productDto);

    ProductDto getProductById(Long id);

    List<ProductDto> getProductByTitle(String title);

    List<ProductDto> getAllProducts();

    ProductDto updateProduct(Long id, ProductDto productDto);

    ProductDto getProductWithHigherValue();

    Map<String, List<ProductDto>> getProductsGroupCurrency();


}
