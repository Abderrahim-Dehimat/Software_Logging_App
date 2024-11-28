package com.example.softwareloggingapp.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * The Product class represents a model of a product entity in the application.
 * It is annotated with @Document, indicating that this class maps to a MongoDB collection.
 */
@Data
@Document
public class Product {
    /**
     * The unique identifier for a product.
     * Annotated with @Id to mark it as the primary key in the MongoDB collection.
     */
    @Id
    private String id;

    /**
     * The name of the product.
     */
    private String name;

    /**
     * The price of the product.
     */
    private double price;

    /**
     * The expiration date of the product, represented as a string.
     */
    private String expirationDate;
}
