package com.rdt.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.patterns.TypePatternQuestions;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "interview_sessions")
public class InterviewSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String domain; // Java, Spring, React, System Design

    private String difficulty; // EASY / MEDIUM / HARD

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    @OneToMany(mappedBy = "interviewSession", cascade = CascadeType.ALL)
//    private List<TypePatternQuestions.Question> questions;

    @OneToOne(mappedBy = "interviewSession", cascade = CascadeType.ALL)
    private InterviewFeedback feedback;

    @OneToMany(mappedBy = "interviewSession", cascade = CascadeType.ALL)
    private List<InterviewQuestion> questions;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public InterviewFeedback getFeedback() {
        return feedback;
    }

    public void setFeedback(InterviewFeedback feedback) {
        this.feedback = feedback;
    }

    public List<InterviewQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<InterviewQuestion> questions) {
        this.questions = questions;
    }
}
