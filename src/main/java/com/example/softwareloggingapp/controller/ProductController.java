package com.example.softwareloggingapp.controller;
import com.example.softwareloggingapp.model.Product;
import com.example.softwareloggingapp.service.ProductService;
import java.util.List;

import com.example.softwareloggingapp.spoon.ProfileGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
// Annotation for SLF4J logger
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final ProfileGenerator profileGenerator;

    @PostMapping("/create")
    public ResponseEntity<?> addProduct(@RequestHeader("user-email")
                              String userEmail, @RequestBody
                              Product product) {
        log.info("WRITE operation performed by user: " + userEmail);
        log.info("User {} performed WRITE operation: adding product {}", userEmail, product);
        productService.addProduct(product);
        profileGenerator.generateAggregatedProfiles(); // Rebuild the JSON file
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");
    }

    @GetMapping("/readProductById/{id}")
    public Product getProductById(@RequestHeader("user-email")
                                  String userEmail, @PathVariable
                                  String id) {
        log.info("User {} performed READ operation: fetching product with ID {}", userEmail, id);
        Product product = productService.getProductById(id);
        log.info("Product fetched successfully: {}", product);
        profileGenerator.generateAggregatedProfiles();
        return product;
    }

    @DeleteMapping("/deleteProduct/{id}")
    public void deleteProduct(@RequestHeader("user-email")
                              String userEmail, @PathVariable
                              String id) {
        log.info("WRITE operation performed by user: " + userEmail);
        log.warn("User {} performed DELETE operation: deleting product with ID {}", userEmail, id);
        productService.deleteProduct(id);
        profileGenerator.generateAggregatedProfiles();
        log.info("Product with ID {} deleted successfully by user {}", id, userEmail);
    }

    @PutMapping("/updateProduct")
    public Product updateProduct(@RequestHeader("user-email")
                                 String userEmail, @RequestBody
                                 Product product) {
        log.info("WRITE operation performed by user: " + userEmail);
        log.info("User {} performed WRITE operation: updating product {}", userEmail, product);
        Product updatedProduct = productService.updateProduct(product);
        log.info("Product updated successfully: {}", updatedProduct);
        profileGenerator.generateAggregatedProfiles();
        return updatedProduct;
    }

    @GetMapping("/readAllProducts")
    public List<Product> getAllProducts(@RequestHeader("user-email")
                                        String userEmail) {
        log.info("User {} performed READ operation: fetching all products", userEmail);
        List<Product> products = productService.getAllProducts();
        log.info("Total products fetched by user {}: {}", userEmail, products.size());
        profileGenerator.generateAggregatedProfiles();
        return products;
    }

    @GetMapping("/most-expensive-products")
    public List<Product> getTopExpensiveProducts(@RequestHeader("user-email")
                                                     String userEmail) {
        List<Product> expensiveProducts = productService.getTopExpensiveProducts();
        log.info("User {} searched for the most expensive products", userEmail);
        profileGenerator.generateAggregatedProfiles();
        return expensiveProducts;
    }
}