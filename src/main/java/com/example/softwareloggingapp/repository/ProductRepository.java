package com.example.softwareloggingapp.repository;

import com.example.softwareloggingapp.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
