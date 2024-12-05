package com.example.softwareloggingapp.spoon;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.*;
import org.springframework.stereotype.Service;
/**
 * ProfileGenerator is responsible for aggregating user activity profiles from logs
 * and saving the results to separate JSON files for each operation type.
 */
@Service
public class ProfileGenerator {
    private static final String INPUT_FILE_PATH = "logs/application.json";// Input log file


    private static final String OUTPUT_DIRECTORY = "logs/";// Directory for output profiles


    private static final ObjectMapper objectMapper = new ObjectMapper();// JSON parser


    /**
     * Generates aggregated profiles for READ, WRITE, and MOST_EXPENSIVE_SEARCH operations.
     * Profiles include detailed logs for each user and are saved to separate JSON files.
     */
    public void generateAggregatedProfiles() {
        Map<String, List<Map<String, Object>>> readProfiles = new HashMap<>();
        Map<String, List<Map<String, Object>>> writeProfiles = new HashMap<>();
        Map<String, List<Map<String, Object>>> mostExpensiveSearchProfiles = new HashMap<>();
        try (Scanner scanner = new Scanner(new File(INPUT_FILE_PATH))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                try {
                    Map<String, Object> logEntry = objectMapper.readValue(line, Map.class);
                    String message = ((String) (logEntry.get("message")));
                    if ((message != null) && message.contains("User")) {
                        String email = extractEmail(message);
                        String operation = extractOperation(message);
                        if ((email != null) && (operation != null)) {
                            logEntry.put("user", email);// Add user info to log entry

                            logEntry.remove("level_value");// Remove unnecessary fields

                            logEntry = reorderFields(logEntry);// Reorder fields

                            if ("READ".equals(operation)) {
                                readProfiles.putIfAbsent(email, new ArrayList<>());
                                readProfiles.get(email).add(logEntry);
                            } else if ("WRITE".equals(operation)) {
                                writeProfiles.putIfAbsent(email, new ArrayList<>());
                                writeProfiles.get(email).add(logEntry);
                            } else if ("MOST_EXPENSIVE_SEARCH".equals(operation)) {
                                mostExpensiveSearchProfiles.putIfAbsent(email, new ArrayList<>());
                                mostExpensiveSearchProfiles.get(email).add(logEntry);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing log entry: " + e.getMessage());
                }
            } 
            saveProfiles("READ", readProfiles);
            saveProfiles("WRITE", writeProfiles);
            saveProfiles("MOST_EXPENSIVE_SEARCH", mostExpensiveSearchProfiles);
        } catch (IOException e) {
            System.err.println("Error reading the log file: " + e.getMessage());
        }
    }

    private void saveProfiles(String operationType, Map<String, List<Map<String, Object>>> profiles) {
        List<Map<String, Object>> formattedProfiles = new ArrayList<>();
        // Sort by operation count
        profiles.entrySet().stream().sorted((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size())).forEach(entry -> {
            Map<String, Object> userProfile = new LinkedHashMap<>();// Use LinkedHashMap for field order

            userProfile.put("user", entry.getKey());
            userProfile.put("operationCount", entry.getValue().size());
            userProfile.put("operations", entry.getValue());
            formattedProfiles.add(userProfile);
        });
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("operation", operationType);
        result.put("profiles", formattedProfiles);
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File((OUTPUT_DIRECTORY + operationType.toLowerCase()) + "_profiles.json"), result);
        } catch (IOException e) {
            System.err.println("Error writing profiles to file: " + e.getMessage());
        }
    }

    private String extractEmail(String message) {
        int startIndex = message.indexOf("User ");
        int endIndex = message.indexOf(" ", startIndex + 5);
        if ((startIndex != (-1)) && (endIndex != (-1))) {
            return message.substring(startIndex + 5, endIndex);
        }
        return null;
    }

    private String extractOperation(String message) {
        if (message.contains("READ operation")) {
            return "READ";
        } else if (message.contains("WRITE operation") || message.contains("DELETE operation")) {
            return "WRITE";
        } else if (message.contains("searched for the most expensive products")) {
            return "MOST_EXPENSIVE_SEARCH";
        }
        return null;
    }

    private Map<String, Object> reorderFields(Map<String, Object> logEntry) {
        Map<String, Object> reorderedLog = new LinkedHashMap<>();
        reorderedLog.put("user", logEntry.get("user"));// Move user to the top

        reorderedLog.put("@timestamp", logEntry.get("@timestamp"));
        reorderedLog.put("message", logEntry.get("message"));
        reorderedLog.put("logger_name", logEntry.get("logger_name"));
        reorderedLog.put("thread_name", logEntry.get("thread_name"));
        reorderedLog.put("level", logEntry.get("level"));
        return reorderedLog;
    }
}