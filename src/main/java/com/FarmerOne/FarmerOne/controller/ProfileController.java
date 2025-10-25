package com.FarmerOne.FarmerOne.controller;

import com.FarmerOne.FarmerOne.dto.ProfileDTO;
import com.FarmerOne.FarmerOne.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = "*")
public class ProfileController {
    
    @Autowired
    private ProfileService profileService;
    
    // Get all profiles
    @GetMapping
    public ResponseEntity<List<ProfileDTO>> getAllProfiles() {
        List<ProfileDTO> profiles = profileService.getAllProfiles();
        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }
    
    // Get profile by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProfileDTO> getProfileById(@PathVariable String id) {
        ProfileDTO profile = profileService.getProfileById(id);
        if (profile != null) {
            return new ResponseEntity<>(profile, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Create new profile
    @PostMapping
    public ResponseEntity<?> createProfile(@RequestBody ProfileDTO profileDTO) {
        try {
            ProfileDTO createdProfile = profileService.createProfile(profileDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("id", createdProfile.getId());
            response.put("message", "User profile created successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Let the GlobalExceptionHandler handle DuplicateProfileException
            throw e;
        }
    }
    
    // Update profile
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable String id, @RequestBody ProfileDTO profileDTO) {
        try {
            ProfileDTO updatedProfile = profileService.updateProfile(id, profileDTO);
            if (updatedProfile != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("id", updatedProfile.getId());
                response.put("message", "User profile updated successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Profile not found", HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            // Let the GlobalExceptionHandler handle DuplicateProfileException
            throw e;
        }
    }
    
    // Delete profile
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProfile(@PathVariable String id) {
        boolean deleted = profileService.deleteProfile(id);
        Map<String, Object> response = new HashMap<>();
        if (deleted) {
            response.put("status", "success");
            response.put("id", id);
            response.put("message", "User profile deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("status", "error");
            response.put("message", "Profile not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}