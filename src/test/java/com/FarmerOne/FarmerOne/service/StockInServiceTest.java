package com.FarmerOne.FarmerOne.service;

import com.FarmerOne.FarmerOne.dto.StockInDTO;
import com.FarmerOne.FarmerOne.model.StockIn;
import com.FarmerOne.FarmerOne.repository.StockInRepository;
import com.FarmerOne.FarmerOne.util.CustomIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StockInServiceTest {

    @Mock
    private StockInRepository stockInRepository;

    @Mock
    private FarmerRepository farmerRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private CustomIdGenerator idGenerator;

    @InjectMocks
    private StockInService stockInService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateStockIn() {
        // Given
        StockInDTO stockInDTO = new StockInDTO();
        stockInDTO.setMediatorId("1");
        stockInDTO.setFarmerId("2");
        stockInDTO.setEntryDate(LocalDate.now());
        stockInDTO.setTotalAmount(new BigDecimal("100.00"));
        stockInDTO.setServiceChargePercentage(new BigDecimal("5.00"));
        stockInDTO.setBillId("BILL-001");

        StockIn stockIn = new StockIn();
        stockIn.setId("1");
        stockIn.setMediatorId("1");
        stockIn.setFarmerId("2");
        stockIn.setEntryDate(LocalDate.now());
        stockIn.setTotalAmount(new BigDecimal("100.00"));
        stockIn.setServiceChargePercentage(new BigDecimal("5.00"));
        stockIn.setBillId("BILL-001");
        stockIn.setCreatedAt(LocalDateTime.now());
        stockIn.setUpdatedAt(LocalDateTime.now());

        when(idGenerator.generateNextId()).thenReturn("1");
        when(stockInRepository.save(any(StockIn.class))).thenReturn(stockIn);

        // When
        StockInDTO result = stockInService.createStockIn(stockInDTO);

        // Then
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("1", result.getMediatorId());
        assertEquals("2", result.getFarmerId());
        assertEquals("BILL-001", result.getBillId());
        
        verify(idGenerator, times(1)).generateNextId();
        verify(stockInRepository, times(1)).save(any(StockIn.class));
    }

    @Test
    void testGetStockInById() {
        // Given
        String stockInId = "1";
        StockIn stockIn = new StockIn();
        stockIn.setId(stockInId);
        stockIn.setMediatorId("1");
        stockIn.setFarmerId("2");
        stockIn.setEntryDate(LocalDate.now());
        stockIn.setTotalAmount(new BigDecimal("100.00"));
        stockIn.setServiceChargePercentage(new BigDecimal("5.00"));
        stockIn.setBillId("BILL-001");
        stockIn.setCreatedAt(LocalDateTime.now());
        stockIn.setUpdatedAt(LocalDateTime.now());

        when(stockInRepository.findById(stockInId)).thenReturn(Optional.of(stockIn));

        // When
        StockInDTO result = stockInService.getStockInById(stockInId);

        // Then
        assertNotNull(result);
        assertEquals(stockInId, result.getId());
        assertEquals("1", result.getMediatorId());
        assertEquals("2", result.getFarmerId());
        assertEquals("BILL-001", result.getBillId());
    }

    @Test
    void testGetStockInByIdNotFound() {
        // Given
        String stockInId = "1";
        when(stockInRepository.findById(stockInId)).thenReturn(Optional.empty());

        // When
        StockInDTO result = stockInService.getStockInById(stockInId);

        // Then
        assertNull(result);
    }

    @Test
    void testGetAllStockIns() {
        // Given
        StockIn stockIn1 = new StockIn();
        stockIn1.setId("1");
        stockIn1.setBillId("BILL-001");

        StockIn stockIn2 = new StockIn();
        stockIn2.setId("2");
        stockIn2.setBillId("BILL-002");

        List<StockIn> stockIns = new ArrayList<>();
        stockIns.add(stockIn1);
        stockIns.add(stockIn2);

        when(stockInRepository.findAll()).thenReturn(stockIns);

        // When
        List<StockInDTO> result = stockInService.getAllStockIns();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("2", result.get(1).getId());
    }

    @Test
    void testUpdateStockIn() {
        // Given
        String stockInId = "1";
        StockInDTO stockInDTO = new StockInDTO();
        stockInDTO.setMediatorId("1");
        stockInDTO.setFarmerId("2");
        stockInDTO.setEntryDate(LocalDate.now());
        stockInDTO.setTotalAmount(new BigDecimal("150.00"));
        stockInDTO.setServiceChargePercentage(new BigDecimal("5.00"));
        stockInDTO.setBillId("BILL-001");

        StockIn existingStockIn = new StockIn();
        existingStockIn.setId(stockInId);
        existingStockIn.setMediatorId("1");
        existingStockIn.setFarmerId("2");
        existingStockIn.setEntryDate(LocalDate.now().minusDays(1)); // Created yesterday
        existingStockIn.setTotalAmount(new BigDecimal("100.00"));
        existingStockIn.setServiceChargePercentage(new BigDecimal("5.00"));
        existingStockIn.setBillId("BILL-001");
        existingStockIn.setCreatedAt(LocalDateTime.now().minusDays(1)); // Created yesterday

        StockIn updatedStockIn = new StockIn();
        updatedStockIn.setId(stockInId);
        updatedStockIn.setMediatorId("1");
        updatedStockIn.setFarmerId("2");
        updatedStockIn.setEntryDate(LocalDate.now());
        updatedStockIn.setTotalAmount(new BigDecimal("150.00"));
        updatedStockIn.setServiceChargePercentage(new BigDecimal("5.00"));
        updatedStockIn.setBillId("BILL-001");
        updatedStockIn.setCreatedAt(existingStockIn.getCreatedAt()); // Should preserve original creation time
        updatedStockIn.setUpdatedAt(LocalDateTime.now());

        when(stockInRepository.findById(stockInId)).thenReturn(Optional.of(existingStockIn));
        when(stockInRepository.save(any(StockIn.class))).thenReturn(updatedStockIn);

        // When
        StockInDTO result = stockInService.updateStockIn(stockInId, stockInDTO);

        // Then
        assertNotNull(result);
        assertEquals(stockInId, result.getId());
        assertEquals("1", result.getMediatorId());
        assertEquals("2", result.getFarmerId());
        assertEquals("BILL-001", result.getBillId());
        assertEquals(new BigDecimal("150.00"), result.getTotalAmount());
        // Check that creation time was preserved
        assertEquals(existingStockIn.getCreatedAt(), result.getCreatedAt());
        // Check that update time was set
        assertNotNull(result.getUpdatedAt());
        
        verify(stockInRepository, times(1)).findById(stockInId);
        verify(stockInRepository, times(1)).save(any(StockIn.class));
    }

    @Test
    void testUpdateStockInNotFound() {
        // Given
        String stockInId = "1";
        StockInDTO stockInDTO = new StockInDTO();
        when(stockInRepository.findById(stockInId)).thenReturn(Optional.empty());

        // When
        StockInDTO result = stockInService.updateStockIn(stockInId, stockInDTO);

        // Then
        assertNull(result);
    }

    @Test
    void testCreateStockInWithInvalidMediator() {
        // Given
        StockInDTO stockInDTO = new StockInDTO();
        stockInDTO.setMediatorId("999"); // Non-existent mediator
        stockInDTO.setFarmerId("2");
        
        // Mock the repository to return false for existsById
        when(profileRepository.existsById("999")).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            stockInService.createStockIn(stockInDTO);
        });
        
        verify(profileRepository, times(1)).existsById("999");
        verify(farmerRepository, times(0)).existsById(any());
    }

    @Test
    void testCreateStockInWithInvalidFarmer() {
        // Given
        StockInDTO stockInDTO = new StockInDTO();
        stockInDTO.setMediatorId("1");
        stockInDTO.setFarmerId("999"); // Non-existent farmer
        
        // Mock the repositories
        when(profileRepository.existsById("1")).thenReturn(true);
        when(farmerRepository.existsById("999")).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            stockInService.createStockIn(stockInDTO);
        });
        
        verify(profileRepository, times(1)).existsById("1");
        verify(farmerRepository, times(1)).existsById("999");
    }

    @Test
    void testUpdateStockInWithInvalidMediator() {
        // Given
        String stockInId = "1";
        StockInDTO stockInDTO = new StockInDTO();
        stockInDTO.setMediatorId("999"); // Non-existent mediator
        stockInDTO.setFarmerId("2");
        
        // Mock existing stockIn
        StockIn existingStockIn = new StockIn();
        existingStockIn.setId(stockInId);
        when(stockInRepository.findById(stockInId)).thenReturn(Optional.of(existingStockIn));
        
        // Mock the repository to return false for existsById
        when(profileRepository.existsById("999")).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            stockInService.updateStockIn(stockInId, stockInDTO);
        });
        
        verify(profileRepository, times(1)).existsById("999");
        verify(farmerRepository, times(0)).existsById(any());
    }

    @Test
    void testUpdateStockInWithInvalidFarmer() {
        // Given
        String stockInId = "1";
        StockInDTO stockInDTO = new StockInDTO();
        stockInDTO.setMediatorId("1");
        stockInDTO.setFarmerId("999"); // Non-existent farmer
        
        // Mock existing stockIn
        StockIn existingStockIn = new StockIn();
        existingStockIn.setId(stockInId);
        when(stockInRepository.findById(stockInId)).thenReturn(Optional.of(existingStockIn));
        
        // Mock the repositories
        when(profileRepository.existsById("1")).thenReturn(true);
        when(farmerRepository.existsById("999")).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            stockInService.updateStockIn(stockInId, stockInDTO);
        });
        
        verify(profileRepository, times(1)).existsById("1");
        verify(farmerRepository, times(1)).existsById("999");
    }

    @Test
    void testDeleteStockIn() {
        // Given
        String stockInId = "1";
        when(stockInRepository.existsById(stockInId)).thenReturn(true);

        // When
        boolean result = stockInService.deleteStockIn(stockInId);

        // Then
        assertTrue(result);
        verify(stockInRepository, times(1)).deleteById(stockInId);
    }

    @Test
    void testDeleteStockInNotFound() {
        // Given
        String stockInId = "1";
        when(stockInRepository.existsById(stockInId)).thenReturn(false);

        // When
        boolean result = stockInService.deleteStockIn(stockInId);

        // Then
        assertFalse(result);
        verify(stockInRepository, times(0)).deleteById(stockInId);
    }
}