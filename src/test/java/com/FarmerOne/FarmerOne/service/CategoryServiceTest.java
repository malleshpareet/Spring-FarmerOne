package com.FarmerOne.FarmerOne.service;

import com.FarmerOne.FarmerOne.dto.CategoryDTO;
import com.FarmerOne.FarmerOne.model.Category;
import com.FarmerOne.FarmerOne.repository.CategoryRepository;
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

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CustomIdGenerator idGenerator;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCategory() {
        // Given
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("Test Category");
        categoryDTO.setSlug("test-category");
        categoryDTO.setDescription("Test Description");

        Category category = new Category();
        category.setId("1");
        category.setCategoryName("Test Category");
        category.setSlug("test-category");
        category.setDescription("Test Description");
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        when(idGenerator.generateNextId()).thenReturn("1");
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // When
        CategoryDTO result = categoryService.createCategory(categoryDTO);

        // Then
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Test Category", result.getCategoryName());
        assertEquals("test-category", result.getSlug());
        assertEquals("Test Description", result.getDescription());
        
        verify(idGenerator, times(1)).generateNextId();
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testGetCategoryById() {
        // Given
        String categoryId = "1";
        Category category = new Category();
        category.setId(categoryId);
        category.setCategoryName("Test Category");
        category.setSlug("test-category");
        category.setDescription("Test Description");
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When
        CategoryDTO result = categoryService.getCategoryById(categoryId);

        // Then
        assertNotNull(result);
        assertEquals(categoryId, result.getId());
        assertEquals("Test Category", result.getCategoryName());
        assertEquals("test-category", result.getSlug());
        assertEquals("Test Description", result.getDescription());
    }

    @Test
    void testGetCategoryByIdNotFound() {
        // Given
        String categoryId = "1";
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When
        CategoryDTO result = categoryService.getCategoryById(categoryId);

        // Then
        assertNull(result);
    }

    @Test
    void testGetAllCategories() {
        // Given
        Category category1 = new Category();
        category1.setId("1");
        category1.setCategoryName("Category 1");
        category1.setSlug("category-1");

        Category category2 = new Category();
        category2.setId("2");
        category2.setCategoryName("Category 2");
        category2.setSlug("category-2");

        List<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);

        when(categoryRepository.findAll()).thenReturn(categories);

        // When
        List<CategoryDTO> result = categoryService.getAllCategories();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("2", result.get(1).getId());
    }

    @Test
    void testUpdateCategory() {
        // Given
        String categoryId = "1";
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("Updated Category");
        categoryDTO.setSlug("updated-category");
        categoryDTO.setDescription("Updated Description");

        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setCategoryName("Original Category");
        existingCategory.setSlug("original-category");
        existingCategory.setDescription("Original Description");
        existingCategory.setCreatedAt(LocalDateTime.now().minusDays(1)); // Created yesterday

        Category updatedCategory = new Category();
        updatedCategory.setId(categoryId);
        updatedCategory.setCategoryName("Updated Category");
        updatedCategory.setSlug("updated-category");
        updatedCategory.setDescription("Updated Description");
        updatedCategory.setCreatedAt(existingCategory.getCreatedAt()); // Should preserve original creation time
        updatedCategory.setUpdatedAt(LocalDateTime.now());

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        // When
        CategoryDTO result = categoryService.updateCategory(categoryId, categoryDTO);

        // Then
        assertNotNull(result);
        assertEquals(categoryId, result.getId());
        assertEquals("Updated Category", result.getCategoryName());
        assertEquals("updated-category", result.getSlug());
        assertEquals("Updated Description", result.getDescription());
        // Check that creation time was preserved
        assertEquals(existingCategory.getCreatedAt(), result.getCreatedAt());
        // Check that update time was set
        assertNotNull(result.getUpdatedAt());
        
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdateCategoryNotFound() {
        // Given
        String categoryId = "1";
        CategoryDTO categoryDTO = new CategoryDTO();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When
        CategoryDTO result = categoryService.updateCategory(categoryId, categoryDTO);

        // Then
        assertNull(result);
    }

    @Test
    void testDeleteCategory() {
        // Given
        String categoryId = "1";
        when(categoryRepository.existsById(categoryId)).thenReturn(true);

        // When
        boolean result = categoryService.deleteCategory(categoryId);

        // Then
        assertTrue(result);
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    void testDeleteCategoryNotFound() {
        // Given
        String categoryId = "1";
        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        // When
        boolean result = categoryService.deleteCategory(categoryId);

        // Then
        assertFalse(result);
        verify(categoryRepository, times(0)).deleteById(categoryId);
    }
}