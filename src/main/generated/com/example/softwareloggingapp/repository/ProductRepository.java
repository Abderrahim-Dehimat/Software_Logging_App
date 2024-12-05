package com.example.softwareloggingapp.repository;
import com.example.softwareloggingapp.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
/**
 * Interface for managing Product entities in the MongoDB database.
 *
 * This interface extends the MongoRepository, which provides CRUD
 * operations for Product entities.
 *
 * MongoRepository automatically implements methods for saving, finding,
 * deleting, and updating entities, reducing boilerplate code.
 */
// No additional methods are defined here; it inherits basic CRUD methods from MongoRepository.
public interface ProductRepository extends MongoRepository<Product, String> {}