package com.example.softwareloggingapp.cli;

import com.example.softwareloggingapp.model.Product;
import com.example.softwareloggingapp.model.User;
import com.example.softwareloggingapp.service.ProductService;
import com.example.softwareloggingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
@Slf4j // Enable SLF4J logging
public class CLI implements CommandLineRunner {
    private final UserService userService;
    private final ProductService productService;

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String... args) {
        System.out.println("=== Welcome to the Backend CLI ===");
        boolean exit = false;

        while (!exit) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Manage Users");
            System.out.println("2. Manage Products");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> manageUsers();
                case 2 -> manageProducts();
                case 0 -> {
                    System.out.println("Goodbye!");
                    exit = true;
                }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private void manageUsers() {
        System.out.println("\n=== Manage Users ===");
        System.out.println("1. Create User");
        System.out.println("2. Display All Users");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1 -> createUser();
            case 2 -> displayUsers();
            case 0 -> System.out.println("Returning to Main Menu...");
            default -> System.out.println("Invalid choice!");
        }
    }

    private void createUser() {
        log.info("Request to create user:");
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        User user = new User();
        user.setName(name);
        user.setAge(age);
        user.setEmail(email);
        user.setPassword(password);

        userService.createUser(user);
        System.out.println("User created successfully!");
        log.info("User added successfully: {}", user);
    }

    private void displayUsers() {
        // Logging when fetching users
        log.info("Fetching all users...");
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            System.out.println("=== Users ===");
            users.forEach(user -> System.out.println(user));
        }
    }

    private void manageProducts() {
        System.out.println("\n=== Manage Products ===");
        System.out.println("1. Display All Products");
        System.out.println("2. Fetch Product by ID");
        System.out.println("3. Add Product");
        System.out.println("4. Update Product");
        System.out.println("5. Delete Product");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1 -> displayProducts();
            case 2 -> fetchProductById();
            case 3 -> addProduct();
            case 4 -> updateProduct();
            case 5 -> deleteProduct();
            case 0 -> System.out.println("Returning to Main Menu...");
            default -> System.out.println("Invalid choice!");
        }
    }

    private void displayProducts() {
        // Logging when fetching products
        log.info("Fetching all products...");
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            System.out.println("=== Products ===");
            products.forEach(product -> System.out.println(product));
        }
    }

    private void fetchProductById() {
        System.out.print("Enter Product ID: ");
        String id = scanner.nextLine();
        try {
            // Logging when fetching a product by ID
            log.info("Fetching product with ID: {}", id);
            Product product = productService.getProductById(id);
            System.out.println("Product: " + product);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addProduct() {
        log.info("Request to add a product");
        System.out.print("Enter Product ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Product Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Product Price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter Expiration Date (yyyy-MM-dd): ");
        LocalDate expirationDate = LocalDate.parse(scanner.nextLine());

        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setExpirationDate(expirationDate);

        try {
            productService.addProduct(product);
            log.info("Product added successfully: {}", product);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateProduct() {
        log.info("Request to update a product");
        System.out.print("Enter Product ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Product Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Product Price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter Expiration Date (yyyy-MM-dd): ");
        LocalDate expirationDate = LocalDate.parse(scanner.nextLine());

        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setExpirationDate(expirationDate);

        try {
            productService.updateProduct(product);
            log.info("Product updated successfully: {}", product);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteProduct() {
        log.info("Request to delete a product");
        System.out.print("Enter Product ID: ");
        String id = scanner.nextLine();
        try {
            productService.deleteProduct(id);
            log.info("Product with ID {} deleted successfully.", id);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
