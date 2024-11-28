package com.example.softwareloggingapp.lps;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The LogParser class processes unstructured application logs to extract and organize
 * user activities into structured user profiles. It reads logs, extracts relevant
 * data, and generates JSON profiles with user activity summaries.
 */

public class LogParser {

    public static void main(String[] args) {
        // Path to the log file containing unstructured logs.
        String logFilePath = "logs/application.json";

        // Path where the structured profiles will be saved.
        String outputFilePath = "logs/structured_lps_profiles.json";

        // ObjectMapper is used for JSON serialization and deserialization.
        ObjectMapper objectMapper = new ObjectMapper();

        // Map to aggregate user activities based on their email.
        Map<String, UserProfile> userProfiles = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
            String line;

            // Reading each line of the log file.
            while ((line = br.readLine()) != null) {
                // Deserialize the JSON line into a Map.
                Map<String, Object> logEntry = objectMapper.readValue(line, Map.class);

                // Extract relevant information: email, operation, and timestamp.
                String email = extractEmail((String) logEntry.get("message"));
                String operation = extractOperation((String) logEntry.get("message"));
                String timestamp = (String) logEntry.get("@timestamp");

                if (email != null && operation != null) {
                    // Initialize user profile if not already present.
                    userProfiles.putIfAbsent(email, new UserProfile(email));

                    // Add the activity to the user's profile.
                    UserProfile profile = userProfiles.get(email);
                    profile.addActivity(timestamp, operation, (String) logEntry.get("message"));
                }
            }

            // Convert the aggregated profiles to a list for display and saving.
            List<UserProfile> profiles = new ArrayList<>(userProfiles.values());

            // Print each user's profile to the console in a readable format.
            System.out.println("Generated User Profiles:");
            for (UserProfile profile : profiles) {
                System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(profile));
            }

            // Save all user profiles to the output file as a JSON array.
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new FileWriter(outputFilePath), profiles);
            System.out.println("User profiles saved to: " + outputFilePath);

        } catch (IOException e) {
            // Print stack trace in case of any IO-related errors.
            e.printStackTrace();
        }
    }


    /**
     * Extracts the email address from the log message.
     * @param message The log message.
     * @return The extracted email address, or null if not found.
     */
    private static String extractEmail(String message) {
        if (message != null && message.contains("User")) {
            int startIndex = message.indexOf("User ") + 5;
            int endIndex = message.indexOf(" ", startIndex);
            return message.substring(startIndex, endIndex);
        }
        return null;
    }

    /**
     * Determines the operation type from the log message.
     * @param message The log message.
     * @return The operation type (e.g., READ, WRITE, MOST_EXPENSIVE_SEARCH).
     */
    private static String extractOperation(String message) {
        if (message.contains("SEARCH") || message.contains("READ") || message.contains("fetching")) {
            return "READ";
        } else if (message.contains("WRITE") || message.contains("adding a new product") || message.contains("updated")) {
            return "WRITE";
        } else if (message.contains("searched for the most expensive products")) {
            return "MOST_EXPENSIVE_SEARCH";
        }
        return null;
    }


    /**
     * Represents a user's profile containing their activities and a summary.
     */
    static class UserProfile {
        private String email; // User's email address.
        private List<Activity> activities = new ArrayList<>(); // List of user activities.
        private Map<String, Integer> summary = new HashMap<>(); // Summary of activity counts.

        public UserProfile(String email) {
            this.email = email;
        }

        /**
         * Adds a new activity to the user's profile.
         * Updates the activity summary.
         * @param timestamp The timestamp of the activity.
         * @param operation The operation performed.
         * @param action The detailed action message.
         */
        public void addActivity(String timestamp, String operation, String action) {
            activities.add(new Activity(timestamp, operation, action));
            summary.put(operation, summary.getOrDefault(operation, 0) + 1);
        }

        // Getter methods for accessing profile data.
        public String getEmail() {
            return email;
        }

        public List<Activity> getActivities() {
            return activities;
        }

        public Map<String, Integer> getSummary() {
            return summary;
        }
    }


    /**
     * Represents a single user activity with details.
     */
    static class Activity {
        private String timestamp; // The timestamp of the activity.
        private String event; // The type of event (e.g., READ, WRITE).
        private String action; // The detailed log message describing the action.

        public Activity(String timestamp, String event, String action) {
            this.timestamp = timestamp;
            this.event = event;
            this.action = action;
        }

        // Getter methods for accessing activity details.
        public String getTimestamp() {
            return timestamp;
        }

        public String getEvent() {
            return event;
        }

        public String getAction() {
            return action;
        }
    }

}
