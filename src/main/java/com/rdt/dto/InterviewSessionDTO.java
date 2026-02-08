package com.rdt.dto;

import java.time.LocalDateTime;
import java.util.List;

public class InterviewSessionDTO {

    private Long id;
    private String domain;
    private String difficulty;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    private Long userId; // ✅ FIXED

    private List<InterviewQuestionDTO> questions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<InterviewQuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<InterviewQuestionDTO> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "InterviewSessionDTO{" +
                "id=" + id +
                ", domain='" + domain + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", startedAt=" + startedAt +
                ", endedAt=" + endedAt +
                ", userId=" + userId +
                '}';
    }
}
