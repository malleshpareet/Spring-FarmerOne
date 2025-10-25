package com.FarmerOne.FarmerOne.service;

import com.FarmerOne.FarmerOne.dto.FarmerDTO;
import com.FarmerOne.FarmerOne.model.Farmer;
import com.FarmerOne.FarmerOne.repository.FarmerRepository;
import com.FarmerOne.FarmerOne.util.CustomIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.ZonedDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FarmerServiceTest {

    @Mock
    private FarmerRepository farmerRepository;

    @Mock
    private CustomIdGenerator idGenerator;

    @InjectMocks
    private FarmerService farmerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFarmer() {
        // Given
        FarmerDTO farmerDTO = new FarmerDTO();
        farmerDTO.setFullName("John Farmer");
        farmerDTO.setVillageName("Green Village");
        farmerDTO.setMobileNumber("1234567890");
        farmerDTO.setMediatorId("1"); // Changed from UUID to String

        Farmer farmer = new Farmer();
        farmer.setId("1");
        farmer.setFullName("John Farmer");
        farmer.setVillageName("Green Village");
        farmer.setMobileNumber("1234567890");
        farmer.setMediatorId("1"); // Changed from UUID to String

        when(idGenerator.generateNextId()).thenReturn("1");
        when(farmerRepository.save(any(Farmer.class))).thenReturn(farmer);

        // When
        FarmerDTO result = farmerService.createFarmer(farmerDTO);

        // Then
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("John Farmer", result.getFullName());
        assertEquals("Green Village", result.getVillageName());
        assertEquals("1", result.getMediatorId()); // Changed from UUID to String
        verify(farmerRepository, times(1)).save(any(Farmer.class));
    }

    @Test
    void testGetFarmerById() {
        // Given
        String farmerId = "1";
        Farmer farmer = new Farmer();
        farmer.setId(farmerId);
        farmer.setFullName("John Farmer");
        farmer.setVillageName("Green Village");
        farmer.setMobileNumber("1234567890");

        when(farmerRepository.findById(farmerId)).thenReturn(Optional.of(farmer));

        // When
        FarmerDTO result = farmerService.getFarmerById(farmerId);

        // Then
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("John Farmer", result.getFullName());
        assertEquals("Green Village", result.getVillageName());
    }

    @Test
    void testUpdateFarmer() {
        // Given
        String farmerId = "1";
        FarmerDTO farmerDTO = new FarmerDTO();
        farmerDTO.setId(farmerId);
        farmerDTO.setFullName("Jane Farmer");
        farmerDTO.setVillageName("Blue Village");
        farmerDTO.setMediatorId("1"); // Changed from UUID to String

        Farmer farmer = new Farmer();
        farmer.setId(farmerId);
        farmer.setFullName("Jane Farmer");
        farmer.setVillageName("Blue Village");
        farmer.setMediatorId("1"); // Changed from UUID to String

        when(farmerRepository.findById(farmerId)).thenReturn(Optional.of(new Farmer()));
        when(farmerRepository.save(any(Farmer.class))).thenReturn(farmer);

        // When
        FarmerDTO result = farmerService.updateFarmer(farmerId, farmerDTO);

        // Then
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Jane Farmer", result.getFullName());
        assertEquals("Blue Village", result.getVillageName());
        assertEquals("1", result.getMediatorId()); // Changed from UUID to String
        verify(farmerRepository, times(1)).findById(farmerId);
        verify(farmerRepository, times(1)).save(any(Farmer.class));
    }

    @Test
    void testDeleteFarmer() {
        // Given
        String farmerId = "1";
        when(farmerRepository.existsById(farmerId)).thenReturn(true);

        // When
        boolean result = farmerService.deleteFarmer(farmerId);

        // Then
        assertTrue(result);
        verify(farmerRepository, times(1)).deleteById(farmerId);
    }
}