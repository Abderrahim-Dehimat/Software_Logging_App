package com.example.softwareloggingapp.controller;

import com.example.softwareloggingapp.model.Product;
import com.example.softwareloggingapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j // Annotation for SLF4J logger
public class ProductController {
    private final ProductService productService;

    @PostMapping("/create")
    public Product addProduct(@RequestBody Product product) {
        log.info("Request to create product: {}", product);
        Product createdProduct = productService.addProduct(product);
        log.info("Product created successfully: {}", createdProduct);
        return createdProduct;
    }

    @GetMapping("/readProductById/{id}")
    public Product getProductById(@PathVariable String id) {
        log.info("Request to fetch product with ID: {}", id);
        Product product = productService.getProductById(id);
        log.info("Product fetched successfully: {}", product);
        return product;
    }

    @DeleteMapping("/deleteProduct/{id}")
    public void deleteProduct(@PathVariable String id) {
        log.warn("Request to delete product with ID: {}", id);
        productService.deleteProduct(id);
        log.info("Product with ID {} deleted successfully.", id);
    }

    @PutMapping("/updateProduct")
    public Product updateProduct(@RequestBody Product product) {
        log.info("Request to update product: {}", product);
        Product updatedProduct = productService.updateProduct(product);
        log.info("Product updated successfully: {}", updatedProduct);
        return updatedProduct;
    }

    @GetMapping("/readAllProducts")
    public List<Product> getAllProducts() {
        log.info("Request to fetch all products.");
        List<Product> products = productService.getAllProducts();
        log.info("Total products fetched: {}", products.size());
        return products;
    }
}
