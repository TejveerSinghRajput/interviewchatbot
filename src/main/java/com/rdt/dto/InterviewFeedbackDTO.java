package com.rdt.dto;

public class InterviewFeedbackDTO {

    private Long id;
    private Integer totalScore;
    private String strengths;
    private String weaknesses;
    private String improvementSuggestions;

    private Long sessionId; // ✅ FIXED

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public String getStrengths() {
        return strengths;
    }

    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }

    public String getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(String weaknesses) {
        this.weaknesses = weaknesses;
    }

    public String getImprovementSuggestions() {
        return improvementSuggestions;
    }

    public void setImprovementSuggestions(String improvementSuggestions) {
        this.improvementSuggestions = improvementSuggestions;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "InterviewFeedbackDTO{" +
                "id=" + id +
                ", totalScore=" + totalScore +
                ", strengths='" + strengths + '\'' +
                ", weaknesses='" + weaknesses + '\'' +
                ", improvementSuggestions='" + improvementSuggestions + '\'' +
                ", sessionId=" + sessionId +
                '}';
    }
}
