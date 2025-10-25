package com.FarmerOne.FarmerOne.repository;

import com.FarmerOne.FarmerOne.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    Optional<Category> findBySlug(String slug);
    Optional<Category> findByCategoryName(String categoryName);
    boolean existsById(String id);
}