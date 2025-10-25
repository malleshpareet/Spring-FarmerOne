package com.FarmerOne.FarmerOne.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "products")
public class Product {
    @Id
    private String id; // Using String ID as per the application pattern
    
    private String categoryId; // Reference to category (String as per pattern)
    
    private String productName;
    
    private String slug;
    
    private String unit;
    
    private String description;
    
    private Boolean isActive = true;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime updatedAt = LocalDateTime.now();
}