package com.example.softwareloggingapp.service;

import com.example.softwareloggingapp.model.Product;
import com.example.softwareloggingapp.repository.ProductRepository;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final Tracer tracer;



    public Product addProduct(Product product) {
        Span span = tracer.spanBuilder("addProduct").startSpan();
        try {
            return productRepository.save(product);

        }catch (Exception e) {
            span.recordException(e); // Enregistrer les erreurs dans la trace
            throw e;
        }finally {
            span.end(); // Terminer la trace
        }

    }

    public Product getProductById(String id) {
        Span span = tracer.spanBuilder("fetchProductById").startSpan();
        try {
            return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found!"));

        }catch (Exception e) {
            span.recordException(e); // Enregistrer les erreurs dans la trace
            throw e;
        }finally {
            span.end(); // Terminer la trace
        }

    }

    public void deleteProduct(String id) {
        Span span = tracer.spanBuilder("deleteProduct").startSpan();
        try {
            if (!productRepository.existsById(id)) {
                throw new RuntimeException("Product not found!");
            }
            productRepository.deleteById(id);

        }catch (Exception e) {
            span.recordException(e); // Enregistrer les erreurs dans la trace
            throw e;
        }finally {
            span.end(); // Terminer la trace
        }

    }

    public Product updateProduct(Product product) {
        Span span = tracer.spanBuilder("fetchProductById").startSpan();
        try {
            if (!productRepository.existsById(product.getId())) {
                throw new RuntimeException("Product not found!");
            }
            return productRepository.save(product);

        }catch (Exception e) {
            span.recordException(e); // Enregistrer les erreurs dans la trace
            throw e;
        }finally {
            span.end(); // Terminer la trace
        }

    }

    public List<Product> getAllProducts() {
        Span span = tracer.spanBuilder("displayAllProducts").startSpan();

        try {
            return productRepository.findAll();

        }catch (Exception e) {
            span.recordException(e); // Enregistrer les erreurs dans la trace
            throw e;
        }finally {
            span.end(); // Terminer la trace
        }


    }

    public List<Product> getTopExpensiveProducts() {
        Span span = tracer.spanBuilder("getMostExpensiveProducts").startSpan();
        try {

            // Retrieve all products from the repository
            List<Product> allProducts = productRepository.findAll();

            // Create a list to store the top 3 expensive products
            List<Product> topExpensiveProducts = new ArrayList<>();

            // Iterate to find the top 3 most expensive products
            for (Product product : allProducts) {
                if (topExpensiveProducts.size() < 3) {
                    // Add products directly until we have 3 products
                    topExpensiveProducts.add(product);
                    // Sort the list in descending order of price
                    topExpensiveProducts.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
                } else if (product.getPrice() > topExpensiveProducts.get(2).getPrice()) {
                    // Replace the cheapest product in the top 3 if the current product is more expensive
                    topExpensiveProducts.set(2, product);
                    // Re-sort the list
                    topExpensiveProducts.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
                }
            }

            return topExpensiveProducts;

        }catch (Exception e) {
            span.recordException(e); // Enregistrer les erreurs dans la trace
            throw e;
        }finally {
            span.end(); // Terminer la trace
        }

    }


}
