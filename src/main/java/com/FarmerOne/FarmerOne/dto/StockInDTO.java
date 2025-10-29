package com.FarmerOne.FarmerOne.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StockInDTO {
    private String id; // Using String ID as per the application pattern
    
    private String mediatorId; // Reference to Profile (mediator)
    
    private String farmerId; // Reference to Farmer
    
    private BigDecimal totalAmount;
    
    private BigDecimal serviceChargePercentage;
    
    private BigDecimal serviceChargeAmount;
    
    private BigDecimal payableAmount;
    
    private String paymentStatus;
    
    private String billId; // Unique identifier
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private BigDecimal transportCharge;
    
    private BigDecimal loadingCharge;
    
    private BigDecimal laborCharge;
    
    private BigDecimal totalCharges;
}