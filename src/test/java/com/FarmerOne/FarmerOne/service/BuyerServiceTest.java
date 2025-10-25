package com.FarmerOne.FarmerOne.service;

import com.FarmerOne.FarmerOne.dto.BuyerDTO;
import com.FarmerOne.FarmerOne.model.Buyer;
import com.FarmerOne.FarmerOne.repository.BuyerRepository;
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

class BuyerServiceTest {

    @Mock
    private BuyerRepository buyerRepository;

    @Mock
    private CustomIdGenerator idGenerator;

    @InjectMocks
    private BuyerService buyerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBuyer() {
        // Given
        BuyerDTO buyerDTO = new BuyerDTO();
        buyerDTO.setFullName("John Buyer");
        buyerDTO.setMobileNumber("1234567890");
        buyerDTO.setMediatorId("1"); // Using String for numeric IDs

        Buyer buyer = new Buyer();
        buyer.setId("1");
        buyer.setFullName("John Buyer");
        buyer.setMobileNumber("1234567890");
        buyer.setMediatorId("1"); // Using String for numeric IDs

        when(idGenerator.generateNextId()).thenReturn("1");
        when(buyerRepository.save(any(Buyer.class))).thenReturn(buyer);

        // When
        BuyerDTO result = buyerService.createBuyer(buyerDTO);

        // Then
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("John Buyer", result.getFullName());
        assertEquals("1", result.getMediatorId()); // Using String for numeric IDs
        verify(buyerRepository, times(1)).save(any(Buyer.class));
    }

    @Test
    void testGetBuyerById() {
        // Given
        String buyerId = "1";
        Buyer buyer = new Buyer();
        buyer.setId(buyerId);
        buyer.setFullName("John Buyer");
        buyer.setMobileNumber("1234567890");

        when(buyerRepository.findById(buyerId)).thenReturn(Optional.of(buyer));

        // When
        BuyerDTO result = buyerService.getBuyerById(buyerId);

        // Then
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("John Buyer", result.getFullName());
    }

    @Test
    void testUpdateBuyer() {
        // Given
        String buyerId = "1";
        BuyerDTO buyerDTO = new BuyerDTO();
        buyerDTO.setId(buyerId);
        buyerDTO.setFullName("Jane Buyer");
        buyerDTO.setMediatorId("1"); // Using String for numeric IDs

        Buyer buyer = new Buyer();
        buyer.setId(buyerId);
        buyer.setFullName("Jane Buyer");
        buyer.setMediatorId("1"); // Using String for numeric IDs

        when(buyerRepository.findById(buyerId)).thenReturn(Optional.of(new Buyer()));
        when(buyerRepository.save(any(Buyer.class))).thenReturn(buyer);

        // When
        BuyerDTO result = buyerService.updateBuyer(buyerId, buyerDTO);

        // Then
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Jane Buyer", result.getFullName());
        assertEquals("1", result.getMediatorId()); // Using String for numeric IDs
        verify(buyerRepository, times(1)).findById(buyerId);
        verify(buyerRepository, times(1)).save(any(Buyer.class));
    }

    @Test
    void testDeleteBuyer() {
        // Given
        String buyerId = "1";
        when(buyerRepository.existsById(buyerId)).thenReturn(true);

        // When
        boolean result = buyerService.deleteBuyer(buyerId);

        // Then
        assertTrue(result);
        verify(buyerRepository, times(1)).deleteById(buyerId);
    }
}