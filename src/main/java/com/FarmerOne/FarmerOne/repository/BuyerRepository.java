 package com.FarmerOne.FarmerOne.repository;

import com.FarmerOne.FarmerOne.model.Buyer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BuyerRepository extends MongoRepository<Buyer, String> {
    Optional<Buyer> findByMobileNumber(String mobileNumber);
    Optional<Buyer> findByBuyerCode(String buyerCode);
    List<Buyer> findByMediatorId(String mediatorId); // Using String for numeric IDs
}