package com.FarmerOne.FarmerOne.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductDTO {
    private String id; // Using String ID as per the application pattern
    
    private String categoryId; // Reference to category (String as per pattern)
    
    private String productName;
    
    private String slug;
    
    private String unit;
    
    private String description;
    
    private Boolean isActive = true;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}