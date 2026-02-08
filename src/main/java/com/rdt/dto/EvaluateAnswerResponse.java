package com.rdt.dto;

public class EvaluateAnswerResponse {

    private int score; // 0 - 10
    private String feedback;
    private boolean interviewComplete;
    private String nextQuestionId;
    private String nextQuestionText;

    public EvaluateAnswerResponse() {
    }

    public EvaluateAnswerResponse(int score, String feedback, boolean interviewComplete,
                                  String nextQuestionId, String nextQuestionText) {
        this.score = score;
        this.feedback = feedback;
        this.interviewComplete = interviewComplete;
        this.nextQuestionId = nextQuestionId;
        this.nextQuestionText = nextQuestionText;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public boolean isInterviewComplete() {
        return interviewComplete;
    }

    public void setInterviewComplete(boolean interviewComplete) {
        this.interviewComplete = interviewComplete;
    }

    public String getNextQuestionId() {
        return nextQuestionId;
    }

    public void setNextQuestionId(String nextQuestionId) {
        this.nextQuestionId = nextQuestionId;
    }

    public String getNextQuestionText() {
        return nextQuestionText;
    }

    public void setNextQuestionText(String nextQuestionText) {
        this.nextQuestionText = nextQuestionText;
    }

    @Override
    public String toString() {
        return "EvaluateAnswerResponse{" +
                "score=" + score +
                ", feedback='" + feedback + '\'' +
                ", interviewComplete=" + interviewComplete +
                ", nextQuestionId='" + nextQuestionId + '\'' +
                ", nextQuestionText='" + nextQuestionText + '\'' +
                '}';
    }
}
