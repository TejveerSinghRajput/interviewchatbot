package com.rdt.dto;

public class StartInterviewResponse {

    private String sessionId;
    private String questionId;
    private String questionText;

    public StartInterviewResponse() {
    }

    public StartInterviewResponse(String sessionId, String questionId, String questionText) {
        this.sessionId = sessionId;
        this.questionId = questionId;
        this.questionText = questionText;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    @Override
    public String toString() {
        return "StartInterviewResponse{" +
                "sessionId='" + sessionId + '\'' +
                ", questionId='" + questionId + '\'' +
                ", questionText='" + questionText + '\'' +
                '}';
    }
}
