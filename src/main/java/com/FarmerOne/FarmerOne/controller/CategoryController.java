package com.FarmerOne.FarmerOne.controller;

import com.FarmerOne.FarmerOne.dto.CategoryDTO;
import com.FarmerOne.FarmerOne.dto.ProductDTO;
import com.FarmerOne.FarmerOne.service.CategoryService;
import com.FarmerOne.FarmerOne.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ProductService productService;
    
    // Get all categories
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    
    // Get category by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable String id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        if (category != null) {
            return new ResponseEntity<>(category, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Category not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    // Create new category
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("id", createdCategory.getId());
            response.put("message", "Category created successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to create category: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Update category
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable String id, @RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
            if (updatedCategory != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("id", updatedCategory.getId());
                response.put("message", "Category updated successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Category not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to update category: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Delete category
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable String id) {
        boolean deleted = categoryService.deleteCategory(id);
        Map<String, Object> response = new HashMap<>();
        if (deleted) {
            response.put("status", "success");
            response.put("id", id);
            response.put("message", "Category deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("status", "error");
            response.put("message", "Category not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    // Get products by category ID
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<List<ProductDTO>> getProductsByCategoryId(@PathVariable String categoryId) {
        List<ProductDTO> products = productService.getProductsByCategoryId(categoryId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}