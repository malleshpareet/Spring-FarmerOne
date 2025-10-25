package com.FarmerOne.FarmerOne.service;

import com.FarmerOne.FarmerOne.dto.ProductDTO;
import com.FarmerOne.FarmerOne.model.Product;
import com.FarmerOne.FarmerOne.repository.ProductRepository;
import com.FarmerOne.FarmerOne.util.CustomIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomIdGenerator idGenerator;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {
        // Given
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategoryId("1");
        productDTO.setProductName("Organic Tomatoes");
        productDTO.setSlug("organic-tomatoes");
        productDTO.setUnit("kg");
        productDTO.setDescription("Fresh organic tomatoes");

        Product product = new Product();
        product.setId("1");
        product.setCategoryId("1");
        product.setProductName("Organic Tomatoes");
        product.setSlug("organic-tomatoes");
        product.setUnit("kg");
        product.setDescription("Fresh organic tomatoes");
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        when(idGenerator.generateNextId()).thenReturn("1");
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // When
        ProductDTO result = productService.createProduct(productDTO);

        // Then
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("1", result.getCategoryId());
        assertEquals("Organic Tomatoes", result.getProductName());
        assertEquals("organic-tomatoes", result.getSlug());
        assertEquals("kg", result.getUnit());
        assertEquals("Fresh organic tomatoes", result.getDescription());
        
        verify(idGenerator, times(1)).generateNextId();
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testGetProductById() {
        // Given
        String productId = "1";
        Product product = new Product();
        product.setId(productId);
        product.setCategoryId("1");
        product.setProductName("Organic Tomatoes");
        product.setSlug("organic-tomatoes");
        product.setUnit("kg");
        product.setDescription("Fresh organic tomatoes");
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // When
        ProductDTO result = productService.getProductById(productId);

        // Then
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("1", result.getCategoryId());
        assertEquals("Organic Tomatoes", result.getProductName());
        assertEquals("organic-tomatoes", result.getSlug());
        assertEquals("kg", result.getUnit());
        assertEquals("Fresh organic tomatoes", result.getDescription());
    }

    @Test
    void testGetProductByIdNotFound() {
        // Given
        String productId = "1";
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When
        ProductDTO result = productService.getProductById(productId);

        // Then
        assertNull(result);
    }

    @Test
    void testGetAllProducts() {
        // Given
        Product product1 = new Product();
        product1.setId("1");
        product1.setProductName("Product 1");
        product1.setSlug("product-1");

        Product product2 = new Product();
        product2.setId("2");
        product2.setProductName("Product 2");
        product2.setSlug("product-2");

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        when(productRepository.findAll()).thenReturn(products);

        // When
        List<ProductDTO> result = productService.getAllProducts();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("2", result.get(1).getId());
    }

    @Test
    void testGetProductsByCategoryId() {
        // Given
        String categoryId = "1";
        Product product1 = new Product();
        product1.setId("1");
        product1.setCategoryId(categoryId);
        product1.setProductName("Product 1");
        product1.setSlug("product-1");

        Product product2 = new Product();
        product2.setId("2");
        product2.setCategoryId(categoryId);
        product2.setProductName("Product 2");
        product2.setSlug("product-2");

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        when(productRepository.findByCategoryId(categoryId)).thenReturn(products);

        // When
        List<ProductDTO> result = productService.getProductsByCategoryId(categoryId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(categoryId, result.get(0).getCategoryId());
        assertEquals(categoryId, result.get(1).getCategoryId());
    }

    @Test
    void testUpdateProduct() {
        // Given
        String productId = "1";
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategoryId("1");
        productDTO.setProductName("Updated Product");
        productDTO.setSlug("updated-product");
        productDTO.setUnit("piece");
        productDTO.setDescription("Updated Description");

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setCategoryId("1");
        existingProduct.setProductName("Original Product");
        existingProduct.setSlug("original-product");
        existingProduct.setUnit("kg");
        existingProduct.setDescription("Original Description");
        existingProduct.setCreatedAt(LocalDateTime.now().minusDays(1)); // Created yesterday

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setCategoryId("1");
        updatedProduct.setProductName("Updated Product");
        updatedProduct.setSlug("updated-product");
        updatedProduct.setUnit("piece");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setCreatedAt(existingProduct.getCreatedAt()); // Should preserve original creation time
        updatedProduct.setUpdatedAt(LocalDateTime.now());

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // When
        ProductDTO result = productService.updateProduct(productId, productDTO);

        // Then
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("1", result.getCategoryId());
        assertEquals("Updated Product", result.getProductName());
        assertEquals("updated-product", result.getSlug());
        assertEquals("piece", result.getUnit());
        assertEquals("Updated Description", result.getDescription());
        // Check that creation time was preserved
        assertEquals(existingProduct.getCreatedAt(), result.getCreatedAt());
        // Check that update time was set
        assertNotNull(result.getUpdatedAt());
        
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProductNotFound() {
        // Given
        String productId = "1";
        ProductDTO productDTO = new ProductDTO();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When
        ProductDTO result = productService.updateProduct(productId, productDTO);

        // Then
        assertNull(result);
    }

    @Test
    void testDeleteProduct() {
        // Given
        String productId = "1";
        when(productRepository.existsById(productId)).thenReturn(true);

        // When
        boolean result = productService.deleteProduct(productId);

        // Then
        assertTrue(result);
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void testDeleteProductNotFound() {
        // Given
        String productId = "1";
        when(productRepository.existsById(productId)).thenReturn(false);

        // When
        boolean result = productService.deleteProduct(productId);

        // Then
        assertFalse(result);
        verify(productRepository, times(0)).deleteById(productId);
    }
}