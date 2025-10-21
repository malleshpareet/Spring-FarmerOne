package com.FarmerOne.FarmerOne.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String mobileNumber;
    private String password;
}