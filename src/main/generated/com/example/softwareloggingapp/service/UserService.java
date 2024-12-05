package com.example.softwareloggingapp.service;
import com.example.softwareloggingapp.model.User;
import com.example.softwareloggingapp.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
/**
 * Service class for handling business logic related to users.
 * Provides methods for creating, retrieving, and authenticating users.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    // Repository for accessing user data from the database.
    private final UserRepository userRepository;

    /**
     * Creates a new user by saving it to the database.
     *
     * @param user
     * 		the User object to be created
     * @return the created User object
     */
    public User createUser(User user) {
        log.info("WRITE operation performed by user: " + authenticatedUserEmail);
        return userRepository.save(user);
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a list of all User objects
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Authenticates a user by checking their email and password.
     *
     * @param email
     * 		the email of the user attempting to authenticate
     * @param password
     * 		the password of the user
     * @return true if the email and password match a user in the database, false otherwise
     */
    public boolean authenticate(String email, String password) {
        return userRepository.findAll().stream().anyMatch(user -> user.getEmail().equals(email) && user.getPassword().equals(password));
    }
}