package com.rdt.dto;

public class StartInterviewRequest {

    private String candidateName;
    private String interviewType;
    private int totalQuestions;

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getInterviewType() {
        return interviewType;
    }

    public void setInterviewType(String interviewType) {
        this.interviewType = interviewType;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    @Override
    public String toString() {
        return "StartInterviewRequest{" +
                "candidateName='" + candidateName + '\'' +
                ", interviewType='" + interviewType + '\'' +
                ", totalQuestions=" + totalQuestions +
                '}';
    }
}
