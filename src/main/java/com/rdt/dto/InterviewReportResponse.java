package com.rdt.dto;

import lombok.Data;

import java.util.List;

public class InterviewReportResponse {

    private Long sessionId;
    private int totalQuestions;
    private double averageScore;
    private String overallFeedback;
    private List<QuestionFeedback> details;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public String getOverallFeedback() {
        return overallFeedback;
    }

    public void setOverallFeedback(String overallFeedback) {
        this.overallFeedback = overallFeedback;
    }

    public List<QuestionFeedback> getDetails() {
        return details;
    }

    public void setDetails(List<QuestionFeedback> details) {
        this.details = details;
    }
}
