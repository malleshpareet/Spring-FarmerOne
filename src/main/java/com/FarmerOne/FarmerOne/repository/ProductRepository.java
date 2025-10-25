package com.FarmerOne.FarmerOne.repository;

import com.FarmerOne.FarmerOne.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findBySlug(String slug);
    Optional<Product> findByProductName(String productName);
    List<Product> findByCategoryId(String categoryId);
}