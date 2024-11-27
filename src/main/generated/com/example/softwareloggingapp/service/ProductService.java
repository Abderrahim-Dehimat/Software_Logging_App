package com.example.softwareloggingapp.service;
import com.example.softwareloggingapp.model.Product;
import com.example.softwareloggingapp.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product addProduct(Product product) {
        log.info("WRITE operation performed by user: " + authenticatedUserEmail);
        if (productRepository.existsById(product.getId())) {
            throw new RuntimeException("Product with ID already exists!");
        }
        return productRepository.save(product);
    }

    public Product getProductById(String id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found!"));
    }

    public void deleteProduct(String id) {
        log.info("WRITE operation performed by user: " + authenticatedUserEmail);
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found!");
        }
        productRepository.deleteById(id);
    }

    public Product updateProduct(Product product) {
        log.info("WRITE operation performed by user: " + authenticatedUserEmail);
        if (!productRepository.existsById(product.getId())) {
            throw new RuntimeException("Product not found!");
        }
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}