package com.FarmerOne.FarmerOne.controller;

import com.FarmerOne.FarmerOne.dto.BuyerDTO;
import com.FarmerOne.FarmerOne.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/buyers")
@CrossOrigin(origins = "*")
public class BuyerController {
    
    @Autowired
    private BuyerService buyerService;
    
    // Get all buyers
    @GetMapping
    public ResponseEntity<List<BuyerDTO>> getAllBuyers() {
        List<BuyerDTO> buyers = buyerService.getAllBuyers();
        return new ResponseEntity<>(buyers, HttpStatus.OK);
    }
    
    // Get buyer by ID
    @GetMapping("/{id}")
    public ResponseEntity<BuyerDTO> getBuyerById(@PathVariable String id) {
        BuyerDTO buyer = buyerService.getBuyerById(id);
        if (buyer != null) {
            return new ResponseEntity<>(buyer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Get buyers by mediator ID
    @GetMapping("/mediator/{mediatorId}")
    public ResponseEntity<List<BuyerDTO>> getBuyersByMediatorId(@PathVariable String mediatorId) {
        List<BuyerDTO> buyers = buyerService.getBuyersByMediatorId(mediatorId);
        return new ResponseEntity<>(buyers, HttpStatus.OK);
    }
    
    // Create new buyer
    @PostMapping
    public ResponseEntity<Map<String, Object>> createBuyer(@RequestBody BuyerDTO buyerDTO) {
        BuyerDTO createdBuyer = buyerService.createBuyer(buyerDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("id", createdBuyer.getId());
        response.put("message", "Buyer created successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    // Update buyer
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateBuyer(@PathVariable String id, @RequestBody BuyerDTO buyerDTO) {
        BuyerDTO updatedBuyer = buyerService.updateBuyer(id, buyerDTO);
        if (updatedBuyer != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("id", updatedBuyer.getId());
            response.put("message", "Buyer updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Buyer not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    // Delete buyer
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteBuyer(@PathVariable String id) {
        boolean deleted = buyerService.deleteBuyer(id);
        Map<String, Object> response = new HashMap<>();
        if (deleted) {
            response.put("status", "success");
            response.put("id", id);
            response.put("message", "Buyer deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("status", "error");
            response.put("message", "Buyer not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}