package com.hackerrank.sample.application.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hackerrank.sample.application.port.input.usecase.CreateProductUseCase;
import com.hackerrank.sample.application.port.input.usecase.DeleteProductUseCase;
import com.hackerrank.sample.application.port.input.usecase.GetAllProductsUseCase;
import com.hackerrank.sample.application.port.input.usecase.GetProductUseCase;
import com.hackerrank.sample.application.port.input.usecase.GetProductWithHigherValueUseCase;
import com.hackerrank.sample.application.port.input.usecase.GetProductsGroupedByCurrencyUseCase;
import com.hackerrank.sample.application.port.input.usecase.SearchProductsByTitleUseCase;
import com.hackerrank.sample.application.port.input.usecase.UpdateProductUseCase;
import com.hackerrank.sample.application.port.output.persistence.ProductPersistencePort;
import com.hackerrank.sample.domain.exception.BadResourceRequestException;
import com.hackerrank.sample.domain.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.domain.model.ProductDomain;

@Service
@Transactional
public class ProductService implements 
        CreateProductUseCase,
        GetProductUseCase,
        GetAllProductsUseCase,
        SearchProductsByTitleUseCase,
        UpdateProductUseCase,
        DeleteProductUseCase,
        GetProductWithHigherValueUseCase,
        GetProductsGroupedByCurrencyUseCase {

    private final ProductPersistencePort persistencePort;

    public ProductService(ProductPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public ProductDomain createProduct(ProductDomain product) {
        if (product == null) {
            throw new BadResourceRequestException("Product payload is required.");
        }
        return persistencePort.save(product);
    }

    @Override
    public ProductDomain getProductById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(() -> new NoSuchResourceFoundException("No product with given id found."));
    }

    @Override
    public List<ProductDomain> getAllProducts() {
        return persistencePort.findAll();
    }

    @Override
    public List<ProductDomain> searchByTitle(String title) {
        return persistencePort.findByTitleLikeIgnoreCase(title);
    }

    @Override
    public ProductDomain updateProduct(Long id, ProductDomain product) {
        if (product == null) {
            throw new BadResourceRequestException("Product payload is required.");
        }

        if (!persistencePort.existsById(id)) {
            throw new NoSuchResourceFoundException("No product with given id found.");
        }

        product.setId(id);
        return persistencePort.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!persistencePort.existsById(id)) {
            throw new NoSuchResourceFoundException("No product with given id found.");
        }
        persistencePort.deleteById(id);
    }

    @Override
    public ProductDomain getProductWithHigherValue() {
        List<ProductDomain> products = persistencePort.findAll();
        
        return products.stream()
                .max((p1, p2) -> Float.compare(p1.getPrice(), p2.getPrice()))
                .orElseThrow(() -> new NoSuchResourceFoundException("No product found."));
    }

    @Override
    public Map<String, List<ProductDomain>> getProductsGroupedByCurrency() {
        List<ProductDomain> products = persistencePort.findAll();
        
        return products.stream()
                .collect(Collectors.groupingBy(ProductDomain::getCurrencyId));
    }
}
