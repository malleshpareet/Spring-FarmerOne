package com.FarmerOne.FarmerOne.service;

import com.FarmerOne.FarmerOne.dto.ProductDTO;
import com.FarmerOne.FarmerOne.exception.CategoryNotFoundException;
import com.FarmerOne.FarmerOne.model.Product;
import com.FarmerOne.FarmerOne.repository.ProductRepository;
import com.FarmerOne.FarmerOne.repository.CategoryRepository;
import com.FarmerOne.FarmerOne.util.CustomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private CustomIdGenerator idGenerator;
    
    // Convert Product entity to ProductDTO
    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setCategoryId(product.getCategoryId());
        productDTO.setProductName(product.getProductName());
        productDTO.setSlug(product.getSlug());
        productDTO.setUnit(product.getUnit());
        productDTO.setDescription(product.getDescription());
        productDTO.setIsActive(product.getIsActive());
        productDTO.setCreatedAt(product.getCreatedAt());
        productDTO.setUpdatedAt(product.getUpdatedAt());
        return productDTO;
    }
    
    // Convert ProductDTO to Product entity
    private Product convertToEntity(ProductDTO productDTO) {
        Product product = new Product();
        // For new products, we'll use our custom ID generator
        // For existing products, the ID will be preserved
        if (productDTO.getId() != null) {
            product.setId(productDTO.getId());
        }
        product.setCategoryId(productDTO.getCategoryId());
        product.setProductName(productDTO.getProductName());
        product.setSlug(productDTO.getSlug());
        product.setUnit(productDTO.getUnit());
        product.setDescription(productDTO.getDescription());
        product.setIsActive(productDTO.getIsActive());
        product.setCreatedAt(productDTO.getCreatedAt());
        product.setUpdatedAt(productDTO.getUpdatedAt());
        return product;
    }
    
    // Get all products
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get product by ID
    public ProductDTO getProductById(String id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(this::convertToDTO).orElse(null);
    }
    
    // Get products by category ID
    public List<ProductDTO> getProductsByCategoryId(String categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Create new product
    public ProductDTO createProduct(ProductDTO productDTO) {
        // Validate that the category exists
        if (productDTO.getCategoryId() != null && 
            !categoryRepository.existsById(productDTO.getCategoryId())) {
            throw new CategoryNotFoundException("No such Category in the database");
        }
        
        // Generate custom ID if not provided
        if (productDTO.getId() == null) {
            productDTO.setId(idGenerator.generateNextId());
        }
        
        // Set timestamps
        productDTO.setCreatedAt(LocalDateTime.now());
        productDTO.setUpdatedAt(LocalDateTime.now());
        
        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }
    
    // Update product
    public ProductDTO updateProduct(String id, ProductDTO productDTO) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            // Validate that the category exists
            if (productDTO.getCategoryId() != null && 
                !categoryRepository.existsById(productDTO.getCategoryId())) {
                throw new CategoryNotFoundException("No such Category in the database");
            }
            
            // Preserve original creation time
            productDTO.setCreatedAt(existingProduct.get().getCreatedAt());
            // Update last modified time
            productDTO.setUpdatedAt(LocalDateTime.now());
            
            Product product = convertToEntity(productDTO);
            product.setId(id); // Ensure ID remains the same
            Product updatedProduct = productRepository.save(product);
            return convertToDTO(updatedProduct);
        }
        return null;
    }
    
    // Delete product
    public boolean deleteProduct(String id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}