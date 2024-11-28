package com.example.softwareloggingapp.lps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builder class for constructing and managing UserProfile objects.
 * Follows the Builder Design Pattern to ensure a structured way of creating UserProfile instances.
 */
public class UserProfileBuilder {

    // Email associated with the user profile
    private String email;

    // Map to store operation counts categorized by operation types
    private Map<String, Integer> operationCounts = new HashMap<>();

    // List to store timestamps of operations performed by the user
    private List<String> timestamps = new ArrayList<>();

    // Primary operation type performed by the user
    private String primaryOperation;

    /**
     * Sets the email for the UserProfile.
     *
     * @param email the email address of the user
     * @return the current instance of UserProfileBuilder for method chaining
     */
    public UserProfileBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * Increments the count of a specific operation and logs its timestamp.
     *
     * @param operation the operation type (e.g., READ, WRITE)
     * @param timestamp the timestamp of the operation
     * @return the current instance of UserProfileBuilder for method chaining
     */
    public UserProfileBuilder incrementOperation(String operation, String timestamp) {
        operationCounts.put(operation, operationCounts.getOrDefault(operation, 0) + 1);
        timestamps.add(timestamp);
        return this;
    }

    /**
     * Finalizes the user profile by determining the primary operation based on the highest frequency.
     *
     * @return the current instance of UserProfileBuilder for method chaining
     */
    public UserProfileBuilder finalizeProfile() {
        this.primaryOperation = operationCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("UNKNOWN");
        return this;
    }

    /**
     * Builds the UserProfile object based on the collected data.
     *
     * @return a fully constructed UserProfile instance
     */
    public UserProfile build() {
        UserProfile profile = new UserProfile(email); // Create a new UserProfile instance
        profile.setEmail(email); // Set the user's email
        profile.setOperationCounts(operationCounts); // Set the operation counts
        profile.setPrimaryOperation(primaryOperation); // Set the primary operation
        profile.setTimestamps(timestamps); // Set the operation timestamps
        return profile;
    }
}
