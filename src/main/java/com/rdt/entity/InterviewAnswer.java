package com.rdt.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "interview_answers")
public class InterviewAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Converted text from voice
    @Column(columnDefinition = "TEXT", nullable = false)
    private String answerText;

    // Stored voice file path / cloud URL
    private String audioUrl;

    // AI evaluation score per question
    private Integer score;

    // AI feedback for this answer
    @Column(columnDefinition = "TEXT")
    private String aiFeedback;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // getters & setters + toString
}
