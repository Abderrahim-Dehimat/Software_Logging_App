package com.example.softwareloggingapp.repository;

import com.example.softwareloggingapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
