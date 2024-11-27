package com.example.softwareloggingapp.spoon;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProfileGenerator {

    private static final String INPUT_FILE_PATH = "logs/application.json";
    private static final String OUTPUT_FILE_PATH = "logs/aggregated_user_profiles.json";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void generateAggregatedProfiles() {
        Map<String, UserProfile> userProfiles = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_PATH))) {
            String line;

            // Process each log entry
            while ((line = br.readLine()) != null) {
                try {
                    Map<String, Object> logEntry = objectMapper.readValue(line, Map.class);

                    String message = (String) logEntry.get("message");
                    if (message != null && message.contains("User")) {
                        String email = extractEmail(message);
                        String operation = extractOperation(message);

                        if (email != null && operation != null && !operation.equals("UNKNOWN")) {
                            userProfiles.putIfAbsent(email, new UserProfile(email));
                            UserProfile profile = userProfiles.get(email);
                            profile.incrementOperation(operation);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing log entry: " + e.getMessage());
                }
            }

            // Write aggregated profiles to a JSON file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(OUTPUT_FILE_PATH), userProfiles);
            System.out.println("\nAggregated user profiles have been saved to: " + OUTPUT_FILE_PATH);

        } catch (IOException e) {
            System.err.println("Error processing the file: " + e.getMessage());
        }
    }

    private String extractEmail(String message) {
        int startIndex = message.indexOf("User ");
        int endIndex = message.indexOf(" ", startIndex + 5);
        if (startIndex != -1 && endIndex != -1) {
            return message.substring(startIndex + 5, endIndex);
        }
        return null;
    }

    private String extractOperation(String message) {
        if (message.contains("SEARCH") || message.contains("viewing") || message.contains("fetching")) {
            return "READ";
        } else if (message.contains("WRITE") || message.contains("adding a new product") || message.contains("updated")) {
            return "WRITE";
        } else if (message.contains("searched for the most expensive products")) {
            return "MOST_EXPENSIVE_SEARCH";
        }
        return "UNKNOWN";
    }

    static class UserProfile {
        private String email;
        private Map<String, Integer> operationCounts = new HashMap<>();
        private String primaryOperation;

        public UserProfile(String email) {
            this.email = email;
        }

        public void incrementOperation(String operation) {
            operationCounts.put(operation, operationCounts.getOrDefault(operation, 0) + 1);
            updatePrimaryOperation();
        }

        private void updatePrimaryOperation() {
            primaryOperation = operationCounts.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);
        }

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
