package com.FarmerOne.FarmerOne.service;

import com.FarmerOne.FarmerOne.dto.FarmerDTO;
import com.FarmerOne.FarmerOne.exception.MediatorNotFoundException;
import com.FarmerOne.FarmerOne.model.Farmer;
import com.FarmerOne.FarmerOne.repository.FarmerRepository;
import com.FarmerOne.FarmerOne.repository.ProfileRepository;
import com.FarmerOne.FarmerOne.util.CustomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FarmerService {
    
    @Autowired
    private FarmerRepository farmerRepository;
    
    @Autowired
    private ProfileRepository profileRepository;
    
    @Autowired
    private CustomIdGenerator idGenerator;
    
    // Convert Farmer entity to FarmerDTO
    private FarmerDTO convertToDTO(Farmer farmer) {
        FarmerDTO farmerDTO = new FarmerDTO();
        farmerDTO.setId(farmer.getId());
        farmerDTO.setMediatorId(farmer.getMediatorId());
        farmerDTO.setFullName(farmer.getFullName());
        farmerDTO.setVillageName(farmer.getVillageName());
        farmerDTO.setCreatedAt(farmer.getCreatedAt());
        farmerDTO.setMobileNumber(farmer.getMobileNumber());
        farmerDTO.setFarmerCode(farmer.getFarmerCode());
        return farmerDTO;
    }
    
    // Convert FarmerDTO to Farmer entity
    private Farmer convertToEntity(FarmerDTO farmerDTO) {
        Farmer farmer = new Farmer();
        if (farmerDTO.getId() != null) {
            farmer.setId(farmerDTO.getId());
        }
        farmer.setMediatorId(farmerDTO.getMediatorId());
        farmer.setFullName(farmerDTO.getFullName());
        farmer.setVillageName(farmerDTO.getVillageName());
        farmer.setCreatedAt(farmerDTO.getCreatedAt());
        farmer.setMobileNumber(farmerDTO.getMobileNumber());
        farmer.setFarmerCode(farmerDTO.getFarmerCode());
        return farmer;
    }
    
    // Get all farmers
    public List<FarmerDTO> getAllFarmers() {
        return farmerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get farmer by ID
    public FarmerDTO getFarmerById(String id) {
        Optional<Farmer> farmer = farmerRepository.findById(id);
        return farmer.map(this::convertToDTO).orElse(null);
    }
    
    // Get farmers by mediator ID
    public List<FarmerDTO> getFarmersByMediatorId(String mediatorId) {
        List<Farmer> farmers = farmerRepository.findByMediatorId(mediatorId);
        return farmers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Create new farmer
    public FarmerDTO createFarmer(FarmerDTO farmerDTO) {
        // Validate that the mediator exists
        if (farmerDTO.getMediatorId() != null && 
            !profileRepository.existsById(farmerDTO.getMediatorId())) {
            throw new MediatorNotFoundException("No mediator available for this id");
        }
        
        // Generate custom ID if not provided
        if (farmerDTO.getId() == null) {
            farmerDTO.setId(idGenerator.generateNextId());
        }
        
        Farmer farmer = convertToEntity(farmerDTO);
        Farmer savedFarmer = farmerRepository.save(farmer);
        return convertToDTO(savedFarmer);
    }
    
    // Update farmer
    public FarmerDTO updateFarmer(String id, FarmerDTO farmerDTO) {
        Optional<Farmer> existingFarmer = farmerRepository.findById(id);
        if (existingFarmer.isPresent()) {
            // Validate that the mediator exists
            if (farmerDTO.getMediatorId() != null && 
                !profileRepository.existsById(farmerDTO.getMediatorId())) {
                throw new MediatorNotFoundException("No mediator available for this id");
            }
            
            Farmer farmer = convertToEntity(farmerDTO);
            farmer.setId(id); // Ensure ID remains the same
            Farmer updatedFarmer = farmerRepository.save(farmer);
            return convertToDTO(updatedFarmer);
        }
        return null;
    }
    
    // Delete farmer
    public boolean deleteFarmer(String id) {
        if (farmerRepository.existsById(id)) {
            farmerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}