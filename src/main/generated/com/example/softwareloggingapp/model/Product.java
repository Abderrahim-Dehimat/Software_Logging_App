package com.example.softwareloggingapp.model;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document
public class Product {
    @Id
    private String id;

    private String name;

    private double price;

    private LocalDate expirationDate;
}