package com.FarmerOne.FarmerOne.service;

import com.FarmerOne.FarmerOne.dto.BuyerDTO;
import com.FarmerOne.FarmerOne.model.Buyer;
import com.FarmerOne.FarmerOne.repository.BuyerRepository;
import com.FarmerOne.FarmerOne.util.CustomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BuyerService {
    
    @Autowired
    private BuyerRepository buyerRepository;
    
    @Autowired
    private CustomIdGenerator idGenerator;
    
    // Convert Buyer entity to BuyerDTO
    private BuyerDTO convertToDTO(Buyer buyer) {
        BuyerDTO buyerDTO = new BuyerDTO();
        buyerDTO.setId(buyer.getId());
        buyerDTO.setMediatorId(buyer.getMediatorId());
        buyerDTO.setFullName(buyer.getFullName());
        buyerDTO.setCreatedAt(buyer.getCreatedAt());
        buyerDTO.setMobileNumber(buyer.getMobileNumber());
        buyerDTO.setBuyerCode(buyer.getBuyerCode());
        return buyerDTO;
    }
    
    // Convert BuyerDTO to Buyer entity
    private Buyer convertToEntity(BuyerDTO buyerDTO) {
        Buyer buyer = new Buyer();
        if (buyerDTO.getId() != null) {
            buyer.setId(buyerDTO.getId());
        }
        buyer.setMediatorId(buyerDTO.getMediatorId());
        buyer.setFullName(buyerDTO.getFullName());
        buyer.setCreatedAt(buyerDTO.getCreatedAt());
        buyer.setMobileNumber(buyerDTO.getMobileNumber());
        buyer.setBuyerCode(buyerDTO.getBuyerCode());
        return buyer;
    }
    
    // Get all buyers
    public List<BuyerDTO> getAllBuyers() {
        return buyerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get buyer by ID
    public BuyerDTO getBuyerById(String id) {
        Optional<Buyer> buyer = buyerRepository.findById(id);
        return buyer.map(this::convertToDTO).orElse(null);
    }
    
    // Get buyers by mediator ID
    public List<BuyerDTO> getBuyersByMediatorId(String mediatorId) {
        List<Buyer> buyers = buyerRepository.findByMediatorId(mediatorId);
        return buyers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Create new buyer
    public BuyerDTO createBuyer(BuyerDTO buyerDTO) {
        // Generate custom ID if not provided
        if (buyerDTO.getId() == null) {
            buyerDTO.setId(idGenerator.generateNextId());
        }
        
        Buyer buyer = convertToEntity(buyerDTO);
        Buyer savedBuyer = buyerRepository.save(buyer);
        return convertToDTO(savedBuyer);
    }
    
    // Update buyer
    public BuyerDTO updateBuyer(String id, BuyerDTO buyerDTO) {
        Optional<Buyer> existingBuyer = buyerRepository.findById(id);
        if (existingBuyer.isPresent()) {
            Buyer buyer = convertToEntity(buyerDTO);
            buyer.setId(id); // Ensure ID remains the same
            Buyer updatedBuyer = buyerRepository.save(buyer);
            return convertToDTO(updatedBuyer);
        }
        return null;
    }
    
    // Delete buyer
    public boolean deleteBuyer(String id) {
        if (buyerRepository.existsById(id)) {
            buyerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}