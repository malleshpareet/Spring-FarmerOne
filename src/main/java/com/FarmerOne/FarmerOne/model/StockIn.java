package com.FarmerOne.FarmerOne.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "stock_ins")
public class StockIn {
    @Id
    private String id; // Using String ID as per the application pattern
    
    private String mediatorId; // Reference to Profile (mediator)
    
    private String farmerId; // Reference to Farmer
    
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    private BigDecimal serviceChargePercentage;
    
    private BigDecimal serviceChargeAmount = BigDecimal.ZERO;
    
    private BigDecimal payableAmount = BigDecimal.ZERO;
    
    private String paymentStatus = "pending"; // Default value
    
    private String billId; // Unique identifier
    
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    private BigDecimal transportCharge = BigDecimal.ZERO;
    
    private BigDecimal loadingCharge = BigDecimal.ZERO;
    
    private BigDecimal laborCharge = BigDecimal.ZERO;
    
    private BigDecimal totalCharges = BigDecimal.ZERO;
}