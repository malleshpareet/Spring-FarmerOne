package com.FarmerOne.FarmerOne.service;

import com.FarmerOne.FarmerOne.dto.AuthResponse;
import com.FarmerOne.FarmerOne.dto.LoginRequest;
import com.FarmerOne.FarmerOne.dto.RegisterRequest;
import com.FarmerOne.FarmerOne.model.User;
import com.FarmerOne.FarmerOne.repository.UserRepository;
import com.FarmerOne.FarmerOne.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    
    public AuthResponse registerUser(RegisterRequest registerRequest) {
        // Check if user already exists
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return new AuthResponse("User with this email already exists", false);
        }
        
        // Create new user
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setMobileNumber(registerRequest.getMobileNumber());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        
        userRepository.save(user);
        
        // Generate JWT token for the registered user
        String token = jwtUtil.generateToken(user.getMobileNumber());
        
        return new AuthResponse("User registered successfully", true, token);
    }
    
    public AuthResponse loginUser(LoginRequest loginRequest) {
        // Find user by mobile number
        User user = userRepository.findByMobileNumber(loginRequest.getMobileNumber())
                .orElse(null);
        
        if (user == null) {
            return new AuthResponse("Invalid mobile number or password", false);
        }
        
        // Validate password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return new AuthResponse("Invalid mobile number or password", false);
        }
        
        // Generate JWT token for the logged in user
        String token = jwtUtil.generateToken(user.getMobileNumber());
        
        return new AuthResponse("Login successful", true, token);
    }
}