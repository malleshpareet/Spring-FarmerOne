package com.FarmerOne.FarmerOne.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class ProfileDTO {
    private String id; // Changed back to String
    
    private String userId;
    
    private String fullName;
    
    private String mobileNumber;
    
    private BigDecimal defaultServiceCharge;
    
    private String languagePref;
    
    private ZonedDateTime createdAt;
    
    private ZonedDateTime updatedAt;
    
    // Optional fields
    private String shopName;
    
    private String mandiName;
    
    private String businessName;
    
    private String gstNumber;
    
    private String addressLine1;
    
    private String addressLine2;
    
    private String city;
    
    private String state;
    
    private String pincode;
    
    private String email;
}