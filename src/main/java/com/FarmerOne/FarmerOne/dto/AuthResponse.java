package com.FarmerOne.FarmerOne.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private boolean success;
    private String token; // Added token field
    
    // Constructor for responses without token
    public AuthResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
        this.token = null;
    }
}