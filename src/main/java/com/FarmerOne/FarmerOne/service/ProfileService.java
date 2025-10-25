package com.FarmerOne.FarmerOne.service;

import com.FarmerOne.FarmerOne.dto.ProfileDTO;
import com.FarmerOne.FarmerOne.model.Profile;
import com.FarmerOne.FarmerOne.repository.ProfileRepository;
import com.FarmerOne.FarmerOne.util.CustomIdGenerator;
import com.FarmerOne.FarmerOne.exception.DuplicateProfileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    
    @Autowired
    private ProfileRepository profileRepository;
    
    @Autowired
    private CustomIdGenerator idGenerator;
    
    // Convert Profile entity to ProfileDTO
    private ProfileDTO convertToDTO(Profile profile) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(profile.getId());
        profileDTO.setUserId(profile.getUserId());
        profileDTO.setFullName(profile.getFullName());
        profileDTO.setMobileNumber(profile.getMobileNumber());
        profileDTO.setDefaultServiceCharge(profile.getDefaultServiceCharge());
        profileDTO.setLanguagePref(profile.getLanguagePref());
        profileDTO.setCreatedAt(profile.getCreatedAt());
        profileDTO.setUpdatedAt(profile.getUpdatedAt());
        profileDTO.setShopName(profile.getShopName());
        profileDTO.setMandiName(profile.getMandiName());
        profileDTO.setBusinessName(profile.getBusinessName());
        profileDTO.setGstNumber(profile.getGstNumber());
        profileDTO.setAddressLine1(profile.getAddressLine1());
        profileDTO.setAddressLine2(profile.getAddressLine2());
        profileDTO.setCity(profile.getCity());
        profileDTO.setState(profile.getState());
        profileDTO.setPincode(profile.getPincode());
        profileDTO.setEmail(profile.getEmail());
        return profileDTO;
    }
    
    // Convert ProfileDTO to Profile entity
    private Profile convertToEntity(ProfileDTO profileDTO) {
        Profile profile = new Profile();
        // For new profiles, we'll use our custom ID generator
        // For existing profiles, the ID will be preserved
        if (profileDTO.getId() != null) {
            profile.setId(profileDTO.getId());
        }
        profile.setUserId(profileDTO.getUserId());
        profile.setFullName(profileDTO.getFullName());
        profile.setMobileNumber(profileDTO.getMobileNumber());
        profile.setDefaultServiceCharge(profileDTO.getDefaultServiceCharge());
        profile.setLanguagePref(profileDTO.getLanguagePref());
        profile.setCreatedAt(profileDTO.getCreatedAt());
        profile.setUpdatedAt(profileDTO.getUpdatedAt());
        profile.setShopName(profileDTO.getShopName());
        profile.setMandiName(profileDTO.getMandiName());
        profile.setBusinessName(profileDTO.getBusinessName());
        profile.setGstNumber(profileDTO.getGstNumber());
        profile.setAddressLine1(profileDTO.getAddressLine1());
        profile.setAddressLine2(profileDTO.getAddressLine2());
        profile.setCity(profileDTO.getCity());
        profile.setState(profileDTO.getState());
        profile.setPincode(profileDTO.getPincode());
        profile.setEmail(profileDTO.getEmail());
        return profile;
    }
    
    // Get all profiles
    public List<ProfileDTO> getAllProfiles() {
        return profileRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get profile by ID
    public ProfileDTO getProfileById(String id) {
        Optional<Profile> profile = profileRepository.findById(id);
        return profile.map(this::convertToDTO).orElse(null);
    }
    
    // Get profile by user ID
    public ProfileDTO getProfileByUserId(String userId) {
        Optional<Profile> profile = profileRepository.findByUserId(userId);
        return profile.map(this::convertToDTO).orElse(null);
    }
    
    // Create new profile
    public ProfileDTO createProfile(ProfileDTO profileDTO) throws DuplicateProfileException {
        try {
            // Check if profiles with the same mobile number already exist
            List<Profile> existingProfiles = profileRepository.findByMobileNumber(profileDTO.getMobileNumber())
                    .map(List::of)
                    .orElse(List.of());
            
            if (!existingProfiles.isEmpty()) {
                throw new DuplicateProfileException("Multiple users found with the given mobile number or email", null);
            }
            
            // Check if profiles with the same email already exist (if email is provided)
            if (profileDTO.getEmail() != null && !profileDTO.getEmail().isEmpty()) {
                List<Profile> existingEmailProfiles = profileRepository.findByEmail(profileDTO.getEmail())
                        .map(List::of)
                        .orElse(List.of());
                
                if (!existingEmailProfiles.isEmpty()) {
                    throw new DuplicateProfileException("Multiple users found with the given mobile number or email", null);
                }
            }
        } catch (IncorrectResultSizeDataAccessException e) {
            // This handles the "non unique result" error
            throw new DuplicateProfileException("Multiple users found with the given mobile number or email", null);
        }
        
        // Generate custom ID if not provided
        if (profileDTO.getId() == null) {
            profileDTO.setId(idGenerator.generateNextId());
        }
        
        Profile profile = convertToEntity(profileDTO);
        Profile savedProfile = profileRepository.save(profile);
        return convertToDTO(savedProfile);
    }
    
    // Update profile
    public ProfileDTO updateProfile(String id, ProfileDTO profileDTO) throws DuplicateProfileException {
        Optional<Profile> existingProfile = profileRepository.findById(id);
        if (existingProfile.isPresent()) {
            try {
                // Check if another profile has the same mobile number
                Optional<Profile> mobileProfile = profileRepository.findByMobileNumber(profileDTO.getMobileNumber());
                if (mobileProfile.isPresent() && !mobileProfile.get().getId().equals(id)) {
                    throw new DuplicateProfileException("Multiple users found with the given mobile number or email", null);
                }
                
                // Check if another profile has the same email (if email is provided)
                if (profileDTO.getEmail() != null && !profileDTO.getEmail().isEmpty()) {
                    Optional<Profile> emailProfile = profileRepository.findByEmail(profileDTO.getEmail());
                    if (emailProfile.isPresent() && !emailProfile.get().getId().equals(id)) {
                        throw new DuplicateProfileException("Multiple users found with the given mobile number or email", null);
                    }
                }
            } catch (IncorrectResultSizeDataAccessException e) {
                // This handles the "non unique result" error
                throw new DuplicateProfileException("Multiple users found with the given mobile number or email", null);
            }
            
            Profile profile = convertToEntity(profileDTO);
            profile.setId(id); // Ensure ID remains the same
            Profile updatedProfile = profileRepository.save(profile);
            return convertToDTO(updatedProfile);
        }
        return null;
    }
    
    // Delete profile
    public boolean deleteProfile(String id) {
        if (profileRepository.existsById(id)) {
            profileRepository.deleteById(id);
            return true;
        }
        return false;
    }
}