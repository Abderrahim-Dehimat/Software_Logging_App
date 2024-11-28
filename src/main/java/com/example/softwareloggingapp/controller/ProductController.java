package com.example.softwareloggingapp.controller;

import com.example.softwareloggingapp.model.Product;
import com.example.softwareloggingapp.service.ProductService;
import com.example.softwareloggingapp.spoon.ProfileGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing products.
 * Provides CRUD operations and additional functionality for fetching the most expensive products.
 * Includes integration with a ProfileGenerator for user profile updates.
 */
@RestController
@RequestMapping("/api/products") // Base URL for all endpoints in this controller
@RequiredArgsConstructor // Generates a constructor for final fields
@Slf4j // Enables SLF4J logging
public class ProductController {

    // Service layer dependency for handling product-related business logic
    private final ProductService productService;

    // Profile generator for updating user profiles after each operation
    private final ProfileGenerator profileGenerator;

    /**
     * Endpoint for adding a new product.
     * Logs the operation, updates user profiles, and returns a success response.
     *
     * @param userEmail Email of the user performing the operation.
     * @param product   Product details to be added.
     * @return ResponseEntity with creation status and message.
     */
    @PostMapping("/create")
    public ResponseEntity<?> addProduct(@RequestHeader("user-email") String userEmail, @RequestBody Product product) {
        log.info("WRITE operation performed by user: " + userEmail);
        log.info("User {} performed WRITE operation: adding product {}", userEmail, product);
        productService.addProduct(product);
        profileGenerator.generateAggregatedProfiles(); // Rebuild the JSON file
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");
    }

    /**
     * Endpoint for fetching a product by its ID.
     * Logs the operation, updates user profiles, and returns the product details.
     *
     * @param userEmail Email of the user performing the operation.
     * @param id        ID of the product to fetch.
     * @return Product object corresponding to the provided ID.
     */
    @GetMapping("/readProductById/{id}")
    public Product getProductById(@RequestHeader("user-email") String userEmail, @PathVariable String id) {
        log.info("User {} performed READ operation: fetching product with ID {}", userEmail, id);
        Product product = productService.getProductById(id);
        log.info("Product fetched successfully: {}", product);
        profileGenerator.generateAggregatedProfiles();
        return product;
    }

    /**
     * Endpoint for deleting a product by its ID.
     * Logs the operation, updates user profiles, and confirms successful deletion.
     *
     * @param userEmail Email of the user performing the operation.
     * @param id        ID of the product to delete.
     */
    @DeleteMapping("/deleteProduct/{id}")
    public void deleteProduct(@RequestHeader("user-email") String userEmail, @PathVariable String id) {
        log.info("WRITE operation performed by user: " + userEmail);
        log.warn("User {} performed DELETE operation: deleting product with ID {}", userEmail, id);
        productService.deleteProduct(id);
        profileGenerator.generateAggregatedProfiles();
        log.info("Product with ID {} deleted successfully by user {}", id, userEmail);
    }

    /**
     * Endpoint for updating an existing product.
     * Logs the operation, updates user profiles, and returns the updated product.
     *
     * @param userEmail Email of the user performing the operation.
     * @param product   Updated product details.
     * @return Updated Product object.
     */
    @PutMapping("/updateProduct")
    public Product updateProduct(@RequestHeader("user-email") String userEmail, @RequestBody Product product) {
        log.info("WRITE operation performed by user: " + userEmail);
        log.info("User {} performed WRITE operation: updating product {}", userEmail, product);
        Product updatedProduct = productService.updateProduct(product);
        log.info("Product updated successfully: {}", updatedProduct);
        profileGenerator.generateAggregatedProfiles();
        return updatedProduct;
    }

    /**
     * Endpoint for fetching all products.
     * Logs the operation, updates user profiles, and returns the list of all products.
     *
     * @param userEmail Email of the user performing the operation.
     * @return List of all products.
     */
    @GetMapping("/readAllProducts")
    public List<Product> getAllProducts(@RequestHeader("user-email") String userEmail) {
        log.info("User {} performed READ operation: fetching all products", userEmail);
        List<Product> products = productService.getAllProducts();
        log.info("Total products fetched by user {}: {}", userEmail, products.size());
        profileGenerator.generateAggregatedProfiles();
        return products;
    }

    /**
     * Endpoint for fetching the top 3 most expensive products.
     * Logs the operation, updates user profiles, and returns the list of products.
     *
     * @param userEmail Email of the user performing the operation.
     * @return List of the top 3 most expensive products.
     */
    @GetMapping("/most-expensive-products")
    public List<Product> getTopExpensiveProducts(@RequestHeader("user-email") String userEmail) {
        List<Product> expensiveProducts = productService.getTopExpensiveProducts();
        log.info("User {} searched for the most expensive products", userEmail);
        profileGenerator.generateAggregatedProfiles();
        return expensiveProducts;
    }
}
