package com.example.softwareloggingapp.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a User entity in the application.
 * This class is mapped to a MongoDB collection using Spring Data annotations.
 */
@Data // Lombok annotation to generate boilerplate code like getters, setters, and toString methods.
@Document // Indicates that this class is a MongoDB document.
public class User {

    @Id // Marks this field as the primary identifier for the MongoDB document.
    private String id; // Unique identifier for the user.

    private String name; // Name of the user.

    private int age; // Age of the user.

    private String email; // Email address of the user, used for identification and authentication.

    private String password; // Password of the user, used for authentication.
}
