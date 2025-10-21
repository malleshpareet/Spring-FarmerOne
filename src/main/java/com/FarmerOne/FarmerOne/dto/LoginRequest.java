package com.FarmerOne.FarmerOne.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String mobileNumber;
    private String password;
}