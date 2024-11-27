package com.example.softwareloggingapp.lps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfile {
    private String email;
    private Map<String, Integer> operationCounts;
    private String primaryOperation;
    private List<String> timestamps;

    public UserProfile(String email) {
        this.email = email;
        this.operationCounts = new HashMap<>();
        this.timestamps = new ArrayList<>();
    }

    public void incrementOperation(String operation) {
        this.operationCounts.put(operation, this.operationCounts.getOrDefault(operation, 0) + 1);
        updatePrimaryOperation();
    }

    public void addTimestamp(String timestamp) {
        this.timestamps.add(timestamp);
    }

    private void updatePrimaryOperation() {
        this.primaryOperation = this.operationCounts.entrySet().stream()
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
