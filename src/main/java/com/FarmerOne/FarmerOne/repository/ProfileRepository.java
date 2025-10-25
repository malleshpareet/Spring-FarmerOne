package com.FarmerOne.FarmerOne.repository;

import com.FarmerOne.FarmerOne.model.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProfileRepository extends MongoRepository<Profile, String> { // Changed back to String
    Optional<Profile> findByUserId(String userId);
    Optional<Profile> findByEmail(String email);
    Optional<Profile> findByMobileNumber(String mobileNumber);
}