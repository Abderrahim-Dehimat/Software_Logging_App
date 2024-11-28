package com.example.softwareloggingapp.lps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a user's activity profile, tracking operations performed and associated timestamps.
 */
public class UserProfile {

    private String email; // The email identifier of the user.
    private Map<String, Integer> operationCounts; // Tracks the count of each operation type performed by the user.
    private String primaryOperation; // The most frequently performed operation type by the user.
    private List<String> timestamps; // List of timestamps corresponding to the operations.

    /**
     * Constructor to initialize the UserProfile with a user's email.
     *
     * @param email The email of the user.
     */
    public UserProfile(String email) {
        this.email = email;
        this.operationCounts = new HashMap<>();
        this.timestamps = new ArrayList<>();
    }

    /**
     * Increments the count of a specific operation performed by the user.
     * Updates the primary operation based on the new counts.
     *
     * @param operation The operation type to increment.
     */
    public void incrementOperation(String operation) {
        this.operationCounts.put(operation, this.operationCounts.getOrDefault(operation, 0) + 1);
        updatePrimaryOperation();
    }

    /**
     * Adds a timestamp to the list of timestamps for the user's operations.
     *
     * @param timestamp The timestamp to add.
     */
    public void addTimestamp(String timestamp) {
        this.timestamps.add(timestamp);
    }

    /**
     * Updates the primary operation based on the operation with the highest count.
     */
    private void updatePrimaryOperation() {
        this.primaryOperation = this.operationCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null); // Sets to null if no operations exist.
    }

    // Getters and Setters for accessing and modifying the class properties.

    public String getEmail() {
        return email;
    }

    public Map<String, Integer> getOperationCounts() {
        return operationCounts;
    }

    public String getPrimaryOperation() {
        return primaryOperation;
    }

    public List<String> getTimestamps() {
        return timestamps;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOperationCounts(Map<String, Integer> operationCounts) {
        this.operationCounts = operationCounts;
    }

    public void setPrimaryOperation(String primaryOperation) {
        this.primaryOperation = primaryOperation;
    }

    public void setTimestamps(List<String> timestamps) {
        this.timestamps = timestamps;
    }
}
