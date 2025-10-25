package com.FarmerOne.FarmerOne.dto;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class FarmerDTO {
    private String id;
    
    private String mediatorId; // Changed from UUID to String for numeric IDs
    
    private String fullName;
    
    private String villageName;
    
    private ZonedDateTime createdAt;
    
    private String mobileNumber;
    
    private String farmerCode;
}