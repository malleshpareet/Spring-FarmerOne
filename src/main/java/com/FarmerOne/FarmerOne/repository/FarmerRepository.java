package com.FarmerOne.FarmerOne.repository;

import com.FarmerOne.FarmerOne.model.Farmer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FarmerRepository extends MongoRepository<Farmer, String> {
    Optional<Farmer> findByMobileNumber(String mobileNumber);
    Optional<Farmer> findByFarmerCode(String farmerCode);
    List<Farmer> findByMediatorId(String mediatorId); // Changed from UUID to String
    boolean existsById(String id);
}