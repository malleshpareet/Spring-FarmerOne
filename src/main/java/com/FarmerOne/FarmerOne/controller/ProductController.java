package com.FarmerOne.FarmerOne.controller;

import com.FarmerOne.FarmerOne.dto.ProductDTO;
import com.FarmerOne.FarmerOne.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    // Get all products
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    
    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        ProductDTO product = productService.getProductById(id);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Product not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    // Create new product
    @PostMapping
    public ResponseEntity<Map<String, Object>> createProduct(@RequestBody ProductDTO productDTO) {
        try {
            ProductDTO createdProduct = productService.createProduct(productDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("id", createdProduct.getId());
            response.put("message", "Product created successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to create product: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Update product
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody ProductDTO productDTO) {
        try {
            ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
            if (updatedProduct != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("id", updatedProduct.getId());
                response.put("message", "Product updated successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Product not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to update product: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable String id) {
        boolean deleted = productService.deleteProduct(id);
        Map<String, Object> response = new HashMap<>();
        if (deleted) {
            response.put("status", "success");
            response.put("id", id);
            response.put("message", "Product deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("status", "error");
            response.put("message", "Product not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}