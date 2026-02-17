package com.hackerrank.sample.infrastructure.adapter.output.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.hackerrank.sample.application.port.output.persistence.ProductPersistencePort;
import com.hackerrank.sample.domain.model.ProductDomain;
import com.hackerrank.sample.infrastructure.adapter.output.persistence.jpa.entity.ProductEntity;
import com.hackerrank.sample.infrastructure.adapter.output.persistence.jpa.repository.ProductJpaRepository;
import com.hackerrank.sample.infrastructure.adapter.output.persistence.mapper.ProductPersistenceMapper;

@Component
public class ProductPersistenceAdapter implements ProductPersistencePort {

    private final ProductJpaRepository repository;
    private final ProductPersistenceMapper mapper;

    public ProductPersistenceAdapter(ProductJpaRepository repository, 
                                     ProductPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ProductDomain save(ProductDomain product) {
        ProductEntity entity = mapper.toEntity(product);
        ProductEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<ProductDomain> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<ProductDomain> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDomain> findByTitleLikeIgnoreCase(String title) {
        return repository.findByTitleLikeIgnoreCase(title).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
