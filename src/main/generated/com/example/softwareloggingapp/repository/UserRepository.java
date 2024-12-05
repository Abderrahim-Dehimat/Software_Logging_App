package com.example.softwareloggingapp.repository;
import com.example.softwareloggingapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
/**
 * Interface for managing User entities in the MongoDB database.
 *
 * This interface extends the MongoRepository, which provides CRUD
 * operations for User entities.
 *
 * MongoRepository handles common database interactions, including saving,
 * finding, deleting, and updating entities, streamlining database management.
 */
// No additional methods are defined here; it inherits basic CRUD methods from MongoRepository.
public interface UserRepository extends MongoRepository<User, String> {}