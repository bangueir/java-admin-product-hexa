package com.hackerrank.sample.service.Impl;

import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;

import com.hackerrank.sample.dto.ProductDto;
import com.hackerrank.sample.enums.CurrencyTypes;
import com.hackerrank.sample.enums.ProductContiditions;
import com.hackerrank.sample.exception.BadResourceRequestException;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.repository.ProductRepository;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductServiceImpl productService;

	@Test
	public void deleteProductById_deletesWhenExists() {
		when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct(1L)));
		doNothing().when(productRepository).deleteById(1L);

		productService.deleteProductById(1L);

		verify(productRepository).deleteById(1L);
	}

	@Test
	public void deleteProductById_throwsWhenNotFound() {
		when(productRepository.findById(99L)).thenReturn(Optional.empty());

		try {
			productService.deleteProductById(99L);
			Assert.fail("Expected NoSuchResourceFoundException");
		} catch (NoSuchResourceFoundException ex) {
			assertEquals("No product with given id found.", ex.getMessage());
		}

		verify(productRepository, never()).deleteById(any());
	}

	@Test
	public void createProduct_throwsWhenNull() {
		try {
			productService.createProduct(null);
			Assert.fail("Expected BadResourceRequestException");
		} catch (BadResourceRequestException ex) {
			assertEquals("Product payload is required.", ex.getMessage());
		}
	}

	@Test
	public void createProduct_savesAndReturnsDto() {
		ProductDto input = sampleProductDto();
		Product saved = sampleProduct(10L);
		when(productRepository.save(any(Product.class))).thenReturn(saved);

		ProductDto result = productService.createProduct(input);

		assertNotNull(result);
		assertEquals(Long.valueOf(10L), result.getId());
		assertEquals("Test Product", result.getTitle());
		assertEquals(19.99f, result.getPrice(), 0.0001f);
		assertEquals("USD", result.getCurrencyId());
		assertEquals(Integer.valueOf(7), result.getAvailableQuantity());
		assertEquals("NEW", result.getCondition());
		assertTrue(result.isFreeShipping());

		ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
		verify(productRepository).save(captor.capture());
		assertEquals("Test Product", captor.getValue().getTitle());
	}

	@Test
	public void getProductById_returnsDtoWhenFound() {
		when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct(1L)));

		ProductDto result = productService.getProductById(1L);

		assertNotNull(result);
		assertEquals(Long.valueOf(1L), result.getId());
		assertEquals("Test Product", result.getTitle());
	}

	@Test
	public void getProductById_throwsWhenNotFound() {
		when(productRepository.findById(2L)).thenReturn(Optional.empty());

		try {
			productService.getProductById(2L);
			Assert.fail("Expected NoSuchResourceFoundException");
		} catch (NoSuchResourceFoundException ex) {
			assertEquals("No product with given id found.", ex.getMessage());
		}
	}

	@Test
	public void getAllProducts_returnsMappedDtos() {
		when(productRepository.findAll()).thenReturn(List.of(sampleProduct(1L), sampleProduct(2L)));

		List<ProductDto> result = productService.getAllProducts();

		assertEquals(2, result.size());
		assertEquals(Long.valueOf(1L), result.get(0).getId());
		assertEquals(Long.valueOf(2L), result.get(1).getId());
	}

	@Test
	public void updateProduct_throwsWhenNull() {
		try {
			productService.updateProduct(1L, null);
			Assert.fail("Expected BadResourceRequestException");
		} catch (BadResourceRequestException ex) {
			assertEquals("Product payload is required.", ex.getMessage());
		}
	}

	@Test
	public void updateProduct_throwsWhenNotFound() {
		when(productRepository.findById(1L)).thenReturn(Optional.empty());

		try {
			productService.updateProduct(1L, sampleProductDto());
			Assert.fail("Expected NoSuchResourceFoundException");
		} catch (NoSuchResourceFoundException ex) {
			assertEquals("No product with given id found.", ex.getMessage());
		}
	}

	@Test
	public void updateProduct_updatesAndReturnsDto() {
		when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct(1L)));
		when(productRepository.save(any(Product.class))).thenReturn(sampleProduct(1L));

		ProductDto result = productService.updateProduct(1L, sampleProductDto());

		assertNotNull(result);
		assertEquals(Long.valueOf(1L), result.getId());
		assertEquals("Test Product", result.getTitle());
	}

	private Product sampleProduct(Long id) {
		Product product = new Product();
		product.setId(id);
		product.setTitle("Test Product");
		product.setPrice(19.99f);
		product.setCurrencyId(CurrencyTypes.USD);
		product.setAvailableQuantity(7);
		product.setCondition(ProductContiditions.NEW);
		product.setFreeShipping(true);
		product.setDescription("Desc");
		product.setPictureUrl("http://example.com/img.png");
		return product;
	}

	private ProductDto sampleProductDto() {
		ProductDto dto = new ProductDto();
		dto.setTitle("Test Product");
		dto.setPrice(19.99f);
		dto.setCurrencyId("USD");
		dto.setAvailableQuantity(7);
		dto.setCondition("NEW");
		dto.setFreeShipping(true);
		dto.setDescription("Desc");
		dto.setPictureUrl("http://example.com/img.png");
		return dto;
	}
}
