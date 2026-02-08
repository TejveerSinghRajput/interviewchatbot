package com.rdt.dto;

public class InterviewAnswerDTO {

    private Long id;
    private String answerText;
    private String audioUrl;
    private Integer score;
    private String aiFeedback;

    private Long questionId; // ✅ FIXED

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getAiFeedback() {
        return aiFeedback;
    }

    public void setAiFeedback(String aiFeedback) {
        this.aiFeedback = aiFeedback;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    @Override
    public String toString() {
        return "InterviewAnswerDTO{" +
                "id=" + id +
                ", answerText='" + answerText + '\'' +
                ", audioUrl='" + audioUrl + '\'' +
                ", score=" + score +
                ", aiFeedback='" + aiFeedback + '\'' +
                ", questionId=" + questionId +
                '}';
    }
}
