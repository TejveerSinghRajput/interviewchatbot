package com.rdt.dto;

import lombok.Data;

@Data
public class StartInterviewRequest {
    private Long userId;        // Changed from candidateName to userId to match Service
    private String domain;      // e.g., Java, Spring Boot
    private String difficulty;  // EASY, MEDIUM, HARD

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}