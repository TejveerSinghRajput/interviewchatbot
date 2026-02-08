package com.rdt.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "interview_responses")
public class InterviewResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String answerText;

    private String audioUrl; // voice recording path

    private Integer score;

    @Column(columnDefinition = "TEXT")
    private String aiRemarks;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // getters & setters
}
