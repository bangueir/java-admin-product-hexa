package com.hackerrank.sample.mapper;

import org.junit.Assert;
import org.junit.Test;

import com.hackerrank.sample.dto.ProductDto;
import com.hackerrank.sample.enums.CurrencyTypes;
import com.hackerrank.sample.enums.ProductContiditions;
import com.hackerrank.sample.model.Product;

public class ProductMapperTest {

    @Test
    public void mapsProductToDto() {
        Product product = new Product();
        product.setTitle("Test Product");
        product.setPrice(19.99f);
        product.setCurrencyId(CurrencyTypes.USD);
        product.setAvailableQuantity(7);
        product.setCondition(ProductContiditions.NEW);
        product.setFreeShipping(true);
        product.setDescription("Desc");
        product.setPictureUrl("http://example.com/img.png");

        ProductDto dto = ProductMapper.toProductDto(product);

        Assert.assertNotNull(dto);
        Assert.assertEquals("Test Product", dto.getTitle());
        Assert.assertEquals(19.99f, dto.getPrice(), 0.0001f);
        Assert.assertEquals("USD", dto.getCurrencyId());
        Assert.assertEquals(Integer.valueOf(7), dto.getAvailableQuantity());
        Assert.assertEquals("NEW", dto.getCondition());
        Assert.assertTrue(dto.isFreeShipping());
        Assert.assertEquals("Desc", dto.getDescription());
        Assert.assertEquals("http://example.com/img.png", dto.getPictureUrl());
    }

    @Test
    public void mapsDtoToProduct() {
        ProductDto dto = new ProductDto();
        dto.setTitle("Another Product");
        dto.setPrice(42.5f);
        dto.setCurrencyId("COP");
        dto.setAvailableQuantity(3);
        dto.setCondition("USED");
        dto.setFreeShipping(false);
        dto.setDescription("Description");
        dto.setPictureUrl("http://example.com/another.png");

        Product product = ProductMapper.toProduct(dto);

        Assert.assertNotNull(product);
        Assert.assertEquals("Another Product", product.getTitle());
        Assert.assertEquals(42.5f, product.getPrice(), 0.0001f);
        Assert.assertEquals(CurrencyTypes.COP, product.getCurrencyId());
        Assert.assertEquals(Integer.valueOf(3), Integer.valueOf(product.getAvailableQuantity()));
        Assert.assertEquals(ProductContiditions.USED, product.getCondition());
        Assert.assertFalse(product.isFreeShipping());
        Assert.assertEquals("Description", product.getDescription());
        Assert.assertEquals("http://example.com/another.png", product.getPictureUrl());
    }

    @Test
    public void mapsNullSafely() {
        Assert.assertNull(ProductMapper.toProductDto(null));
        Assert.assertNull(ProductMapper.toProduct(null));
    }
}
