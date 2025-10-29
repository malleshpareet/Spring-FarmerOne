package com.FarmerOne.FarmerOne.repository;

import com.FarmerOne.FarmerOne.model.StockIn;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StockInRepository extends MongoRepository<StockIn, String> {
    Optional<StockIn> findByBillId(String billId);
}