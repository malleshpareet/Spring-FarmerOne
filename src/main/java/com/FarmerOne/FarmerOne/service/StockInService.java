package com.FarmerOne.FarmerOne.service;

import com.FarmerOne.FarmerOne.dto.StockInDTO;
import com.FarmerOne.FarmerOne.exception.FarmerNotFoundException;
import com.FarmerOne.FarmerOne.exception.MediatorNotFoundException;
import com.FarmerOne.FarmerOne.model.StockIn;
import com.FarmerOne.FarmerOne.repository.FarmerRepository;
import com.FarmerOne.FarmerOne.repository.ProfileRepository;
import com.FarmerOne.FarmerOne.repository.StockInRepository;
import com.FarmerOne.FarmerOne.util.CustomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockInService {
    
    @Autowired
    private StockInRepository stockInRepository;
    
    @Autowired
    private FarmerRepository farmerRepository;
    
    @Autowired
    private ProfileRepository profileRepository;
    
    @Autowired
    private CustomIdGenerator idGenerator;
    
    // Convert StockIn entity to StockInDTO
    private StockInDTO convertToDTO(StockIn stockIn) {
        StockInDTO stockInDTO = new StockInDTO();
        stockInDTO.setId(stockIn.getId());
        stockInDTO.setMediatorId(stockIn.getMediatorId());
        stockInDTO.setFarmerId(stockIn.getFarmerId());
        stockInDTO.setTotalAmount(stockIn.getTotalAmount());
        stockInDTO.setServiceChargePercentage(stockIn.getServiceChargePercentage());
        stockInDTO.setServiceChargeAmount(stockIn.getServiceChargeAmount());
        stockInDTO.setPayableAmount(stockIn.getPayableAmount());
        stockInDTO.setPaymentStatus(stockIn.getPaymentStatus());
        stockInDTO.setBillId(stockIn.getBillId());
        stockInDTO.setCreatedAt(stockIn.getCreatedAt());
        stockInDTO.setUpdatedAt(stockIn.getUpdatedAt());
        stockInDTO.setTransportCharge(stockIn.getTransportCharge());
        stockInDTO.setLoadingCharge(stockIn.getLoadingCharge());
        stockInDTO.setLaborCharge(stockIn.getLaborCharge());
        stockInDTO.setTotalCharges(stockIn.getTotalCharges());
        return stockInDTO;
    }
    
    // Convert StockInDTO to StockIn entity
    private StockIn convertToEntity(StockInDTO stockInDTO) {
        StockIn stockIn = new StockIn();
        // For new stockIns, we'll use our custom ID generator
        // For existing stockIns, the ID will be preserved
        if (stockInDTO.getId() != null) {
            stockIn.setId(stockInDTO.getId());
        }
        stockIn.setMediatorId(stockInDTO.getMediatorId());
        stockIn.setFarmerId(stockInDTO.getFarmerId());
        stockIn.setTotalAmount(stockInDTO.getTotalAmount());
        stockIn.setServiceChargePercentage(stockInDTO.getServiceChargePercentage());
        stockIn.setServiceChargeAmount(stockInDTO.getServiceChargeAmount());
        stockIn.setPayableAmount(stockInDTO.getPayableAmount());
        stockIn.setPaymentStatus(stockInDTO.getPaymentStatus());
        stockIn.setBillId(stockInDTO.getBillId());
        stockIn.setCreatedAt(stockInDTO.getCreatedAt());
        stockIn.setUpdatedAt(stockInDTO.getUpdatedAt());
        stockIn.setTransportCharge(stockInDTO.getTransportCharge());
        stockIn.setLoadingCharge(stockInDTO.getLoadingCharge());
        stockIn.setLaborCharge(stockInDTO.getLaborCharge());
        stockIn.setTotalCharges(stockInDTO.getTotalCharges());
        return stockIn;
    }
    
    // Get all stockIns
    public List<StockInDTO> getAllStockIns() {
        return stockInRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get stockIn by ID
    public StockInDTO getStockInById(String id) {
        Optional<StockIn> stockIn = stockInRepository.findById(id);
        return stockIn.map(this::convertToDTO).orElse(null);
    }
    
    // Create new stockIn
    public StockInDTO createStockIn(StockInDTO stockInDTO) {
        // Validate that the mediator exists
        if (stockInDTO.getMediatorId() != null && 
            !profileRepository.existsById(stockInDTO.getMediatorId())) {
            throw new MediatorNotFoundException("No mediator available for this id");
        }
        
        // Validate that the farmer exists
        if (stockInDTO.getFarmerId() != null && 
            !farmerRepository.existsById(stockInDTO.getFarmerId())) {
            throw new FarmerNotFoundException("No Farmer Available for this Id");
        }
        
        // Generate custom ID if not provided
        if (stockInDTO.getId() == null) {
            stockInDTO.setId(idGenerator.generateNextId());
        }
        
        // Set timestamps
        stockInDTO.setCreatedAt(LocalDateTime.now());
        stockInDTO.setUpdatedAt(LocalDateTime.now());
        
        StockIn stockIn = convertToEntity(stockInDTO);
        StockIn savedStockIn = stockInRepository.save(stockIn);
        return convertToDTO(savedStockIn);
    }
    
    // Update stockIn
    public StockInDTO updateStockIn(String id, StockInDTO stockInDTO) {
        Optional<StockIn> existingStockIn = stockInRepository.findById(id);
        if (existingStockIn.isPresent()) {
            // Validate that the mediator exists
            if (stockInDTO.getMediatorId() != null && 
                !profileRepository.existsById(stockInDTO.getMediatorId())) {
                throw new MediatorNotFoundException("No mediator available for this id");
            }
            
            // Validate that the farmer exists
            if (stockInDTO.getFarmerId() != null && 
                !farmerRepository.existsById(stockInDTO.getFarmerId())) {
                throw new FarmerNotFoundException("No Farmer Available for this Id");
            }
            
            // Preserve original creation time
            stockInDTO.setCreatedAt(existingStockIn.get().getCreatedAt());
            // Update last modified time
            stockInDTO.setUpdatedAt(LocalDateTime.now());
            
            StockIn stockIn = convertToEntity(stockInDTO);
            stockIn.setId(id); // Ensure ID remains the same
            StockIn updatedStockIn = stockInRepository.save(stockIn);
            return convertToDTO(updatedStockIn);
        }
        return null;
    }
    
    // Delete stockIn
    public boolean deleteStockIn(String id) {
        if (stockInRepository.existsById(id)) {
            stockInRepository.deleteById(id);
            return true;
        }
        return false;
    }
}