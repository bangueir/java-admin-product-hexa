package com.hackerrank.sample.infrastructure.adapter.input.rest.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.hackerrank.sample.application.port.input.usecase.CreateProductUseCase;
import com.hackerrank.sample.application.port.input.usecase.DeleteProductUseCase;
import com.hackerrank.sample.application.port.input.usecase.GetAllProductsUseCase;
import com.hackerrank.sample.application.port.input.usecase.GetProductUseCase;
import com.hackerrank.sample.application.port.input.usecase.GetProductWithHigherValueUseCase;
import com.hackerrank.sample.application.port.input.usecase.GetProductsGroupedByCurrencyUseCase;
import com.hackerrank.sample.application.port.input.usecase.SearchProductsByTitleUseCase;
import com.hackerrank.sample.application.port.input.usecase.UpdateProductUseCase;
import com.hackerrank.sample.domain.model.ProductDomain;
import com.hackerrank.sample.infrastructure.adapter.input.rest.dto.ProductRestDto;
import com.hackerrank.sample.infrastructure.adapter.input.rest.mapper.ProductRestMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final GetAllProductsUseCase getAllProductsUseCase;
    private final SearchProductsByTitleUseCase searchProductsByTitleUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final GetProductWithHigherValueUseCase getProductWithHigherValueUseCase;
    private final GetProductsGroupedByCurrencyUseCase getProductsGroupedByCurrencyUseCase;
    private final ProductRestMapper mapper;

    public ProductController(
            CreateProductUseCase createProductUseCase,
            GetProductUseCase getProductUseCase,
            GetAllProductsUseCase getAllProductsUseCase,
            SearchProductsByTitleUseCase searchProductsByTitleUseCase,
            UpdateProductUseCase updateProductUseCase,
            DeleteProductUseCase deleteProductUseCase,
            GetProductWithHigherValueUseCase getProductWithHigherValueUseCase,
            GetProductsGroupedByCurrencyUseCase getProductsGroupedByCurrencyUseCase,
            ProductRestMapper mapper) {
        this.createProductUseCase = createProductUseCase;
        this.getProductUseCase = getProductUseCase;
        this.getAllProductsUseCase = getAllProductsUseCase;
        this.searchProductsByTitleUseCase = searchProductsByTitleUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
        this.getProductWithHigherValueUseCase = getProductWithHigherValueUseCase;
        this.getProductsGroupedByCurrencyUseCase = getProductsGroupedByCurrencyUseCase;
        this.mapper = mapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductRestDto> getAllProducts() {
        return getAllProductsUseCase.getAllProducts().stream()
                .map(mapper::toRestDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductRestDto getProductById(@PathVariable Long id) {
        ProductDomain product = getProductUseCase.getProductById(id);
        return mapper.toRestDto(product);
    }

    @GetMapping(path = "/search", params = "title")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductRestDto> getProductByTitle(@RequestParam String title) {
        return searchProductsByTitleUseCase.searchByTitle(title).stream()
                .map(mapper::toRestDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/report/topvalue")
    @ResponseStatus(HttpStatus.OK)
    public ProductRestDto getProductWithHigherValue() {
        ProductDomain product = getProductWithHigherValueUseCase.getProductWithHigherValue();
        return mapper.toRestDto(product);
    }

    @GetMapping("/report/groupcurrency")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, List<ProductRestDto>> getProductsGroupCurrency() {
        Map<String, List<ProductDomain>> grouped = 
                getProductsGroupedByCurrencyUseCase.getProductsGroupedByCurrency();
        
        return grouped.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(mapper::toRestDto)
                                .collect(Collectors.toList())
                ));
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductRestDto createProduct(@RequestBody @Valid ProductRestDto productDto) {
        ProductDomain domain = mapper.toDomain(productDto);
        ProductDomain created = createProductUseCase.createProduct(domain);
        return mapper.toRestDto(created);
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ProductRestDto updateProduct(@PathVariable Long id, 
                                        @RequestBody @Valid ProductRestDto productDto) {
        ProductDomain domain = mapper.toDomain(productDto);
        ProductDomain updated = updateProductUseCase.updateProduct(id, domain);
        return mapper.toRestDto(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable Long id) {
        deleteProductUseCase.deleteProduct(id);
    }
}
