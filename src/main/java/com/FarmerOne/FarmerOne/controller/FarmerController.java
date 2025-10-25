package com.FarmerOne.FarmerOne.controller;

import com.FarmerOne.FarmerOne.dto.FarmerDTO;
import com.FarmerOne.FarmerOne.service.FarmerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/farmers")
@CrossOrigin(origins = "*")
public class FarmerController {
    
    @Autowired
    private FarmerService farmerService;
    
    // Get all farmers
    @GetMapping
    public ResponseEntity<List<FarmerDTO>> getAllFarmers() {
        List<FarmerDTO> farmers = farmerService.getAllFarmers();
        return new ResponseEntity<>(farmers, HttpStatus.OK);
    }
    
    // Get farmer by ID
    @GetMapping("/{id}")
    public ResponseEntity<FarmerDTO> getFarmerById(@PathVariable String id) {
        FarmerDTO farmer = farmerService.getFarmerById(id);
        if (farmer != null) {
            return new ResponseEntity<>(farmer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Get farmers by mediator ID
    @GetMapping("/mediator/{mediatorId}")
    public ResponseEntity<List<FarmerDTO>> getFarmersByMediatorId(@PathVariable String mediatorId) {
        List<FarmerDTO> farmers = farmerService.getFarmersByMediatorId(mediatorId);
        return new ResponseEntity<>(farmers, HttpStatus.OK);
    }
    
    // Create new farmer
    @PostMapping
    public ResponseEntity<Map<String, Object>> createFarmer(@RequestBody FarmerDTO farmerDTO) {
        FarmerDTO createdFarmer = farmerService.createFarmer(farmerDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("id", createdFarmer.getId());
        response.put("message", "Farmer created successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    // Update farmer
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateFarmer(@PathVariable String id, @RequestBody FarmerDTO farmerDTO) {
        FarmerDTO updatedFarmer = farmerService.updateFarmer(id, farmerDTO);
        if (updatedFarmer != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("id", updatedFarmer.getId());
            response.put("message", "Farmer updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Farmer not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    // Delete farmer
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteFarmer(@PathVariable String id) {
        boolean deleted = farmerService.deleteFarmer(id);
        Map<String, Object> response = new HashMap<>();
        if (deleted) {
            response.put("status", "success");
            response.put("id", id);
            response.put("message", "Farmer deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("status", "error");
            response.put("message", "Farmer not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}