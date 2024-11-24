package com.example.softwareloggingapp.controller;

import com.example.softwareloggingapp.model.User;
import com.example.softwareloggingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j // Annotation for SLF4J logger
public class UserController {
    private final UserService userService;

    @PostMapping("/createUser")
    public User createUser(@RequestBody User user) {
        log.info("Request to create user: {}", user);
        User createdUser = userService.createUser(user);
        log.info("User created successfully: {}", createdUser);
        return createdUser;
    }

    @GetMapping("/readAllUsers")
    public List<User> getAllUsers() {
        log.info("Request to fetch all users.");
        List<User> users = userService.getAllUsers();
        log.info("Total users fetched: {}", users.size());
        return users;
    }
}
