package com.example.softwareloggingapp.service;

import com.example.softwareloggingapp.model.Product;
import com.example.softwareloggingapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product addProduct(Product product) {
//        if (productRepository.existsById(product.getId())) {
//            throw new RuntimeException("Product with ID already exists!");
//        }
        return productRepository.save(product);
    }

    public Product getProductById(String id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found!"));
    }

    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found!");
        }
        productRepository.deleteById(id);
    }

    public Product updateProduct(Product product) {
        if (!productRepository.existsById(product.getId())) {
            throw new RuntimeException("Product not found!");
        }
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getTopExpensiveProducts() {
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
    }


}
