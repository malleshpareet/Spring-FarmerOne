package com.FarmerOne.FarmerOne.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CategoryDTO {
    private String id; // Using String ID as per the application pattern
    
    private String categoryName;
    
    private String slug;
    
    private String description;
    
    private String parentId; // Reference to parent category (String as per pattern)
    
    private Integer sortOrder = 0;
    
    private Boolean isActive = true;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}