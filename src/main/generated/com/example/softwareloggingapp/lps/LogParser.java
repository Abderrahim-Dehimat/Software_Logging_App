package com.example.softwareloggingapp.lps;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;
/**
 * The LogParser class processes logs to organize operations by type and display the user's profile.
 */
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
                String email = extractEmail(((String) (logEntry.get("message"))));
                String operation = extractOperation(((String) (logEntry.get("message"))));
                String timestamp = ((String) (logEntry.get("@timestamp")));
                if ((email != null) && (operation != null)) {
                    userProfiles.putIfAbsent(email, new UserProfile(email));
                    UserProfile profile = userProfiles.get(email);
                    profile.addOperation(operation, timestamp, ((String) (logEntry.get("message"))));
                }
            } 
            List<UserProfile> sortedProfiles = new ArrayList<>(userProfiles.values());
            sortedProfiles.sort(Comparator.comparing(UserProfile::getTotalOperationCount).reversed());
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("profiles", sortedProfiles);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new FileWriter(outputFilePath), result);
            System.out.println("Structured profiles saved to: " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String extractEmail(String message) {
        if ((message != null) && message.contains("User")) {
            int startIndex = message.indexOf("User ") + 5;
            int endIndex = message.indexOf(" ", startIndex);
            return message.substring(startIndex, endIndex);
        }
        return null;
    }

    private static String extractOperation(String message) {
        if (message.contains("READ") || message.contains("fetching")) {
            return "READ";
        } else if ((message.contains("WRITE") || message.contains("adding")) || message.contains("updated")) {
            return "WRITE";
        } else if (message.contains("searched for the most expensive products")) {
            return "MOST_EXPENSIVE_SEARCH";
        }
        return null;
    }

    static class UserProfile {
        private final String user;

        private final Map<String, OperationDetails> operations = new HashMap<>();

        private final Map<String, Integer> summary = new HashMap<>();

        public UserProfile(String user) {
            this.user = user;
        }

        public void addOperation(String operation, String timestamp, String action) {
            operations.putIfAbsent(operation, new OperationDetails());
            OperationDetails details = operations.get(operation);
            details.addOperation(new Activity(timestamp, operation, action));
            summary.put(operation, summary.getOrDefault(operation, 0) + 1);
        }

        public String getUser() {
            return user;
        }

        public Map<String, OperationDetails> getOperations() {
            return operations;
        }

        public Map<String, Integer> getSummary() {
            return summary;
        }

        public int getTotalOperationCount() {
            return summary.values().stream().mapToInt(Integer::intValue).sum();
        }
    }

    static class OperationDetails {
        private final List<Activity> operations = new ArrayList<>();

        public void addOperation(Activity activity) {
            operations.add(activity);
        }

        public List<Activity> getOperations() {
            return operations;
        }

        public int getOperationCount() {
            return operations.size();
        }
    }

    static class Activity {
        private final String timestamp;

        private final String event;

        private final String action;

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