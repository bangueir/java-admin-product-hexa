package com.hackerrank.sample.service.Impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hackerrank.sample.dto.ProductDto;
import com.hackerrank.sample.exception.BadResourceRequestException;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.mapper.ProductMapper;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.repository.ProductRepository;
import com.hackerrank.sample.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void deleteProductById(Long id) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isEmpty()) {
            throw new NoSuchResourceFoundException("No product with given id found.");
        }

        productRepository.deleteById(id);
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        if (productDto == null) {
            throw new BadResourceRequestException("Product payload is required.");
        }

        Product product = ProductMapper.toProduct(productDto);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.toProductDto(savedProduct);
    }

    @Override
    public ProductDto getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            throw new NoSuchResourceFoundException("No product with given id found.");
        }

        return ProductMapper.toProductDto(product.get());
    }

    @Override
    public List<ProductDto> getProductByTitle(String title) {
        List<Product> products = productRepository.findByTitleLikeIgnoreCase(title);
        return products.stream()
                .map(ProductMapper::toProductDto)
                .toList();
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductMapper::toProductDto)
                .toList();
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        if (productDto == null) {
            throw new BadResourceRequestException("Product payload is required.");
        }

        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isEmpty()) {
            throw new NoSuchResourceFoundException("No product with given id found.");
        }

        Product product = ProductMapper.toProduct(productDto);
        product.setId(id);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.toProductDto(savedProduct);
    }

    @Override
    public ProductDto getProductWithHigherValue() {

        List<Product> products = productRepository.findAll();

        Optional<Product> product = products.stream()
                .max((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()));

        if (product.isEmpty()) {
            throw new NoSuchResourceFoundException("No product found.");
        }

        return ProductMapper.toProductDto(product.get());
    }

    @Override
    public Map<String, List<ProductDto>> getProductsGroupCurrency() {

        List<Product> products = productRepository.findAll();

        Map<String, List<ProductDto>> groupedProducts = products.stream()
                .map(ProductMapper::toProductDto)
                .collect(Collectors.groupingBy(ProductDto::getCurrencyId));

        return groupedProducts;
        
    }
}
