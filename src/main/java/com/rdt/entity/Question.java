package com.rdt.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String questionText;

    private String topic; // Streams, Spring Security, React Hooks

    private Integer maxScore;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private InterviewSession interviewSession;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<InterviewResponse> responses;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<InterviewAnswer> answers;


    // getters & setters
}
