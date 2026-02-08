package com.rdt.serviceImpl;

import com.rdt.dto.*;
import com.rdt.entity.InterviewAnswer;
import com.rdt.entity.InterviewQuestion;
import com.rdt.entity.InterviewSession;
import com.rdt.entity.User;
import com.rdt.repository.InterviewAnswerRepository;
import com.rdt.repository.InterviewQuestionRepository;
import com.rdt.repository.InterviewSessionRepository;
import com.rdt.repository.UserRepository;
import com.rdt.service.InterviewService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InterviewServiceImpl implements InterviewService {

    private final InterviewSessionRepository sessionRepository;
    private final InterviewQuestionRepository questionRepository;
    private final InterviewAnswerRepository answerRepository;
    private final UserRepository userRepository;

    public InterviewServiceImpl(InterviewSessionRepository sessionRepository,
                                InterviewQuestionRepository questionRepository,
                                InterviewAnswerRepository answerRepository,
                                UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    // ✅ START INTERVIEW
    @Override
    public StartInterviewResponse startInterview(StartInterviewRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        InterviewSession session = new InterviewSession();
        session.setDomain(request.getDomain());
        session.setDifficulty(request.getDifficulty());
        session.setStartedAt(LocalDateTime.now());
        session.setUser(user);

        sessionRepository.save(session);

        InterviewQuestion question = new InterviewQuestion();
        question.setQuestionText("Explain Spring Boot Auto Configuration");
        question.setTopic("Spring Boot");
        question.setDifficulty("MEDIUM");
        question.setMaxScore(10);
        question.setInterviewSession(session);

        questionRepository.save(question);

        return new StartInterviewResponse(
                session.getId(),
                question.getId(),
                question.getQuestionText()
        );
    }

    // ✅ EVALUATE ANSWER
    @Override
    public EvaluateAnswerResponse evaluateAnswer(EvaluateAnswerRequest request) {

        InterviewQuestion question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        InterviewAnswer answer = new InterviewAnswer();
        answer.setAnswerText(request.getAnswerText());
        answer.setScore(7);
        answer.setAiFeedback("Good explanation, mention conditional annotations");
        answer.setInterviewQuestion(question);

        answerRepository.save(answer);

        return new EvaluateAnswerResponse(
                7,
                "Good explanation, mention conditional annotations",
                false,
                "Q2",
                "What is @ConditionalOnProperty?"
        );
    }

    // ✅ FINAL REPORT (placeholder)
    @Override
    public InterviewReportResponse getInterviewReport(Long sessionId) {
        return new InterviewReportResponse();
    }
}
