package com.FarmerOne.FarmerOne.exception;

import java.util.Map;

public class DuplicateProfileException extends RuntimeException {
    private Map<String, Object> details;
    
    public DuplicateProfileException(String message, Map<String, Object> details) {
        super(message);
        this.details = details;
    }
    
    public Map<String, Object> getDetails() {
        return details;
    }
}