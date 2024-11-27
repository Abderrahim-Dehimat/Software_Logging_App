package com.example.softwareloggingapp.lps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfileBuilder {
    private String email;
    private Map<String, Integer> operationCounts = new HashMap<>();
    private List<String> timestamps = new ArrayList<>();
    private String primaryOperation;

    public UserProfileBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserProfileBuilder incrementOperation(String operation, String timestamp) {
        operationCounts.put(operation, operationCounts.getOrDefault(operation, 0) + 1);
        timestamps.add(timestamp);
        return this;
    }

    public UserProfileBuilder finalizeProfile() {
        this.primaryOperation = operationCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("UNKNOWN");
        return this;
    }

    public UserProfile build() {
        UserProfile profile = new UserProfile( email);
        profile.setEmail(email);
        profile.setOperationCounts(operationCounts);
        profile.setPrimaryOperation(primaryOperation);
        profile.setTimestamps(timestamps);
        return profile;
    }

}

