package com.FarmerOne.FarmerOne.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "categories")
public class Category {
    @Id
    private String id; // Using String ID as per the application pattern
    
    private String categoryName;
    
    private String slug;
    
    private String description;
    
    private String parentId; // Reference to parent category (String as per pattern)
    
    private Integer sortOrder = 0;
    
    private Boolean isActive = true;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime updatedAt = LocalDateTime.now();
}