package com.FarmerOne.FarmerOne.dto;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class BuyerDTO {
    private String id;
    
    private String mediatorId; // Using String for numeric IDs to avoid UUID issues
    
    private String fullName;
    
    private ZonedDateTime createdAt;
    
    private String mobileNumber;
    
    private String buyerCode;
}