package com.FarmerOne.FarmerOne.service;

import com.FarmerOne.FarmerOne.dto.ProfileDTO;
import com.FarmerOne.FarmerOne.model.Profile;
import com.FarmerOne.FarmerOne.repository.ProfileRepository;
import com.FarmerOne.FarmerOne.exception.DuplicateProfileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProfile() {
        // Given
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setFullName("John Doe");
        profileDTO.setMobileNumber("1234567890");
        profileDTO.setDefaultServiceCharge(new BigDecimal("10.00"));

        Profile profile = new Profile();
        profile.setId("1");
        profile.setFullName("John Doe");
        profile.setMobileNumber("1234567890");
        profile.setDefaultServiceCharge(new BigDecimal("10.00"));

        when(profileRepository.findByMobileNumber("1234567890")).thenReturn(Optional.empty());
        when(profileRepository.findByEmail(null)).thenReturn(Optional.empty());
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        // When
        ProfileDTO result = profileService.createProfile(profileDTO);

        // Then
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("John Doe", result.getFullName());
        assertEquals("1234567890", result.getMobileNumber());
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void testCreateProfileWithDuplicateMobileNumber() {
        // Given
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setFullName("John Doe");
        profileDTO.setMobileNumber("1234567890");

        when(profileRepository.findByMobileNumber("1234567890")).thenReturn(Optional.of(new Profile()));

        // When & Then
        assertThrows(DuplicateProfileException.class, () -> {
            profileService.createProfile(profileDTO);
        });

        verify(profileRepository, never()).save(any(Profile.class));
    }

    @Test
    void testGetProfileById() {
        // Given
        String profileId = "1";
        Profile profile = new Profile();
        profile.setId(profileId);
        profile.setFullName("John Doe");
        profile.setMobileNumber("1234567890");

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));

        // When
        ProfileDTO result = profileService.getProfileById(profileId);

        // Then
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("John Doe", result.getFullName());
        assertEquals("1234567890", result.getMobileNumber());
    }

    @Test
    void testUpdateProfile() {
        // Given
        String profileId = "1";
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(profileId);
        profileDTO.setFullName("Jane Doe");
        profileDTO.setMobileNumber("0987654321");

        Profile profile = new Profile();
        profile.setId(profileId);
        profile.setFullName("Jane Doe");
        profile.setMobileNumber("0987654321");

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(new Profile()));
        when(profileRepository.findByMobileNumber("0987654321")).thenReturn(Optional.empty());
        when(profileRepository.findByEmail(null)).thenReturn(Optional.empty());
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        // When
        ProfileDTO result = profileService.updateProfile(profileId, profileDTO);

        // Then
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Jane Doe", result.getFullName());
        assertEquals("0987654321", result.getMobileNumber());
        verify(profileRepository, times(1)).findById(profileId);
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void testDeleteProfile() {
        // Given
        String profileId = "1";
        when(profileRepository.existsById(profileId)).thenReturn(true);

        // When
        boolean result = profileService.deleteProfile(profileId);

        // Then
        assertTrue(result);
        verify(profileRepository, times(1)).deleteById(profileId);
    }
}