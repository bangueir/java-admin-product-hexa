package com.hackerrank.sample.infrastructure.adapter.output.persistence.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hackerrank.sample.infrastructure.adapter.output.persistence.jpa.entity.ProductEntity;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    
    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<ProductEntity> findByTitleLikeIgnoreCase(@Param("title") String title);
}
