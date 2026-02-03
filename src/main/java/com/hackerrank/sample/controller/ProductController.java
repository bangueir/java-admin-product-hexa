package com.hackerrank.sample.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hackerrank.sample.dto.ProductDto;
import com.hackerrank.sample.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping(path = "/search", params = "title")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getProductByTitle(@RequestParam String title) {
        List<ProductDto> products = productService.getProductByTitle(title);
        return products;
    }

    @GetMapping(path = "/report/topvalue")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getProductWithHigherValue() {
        ProductDto product = productService.getProductWithHigherValue();
        return product;
    }

    @GetMapping(path = "/report/groupcurrency")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, List<ProductDto>> getProductsGroupCurrency() {
        Map<String, List<ProductDto>> resultMap = productService.getProductsGroupCurrency();
        return resultMap;
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createProduct(@RequestBody @Valid ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto updateProduct(@PathVariable Long id, @RequestBody @Valid ProductDto productDto) {
        return productService.updateProduct(id, productDto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
    }

}
