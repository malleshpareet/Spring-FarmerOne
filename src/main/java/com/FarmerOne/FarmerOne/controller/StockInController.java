package com.FarmerOne.FarmerOne.controller;

import com.FarmerOne.FarmerOne.dto.StockInDTO;
import com.FarmerOne.FarmerOne.service.StockInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stock-in")
@CrossOrigin(origins = "*")
public class StockInController {
    
    @Autowired
    private StockInService stockInService;
    
    // Get all stockIns
    @GetMapping
    public ResponseEntity<List<StockInDTO>> getAllStockIns() {
        List<StockInDTO> stockIns = stockInService.getAllStockIns();
        return new ResponseEntity<>(stockIns, HttpStatus.OK);
    }
    
    // Get stockIn by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getStockInById(@PathVariable String id) {
        StockInDTO stockIn = stockInService.getStockInById(id);
        if (stockIn != null) {
            return new ResponseEntity<>(stockIn, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "StockIn not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    // Create new stockIn
    @PostMapping
    public ResponseEntity<Map<String, Object>> createStockIn(@RequestBody StockInDTO stockInDTO) {
        try {
            StockInDTO createdStockIn = stockInService.createStockIn(stockInDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("id", createdStockIn.getId());
            response.put("message", "StockIn created successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to create stockIn: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Update stockIn
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStockIn(@PathVariable String id, @RequestBody StockInDTO stockInDTO) {
        try {
            StockInDTO updatedStockIn = stockInService.updateStockIn(id, stockInDTO);
            if (updatedStockIn != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("id", updatedStockIn.getId());
                response.put("message", "StockIn updated successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "StockIn not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to update stockIn: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Delete stockIn
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteStockIn(@PathVariable String id) {
        boolean deleted = stockInService.deleteStockIn(id);
        Map<String, Object> response = new HashMap<>();
        if (deleted) {
            response.put("status", "success");
            response.put("id", id);
            response.put("message", "StockIn deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("status", "error");
            response.put("message", "StockIn not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}