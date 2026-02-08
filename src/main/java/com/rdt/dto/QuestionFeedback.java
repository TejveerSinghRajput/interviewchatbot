package com.rdt.dto;

public class QuestionFeedback {

    private String question;
    private String answer;
    private int score;
    private String feedback;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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

    @Override
    public String toString() {
        return "QuestionFeedback{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", score=" + score +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}
