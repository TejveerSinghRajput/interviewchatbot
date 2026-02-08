package com.rdt.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "interview_questions")
public class InterviewQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String questionText;

    private String topic;        // Streams, Spring Boot, React Hooks
    private String difficulty;   // EASY / MEDIUM / HARD
    private Integer maxScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private InterviewSession interviewSession;

    @OneToMany(mappedBy = "interviewQuestion", cascade = CascadeType.ALL)
    private List<InterviewAnswer> answers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private InterviewQuestion interviewQuestion;


    // getters & setters + toString
}
