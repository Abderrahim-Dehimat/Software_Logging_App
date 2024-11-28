package com.example.softwareloggingapp.service;

import com.example.softwareloggingapp.model.Product;
import com.example.softwareloggingapp.repository.ProductRepository;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing Product entities.
 * Provides methods for CRUD operations and specific business logic
 * such as retrieving the most expensive products.
 */
@Service
@RequiredArgsConstructor
public class ProductService {
    // Repository for managing Product entities
    private final ProductRepository productRepository;
    // OpenTelemetry Tracer for distributed tracing
    private final Tracer tracer;

    /**
     * Adds a new product to the repository.
     * @param product The Product entity to add.
     * @return The saved Product entity.
     */
    public Product addProduct(Product product) {
        Span span = tracer.spanBuilder("addProduct").startSpan();
        try {
            return productRepository.save(product);
        } catch (Exception e) {
            span.recordException(e); // Record any exception in the trace
            throw e;
        } finally {
            span.end(); // End the trace
        }
    }

    /**
     * Retrieves a product by its ID.
     * @param id The ID of the Product to retrieve.
     * @return The Product entity.
     * @throws RuntimeException if the product is not found.
     */
    public Product getProductById(String id) {
        Span span = tracer.spanBuilder("fetchProductById").startSpan();
        try {
            return productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found!"));
        } catch (Exception e) {
            span.recordException(e); // Record any exception in the trace
            throw e;
        } finally {
            span.end(); // End the trace
        }
    }

    /**
     * Deletes a product by its ID.
     * @param id The ID of the Product to delete.
     * @throws RuntimeException if the product is not found.
     */
    public void deleteProduct(String id) {
        Span span = tracer.spanBuilder("deleteProduct").startSpan();
        try {
            if (!productRepository.existsById(id)) {
                throw new RuntimeException("Product not found!");
            }
            productRepository.deleteById(id);
        } catch (Exception e) {
            span.recordException(e); // Record any exception in the trace
            throw e;
        } finally {
            span.end(); // End the trace
        }
    }

    /**
     * Updates an existing product in the repository.
     * @param product The Product entity with updated values.
     * @return The updated Product entity.
     * @throws RuntimeException if the product is not found.
     */
    public Product updateProduct(Product product) {
        Span span = tracer.spanBuilder("fetchProductById").startSpan();
        try {
            if (!productRepository.existsById(product.getId())) {
                throw new RuntimeException("Product not found!");
            }
            return productRepository.save(product);
        } catch (Exception e) {
            span.recordException(e); // Record any exception in the trace
            throw e;
        } finally {
            span.end(); // End the trace
        }
    }

    /**
     * Retrieves all products from the repository.
     * @return A list of all Product entities.
     */
    public List<Product> getAllProducts() {
        Span span = tracer.spanBuilder("displayAllProducts").startSpan();
        try {
            return productRepository.findAll();
        } catch (Exception e) {
            span.recordException(e); // Record any exception in the trace
            throw e;
        } finally {
            span.end(); // End the trace
        }
    }

    /**
     * Retrieves the top 3 most expensive products from the repository.
     * @return A list of the top 3 most expensive Product entities.
     */
    public List<Product> getTopExpensiveProducts() {
        Span span = tracer.spanBuilder("getMostExpensiveProducts").startSpan();
        try {
            // Retrieve all products
            List<Product> allProducts = productRepository.findAll();

            // Sort and filter the top 3 most expensive products
            List<Product> topExpensiveProducts = new ArrayList<>();
            for (Product product : allProducts) {
                if (topExpensiveProducts.size() < 3) {
                    topExpensiveProducts.add(product);
                    topExpensiveProducts.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
                } else if (product.getPrice() > topExpensiveProducts.get(2).getPrice()) {
                    topExpensiveProducts.set(2, product);
                    topExpensiveProducts.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
                }
            }

            return topExpensiveProducts;
        } catch (Exception e) {
            span.recordException(e); // Record any exception in the trace
            throw e;
        } finally {
            span.end(); // End the trace
        }
    }
}
