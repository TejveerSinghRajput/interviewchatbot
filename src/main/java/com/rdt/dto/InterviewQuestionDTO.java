package com.rdt.dto;

import java.util.List;

public class InterviewQuestionDTO {

    private Long id;
    private String questionText;
    private String topic;
    private String difficulty;
    private Integer maxScore;

    private Long sessionId; // ✅ FIXED

    private List<InterviewAnswerDTO> answers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public List<InterviewAnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<InterviewAnswerDTO> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "InterviewQuestionDTO{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", topic='" + topic + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", maxScore=" + maxScore +
                ", sessionId=" + sessionId +
                '}';
    }
}
