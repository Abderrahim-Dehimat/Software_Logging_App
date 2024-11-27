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

public class LogParser {

    public static void main(String[] args) {
        String logFilePath = "logs/application.json";
        String outputFilePath = "logs/structured_lps_profiles.json";
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, UserProfile> userProfiles = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Map<String, Object> logEntry = objectMapper.readValue(line, Map.class);
                String email = extractEmail((String) logEntry.get("message"));
                String operation = extractOperation((String) logEntry.get("message"));
                String timestamp = (String) logEntry.get("@timestamp");

                if (email != null && operation != null) {
                    userProfiles.putIfAbsent(email, new UserProfile(email));
                    UserProfile profile = userProfiles.get(email);
                    profile.addActivity(timestamp, operation, (String) logEntry.get("message"));
                }
            }

            List<UserProfile> profiles = new ArrayList<>(userProfiles.values());
            System.out.println("Generated User Profiles:");
            for (UserProfile profile : profiles) {
                System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(profile));
            }

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new FileWriter(outputFilePath), profiles);
            System.out.println("User profiles saved to: " + outputFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String extractEmail(String message) {
        if (message != null && message.contains("User")) {
            int startIndex = message.indexOf("User ") + 5;
            int endIndex = message.indexOf(" ", startIndex);
            return message.substring(startIndex, endIndex);
        }
        return null;
    }

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

    static class UserProfile {
        private String email;
        private List<Activity> activities = new ArrayList<>();
        private Map<String, Integer> summary = new HashMap<>();

        public UserProfile(String email) {
            this.email = email;
        }

        public void addActivity(String timestamp, String operation, String action) {
            activities.add(new Activity(timestamp, operation, action));
            summary.put(operation, summary.getOrDefault(operation, 0) + 1);
        }

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

    static class Activity {
        private String timestamp;
        private String event;
        private String action;

        public Activity(String timestamp, String event, String action) {
            this.timestamp = timestamp;
            this.event = event;
            this.action = action;
        }

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
