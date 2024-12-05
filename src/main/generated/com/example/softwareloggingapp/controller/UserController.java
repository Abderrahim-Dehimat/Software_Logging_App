package com.example.softwareloggingapp.controller;
import com.example.softwareloggingapp.model.User;
import com.example.softwareloggingapp.service.UserService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
/**
 * Controller class responsible for handling user-related API requests.
 */
// Base URL for user-related endpoints
// Automatically generates a constructor for required fields
// Enables SLF4J logging for this class
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;// Dependency for user-related operations


    /**
     * Endpoint to create a new user.
     *
     * @param user
     * 		The user details sent in the request body.
     * @return The created user object.
     */
    @PostMapping("/createUser")
    public User createUser(@RequestBody
    User user) {
        log.info("WRITE operation performed by user: " + authenticatedUserEmail);
        log.info("Request to create user: {}", user);
        User createdUser = userService.createUser(user);// Delegate to service layer

        log.info("User created successfully: {}", createdUser);
        return createdUser;
    }

    /**
     * Endpoint to fetch all users.
     *
     * @return A list of all users.
     */
    @GetMapping("/readAllUsers")
    public List<User> getAllUsers() {
        log.info("Request to fetch all users.");
        List<User> users = userService.getAllUsers();// Retrieve all users via the service

        log.info("Total users fetched: {}", users.size());
        return users;
    }

    /**
     * Endpoint to authenticate a user.
     *
     * @param credentials
     * 		A map containing the user's email and password.
     * @return A boolean indicating whether the authentication was successful.
     */
    @PostMapping("/authenticate")
    public boolean authenticateUser(@RequestBody
    Map<String, String> credentials) {
        String email = credentials.get("email");// Extract email from the request body

        String password = credentials.get("password");// Extract password from the request body

        log.info("Authentication attempt for email: {}", email);
        return userService.authenticate(email, password);// Validate credentials via the service

    }
}