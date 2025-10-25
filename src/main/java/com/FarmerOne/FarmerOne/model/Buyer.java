package com.FarmerOne.FarmerOne.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.ZonedDateTime;

@Data
@Document(collection = "buyers")
public class Buyer {
    @Id
    private String id;
    
    private String mediatorId; // Using String for numeric IDs to avoid UUID issues
    
    private String fullName;
    
    private ZonedDateTime createdAt = ZonedDateTime.now();
    
    private String mobileNumber;
    
    private String buyerCode;
}