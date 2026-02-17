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

import com.hackerrank.sample.application.port.output.persistence.ProductPersistencePort;
import com.hackerrank.sample.application.service.ProductService;
import com.hackerrank.sample.domain.exception.BadResourceRequestException;
import com.hackerrank.sample.domain.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.domain.model.ProductDomain;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {

	@Mock
	private ProductPersistencePort productPersistencePort;

	@InjectMocks
	private ProductService productService;


	@Test
	public void deleteProductById_deletesWhenExists() {
		when(productPersistencePort.existsById(1L)).thenReturn(true);
		doNothing().when(productPersistencePort).deleteById(1L);

		productService.deleteProduct(1L);

		verify(productPersistencePort).deleteById(1L);
	}

	@Test
	public void deleteProductById_throwsWhenNotFound() {
		when(productPersistencePort.existsById(99L)).thenReturn(false);

		try {
			productService.deleteProduct(99L);
			Assert.fail("Expected NoSuchResourceFoundException");
		} catch (NoSuchResourceFoundException ex) {
			assertEquals("No product with given id found.", ex.getMessage());
		}

		verify(productPersistencePort, never()).deleteById(any());
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
		ProductDomain input = sampleProductDomain();
		ProductDomain saved = sampleProductDomain(10L);
		when(productPersistencePort.save(any(ProductDomain.class))).thenReturn(saved);

		ProductDomain result = productService.createProduct(input);

		assertNotNull(result);
		assertEquals(Long.valueOf(10L), result.getId());
		assertEquals("Test Product", result.getTitle());
		assertEquals(19.99f, result.getPrice(), 0.0001f);
		assertEquals("USD", result.getCurrencyId());
		assertEquals(7, result.getAvailableQuantity());
		assertEquals("NEW", result.getCondition());
		assertTrue(result.isFreeShipping());

		ArgumentCaptor<ProductDomain> captor = ArgumentCaptor.forClass(ProductDomain.class);
		verify(productPersistencePort).save(captor.capture());
		assertEquals("Test Product", captor.getValue().getTitle());
	}

	@Test
	public void getProductById_returnsDtoWhenFound() {
		when(productPersistencePort.findById(1L)).thenReturn(Optional.of(sampleProductDomain(1L)));

		ProductDomain result = productService.getProductById(1L);

		assertNotNull(result);
		assertEquals(Long.valueOf(1L), result.getId());
		assertEquals("Test Product", result.getTitle());
	}

	@Test
	public void getProductById_throwsWhenNotFound() {
		when(productPersistencePort.findById(2L)).thenReturn(Optional.empty());

		try {
			productService.getProductById(2L);
			Assert.fail("Expected NoSuchResourceFoundException");
		} catch (NoSuchResourceFoundException ex) {
			assertEquals("No product with given id found.", ex.getMessage());
		}
	}

	@Test
	public void getAllProducts_returnsMappedDtos() {
		when(productPersistencePort.findAll()).thenReturn(List.of(sampleProductDomain(1L), sampleProductDomain(2L)));

		List<ProductDomain> result = productService.getAllProducts();

		assertEquals(2, result.size());
		assertEquals(Long.valueOf(1L), result.get(0).getId());
		assertEquals(Long.valueOf(2L), result.get(1).getId());
	}

	@Test
	public void getProductByTitle_returnsMappedList() {
		when(productPersistencePort.findByTitleLikeIgnoreCase("Laptop"))
				.thenReturn(List.of(sampleProductDomain(1L), sampleProductDomain(2L)));

		List<ProductDomain> result = productService.searchByTitle("Laptop");

		assertEquals(2, result.size());
		assertEquals(Long.valueOf(1L), result.get(0).getId());
		assertEquals(Long.valueOf(2L), result.get(1).getId());
	}

	@Test
	public void getProductWithHigherValue_returnsMax() {
		ProductDomain low = sampleProductDomain(1L);
		low.setPrice(10.0f);
		ProductDomain high = sampleProductDomain(2L);
		high.setPrice(99.0f);
		when(productPersistencePort.findAll()).thenReturn(List.of(low, high));

		ProductDomain result = productService.getProductWithHigherValue();

		assertNotNull(result);
		assertEquals(Long.valueOf(2L), result.getId());
	}

	@Test
	public void getProductWithHigherValue_throwsWhenEmpty() {
		when(productPersistencePort.findAll()).thenReturn(List.of());

		try {
			productService.getProductWithHigherValue();
			Assert.fail("Expected NoSuchResourceFoundException");
		} catch (NoSuchResourceFoundException ex) {
			assertEquals("No product found.", ex.getMessage());
		}
	}

	@Test
	public void getProductsGroupCurrency_groupsByCurrency() {
		ProductDomain usd = sampleProductDomain(1L);
		usd.setCurrencyId("USD");
		ProductDomain cop = sampleProductDomain(2L);
		cop.setCurrencyId("COP");
		ProductDomain usd2 = sampleProductDomain(3L);
		usd2.setCurrencyId("USD");
		when(productPersistencePort.findAll()).thenReturn(List.of(usd, cop, usd2));

		var result = productService.getProductsGroupedByCurrency();

		assertEquals(2, result.size());
		assertEquals(2, result.get("USD").size());
		assertEquals(1, result.get("COP").size());
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
		when(productPersistencePort.existsById(1L)).thenReturn(false);

		try {
			productService.updateProduct(1L, sampleProductDomain());
			Assert.fail("Expected NoSuchResourceFoundException");
		} catch (NoSuchResourceFoundException ex) {
			assertEquals("No product with given id found.", ex.getMessage());
		}
	}

	@Test
	public void updateProduct_updatesAndReturnsDto() {
		when(productPersistencePort.existsById(1L)).thenReturn(true);
		when(productPersistencePort.save(any(ProductDomain.class))).thenReturn(sampleProductDomain(1L));

		ProductDomain result = productService.updateProduct(1L, sampleProductDomain());

		assertNotNull(result);
		assertEquals(Long.valueOf(1L), result.getId());
		assertEquals("Test Product", result.getTitle());
	}

	private ProductDomain sampleProductDomain(Long id) {
		ProductDomain product = new ProductDomain();
		product.setId(id);
		product.setTitle("Test Product");
		product.setPrice(19.99f);
		product.setCurrencyId("USD");
		product.setAvailableQuantity(7);
		product.setCondition("NEW");
		product.setFreeShipping(true);
		product.setDescription("Desc");
		product.setPictureUrl("http://example.com/img.png");
		return product;
	}

	private ProductDomain sampleProductDomain() {
		ProductDomain domain = new ProductDomain();
		domain.setTitle("Test Product");
		domain.setPrice(19.99f);
		domain.setCurrencyId("USD");
		domain.setAvailableQuantity(7);
		domain.setCondition("NEW");
		domain.setFreeShipping(true);
		domain.setDescription("Desc");
		domain.setPictureUrl("http://example.com/img.png");
		return domain;
	}
}
