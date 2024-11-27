package com.example.softwareloggingapp.spoon;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
public class ProfileGenerator {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        // Read logs from the JSON file
        List<Map<String, Object>> logs = mapper.readValue(new File("logs/application.json"), List.class);
        // Group logs by user email
        Map<String, List<Map<String, Object>>> logsByUser = logs.stream().filter(log -> log.containsKey("userEmail")).collect(Collectors.groupingBy(log -> log.get("userEmail").toString()));
        // Generate user profiles
        List<Map<String, Object>> profiles = new ArrayList<>();
        for (String email : logsByUser.keySet()) {
            Map<String, Object> profile = new HashMap<>();
            profile.put("email", email);
            // Count operation types
            Map<String, Long> operationCounts = logsByUser.get(email).stream().collect(Collectors.groupingBy(log -> log.get("operation").toString(), Collectors.counting()));
            // Add profile details
            profile.put("operationCounts", operationCounts);
            profile.put("primaryOperation", operationCounts.entrySet().stream().max(Map.Entry.comparingByValue()).orElseThrow().getKey());
            profiles.add(profile);
        }
        // Save profiles to a JSON file
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("profiles.json"), profiles);
    }
}