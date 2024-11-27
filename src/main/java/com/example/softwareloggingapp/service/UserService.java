package com.example.softwareloggingapp.service;

import com.example.softwareloggingapp.model.User;
import com.example.softwareloggingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean authenticate(String email, String password) {
        return userRepository.findAll().stream()
                .anyMatch(user -> user.getEmail().equals(email) && user.getPassword().equals(password));
    }

}
