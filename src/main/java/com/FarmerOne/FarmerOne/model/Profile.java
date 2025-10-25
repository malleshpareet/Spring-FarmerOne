package com.FarmerOne.FarmerOne.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Document(collection = "profiles")
public class Profile {
    @Id
    private String id; // Keep as String to work with MongoDB ObjectId
    
    private String userId;
    
    private String fullName;
    
    private String mobileNumber;
    
    private BigDecimal defaultServiceCharge = new BigDecimal("5.00");
    
    private String languagePref = "english";
    
    private ZonedDateTime createdAt = ZonedDateTime.now();
    
    private ZonedDateTime updatedAt = ZonedDateTime.now();
    
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