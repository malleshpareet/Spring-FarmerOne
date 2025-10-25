package com.FarmerOne.FarmerOne.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.ZonedDateTime;

@Data
@Document(collection = "farmers")
public class Farmer {
    @Id
    private String id;
    
    private String mediatorId; // Changed from UUID to String for numeric IDs
    
    private String fullName;
    
    private String villageName;
    
    private ZonedDateTime createdAt = ZonedDateTime.now();
    
    private String mobileNumber;
    
    private String farmerCode;
}