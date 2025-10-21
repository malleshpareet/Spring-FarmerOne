package com.FarmerOne.FarmerOne.service;

import com.FarmerOne.FarmerOne.dto.AuthResponse;
import com.FarmerOne.FarmerOne.dto.LoginRequest;
import com.FarmerOne.FarmerOne.dto.RegisterRequest;
import com.FarmerOne.FarmerOne.model.User;
import com.FarmerOne.FarmerOne.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_Success() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("John Doe");
        registerRequest.setEmail("john@example.com");
        registerRequest.setPassword("password123");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        // Act
        AuthResponse response = userService.registerUser(registerRequest);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("User registered successfully", response.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_UserAlreadyExists() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("john@example.com");

        User existingUser = new User();
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(existingUser));

        // Act
        AuthResponse response = userService.registerUser(registerRequest);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("User with this email already exists", response.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginUser_Success() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setMobileNumber("1234567890");
        loginRequest.setPassword("password123");

        User user = new User();
        user.setMobileNumber("1234567890");
        user.setPassword("encodedPassword");

        when(userRepository.findByMobileNumber("1234567890")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        // Act
        AuthResponse response = userService.loginUser(loginRequest);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Login successful", response.getMessage());
    }

    @Test
    void loginUser_InvalidEmail() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setMobileNumber("1234567890");
        loginRequest.setPassword("password123");

        when(userRepository.findByMobileNumber("1234567890")).thenReturn(Optional.empty());

        // Act
        AuthResponse response = userService.loginUser(loginRequest);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("Invalid mobile number or password", response.getMessage());
    }

    @Test
    void loginUser_InvalidPassword() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setMobileNumber("1234567890");
        loginRequest.setPassword("wrongPassword");

        User user = new User();
        user.setMobileNumber("1234567890");
        user.setPassword("encodedPassword");

        when(userRepository.findByMobileNumber("1234567890")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // Act
        AuthResponse response = userService.loginUser(loginRequest);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("Invalid mobile number or password", response.getMessage());
    }
}