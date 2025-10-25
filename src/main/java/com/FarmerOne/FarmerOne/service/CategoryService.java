package com.FarmerOne.FarmerOne.service;

import com.FarmerOne.FarmerOne.dto.CategoryDTO;
import com.FarmerOne.FarmerOne.model.Category;
import com.FarmerOne.FarmerOne.repository.CategoryRepository;
import com.FarmerOne.FarmerOne.util.CustomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private CustomIdGenerator idGenerator;
    
    // Convert Category entity to CategoryDTO
    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setCategoryName(category.getCategoryName());
        categoryDTO.setSlug(category.getSlug());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setParentId(category.getParentId());
        categoryDTO.setSortOrder(category.getSortOrder());
        categoryDTO.setIsActive(category.getIsActive());
        categoryDTO.setCreatedAt(category.getCreatedAt());
        categoryDTO.setUpdatedAt(category.getUpdatedAt());
        return categoryDTO;
    }
    
    // Convert CategoryDTO to Category entity
    private Category convertToEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        // For new categories, we'll use our custom ID generator
        // For existing categories, the ID will be preserved
        if (categoryDTO.getId() != null) {
            category.setId(categoryDTO.getId());
        }
        category.setCategoryName(categoryDTO.getCategoryName());
        category.setSlug(categoryDTO.getSlug());
        category.setDescription(categoryDTO.getDescription());
        category.setParentId(categoryDTO.getParentId());
        category.setSortOrder(categoryDTO.getSortOrder());
        category.setIsActive(categoryDTO.getIsActive());
        category.setCreatedAt(categoryDTO.getCreatedAt());
        category.setUpdatedAt(categoryDTO.getUpdatedAt());
        return category;
    }
    
    // Get all categories
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get category by ID
    public CategoryDTO getCategoryById(String id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(this::convertToDTO).orElse(null);
    }
    
    // Create new category
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        // Generate custom ID if not provided
        if (categoryDTO.getId() == null) {
            categoryDTO.setId(idGenerator.generateNextId());
        }
        
        // Set timestamps
        categoryDTO.setCreatedAt(LocalDateTime.now());
        categoryDTO.setUpdatedAt(LocalDateTime.now());
        
        Category category = convertToEntity(categoryDTO);
        Category savedCategory = categoryRepository.save(category);
        return convertToDTO(savedCategory);
    }
    
    // Update category
    public CategoryDTO updateCategory(String id, CategoryDTO categoryDTO) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            // Preserve original creation time
            categoryDTO.setCreatedAt(existingCategory.get().getCreatedAt());
            // Update last modified time
            categoryDTO.setUpdatedAt(LocalDateTime.now());
            
            Category category = convertToEntity(categoryDTO);
            category.setId(id); // Ensure ID remains the same
            Category updatedCategory = categoryRepository.save(category);
            return convertToDTO(updatedCategory);
        }
        return null;
    }
    
    // Delete category
    public boolean deleteCategory(String id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}