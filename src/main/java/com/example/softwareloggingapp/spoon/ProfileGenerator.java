package com.example.softwareloggingapp.spoon;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ProfileGenerator is responsible for aggregating user activity profiles from logs
 * and saving the results to a JSON file. It processes log entries to extract user
 * information and their associated operations, then creates aggregated profiles.
 */
@Service
public class ProfileGenerator {

    // Path to the input log file containing user activity logs
    private static final String INPUT_FILE_PATH = "logs/application.json";

    // Path to the output JSON file to save aggregated user profiles
    private static final String OUTPUT_FILE_PATH = "logs/aggregated_user_profiles.json";

    // ObjectMapper for JSON parsing and writing
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Reads the input log file, processes each log entry to aggregate user profiles,
     * and writes the aggregated profiles to an output JSON file.
     */
    public void generateAggregatedProfiles() {
        Map<String, UserProfile> userProfiles = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_PATH))) {
            String line;

            // Process each log entry line by line
            while ((line = br.readLine()) != null) {
                try {
                    // Parse the log entry as a generic JSON object (map)
                    Map<String, Object> logEntry = objectMapper.readValue(line, Map.class);

                    // Extract user-specific information from the log message
                    String message = (String) logEntry.get("message");
                    if (message != null && message.contains("User")) {
                        String email = extractEmail(message);
                        String operation = extractOperation(message);

                        // Only process valid operations (ignore "UNKNOWN")
                        if (email != null && operation != null && !operation.equals("UNKNOWN")) {
                            // Add or update the user's profile
                            userProfiles.putIfAbsent(email, new UserProfile(email));
                            UserProfile profile = userProfiles.get(email);
                            profile.incrementOperation(operation);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing log entry: " + e.getMessage());
                }
            }

            // Write the aggregated user profiles to a JSON file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(OUTPUT_FILE_PATH), userProfiles);
            System.out.println("\nAggregated user profiles have been saved to: " + OUTPUT_FILE_PATH);

        } catch (IOException e) {
            System.err.println("Error processing the file: " + e.getMessage());
        }
    }

    /**
     * Extracts the email address from a log message.
     *
     * @param message The log message containing the user email.
     * @return The extracted email address, or null if not found.
     */
    private String extractEmail(String message) {
        int startIndex = message.indexOf("User ");
        int endIndex = message.indexOf(" ", startIndex + 5);
        if (startIndex != -1 && endIndex != -1) {
            return message.substring(startIndex + 5, endIndex);
        }
        return null;
    }

    /**
     * Determines the type of operation performed based on the log message.
     *
     * @param message The log message containing operation details.
     * @return The operation type (e.g., "READ", "WRITE", "MOST_EXPENSIVE_SEARCH", or "UNKNOWN").
     */
    private String extractOperation(String message) {
        if (message.contains("SEARCH") || message.contains("viewing") || message.contains("fetching")) {
            return "READ";
        } else if (message.contains("WRITE") || message.contains("adding a new product") || message.contains("updated") || message.contains("DELETE")) {
            return "WRITE";
        } else if (message.contains("searched for the most expensive products")) {
            return "MOST_EXPENSIVE_SEARCH";
        }
        return "UNKNOWN";
    }

    /**
     * Represents a user profile containing aggregated data about the user's operations.
     */
    static class UserProfile {
        private String email; // The email of the user
        private Map<String, Integer> operationCounts = new HashMap<>(); // Counts of each operation type
        private String primaryOperation; // The most frequent operation performed by the user

        /**
         * Constructs a new UserProfile for the given email.
         *
         * @param email The email of the user.
         */
        public UserProfile(String email) {
            this.email = email;
        }

        /**
         * Increments the count of the specified operation and updates the primary operation.
         *
         * @param operation The operation type to increment.
         */
        public void incrementOperation(String operation) {
            operationCounts.put(operation, operationCounts.getOrDefault(operation, 0) + 1);
            updatePrimaryOperation();
        }

        /**
         * Updates the primary operation to the one with the highest count.
         */
        private void updatePrimaryOperation() {
            primaryOperation = operationCounts.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);
        }

        // Getters for the user profile properties
        public String getEmail() {
            return email;
        }

        public Map<String, Integer> getOperationCounts() {
            return operationCounts;
        }

        public String getPrimaryOperation() {
            return primaryOperation;
        }
    }
}
