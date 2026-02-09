package com.rdt.service.serviceImpl;

import com.rdt.dto.*;
import com.rdt.entity.*;
import com.rdt.repository.*;
import com.rdt.service.InterviewService;
import lombok.extern.slf4j.Slf4j; // Added for Logging
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InterviewServiceImpl implements InterviewService {

    private final InterviewSessionRepository sessionRepository;
    private final InterviewQuestionRepository questionRepository;
    private final InterviewAnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final InterviewFeedbackRepository feedbackRepository;
    private final ChatModel chatModel;

    public InterviewServiceImpl(InterviewSessionRepository sessionRepository,
                                InterviewQuestionRepository questionRepository,
                                InterviewAnswerRepository answerRepository,
                                UserRepository userRepository,
                                InterviewFeedbackRepository feedbackRepository,
                                @Qualifier("ollamaChatModel") ChatModel chatModel) {
        this.sessionRepository = sessionRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.feedbackRepository = feedbackRepository;
        this.chatModel = chatModel;
    }

    @Override
    @Transactional
    public StartInterviewResponse startInterview(StartInterviewRequest request) {
        log.info("Starting new interview session for userId: {} in domain: {}", request.getUserId(), request.getDomain());
        try {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));

            InterviewSession session = new InterviewSession();
            session.setDomain(request.getDomain());
            session.setDifficulty(request.getDifficulty());
            session.setStartedAt(LocalDateTime.now());
            session.setUser(user);
            session = sessionRepository.save(session);

            // AI Generation for the FIRST question
            String prompt = String.format("Generate a unique, technical opening interview question for a %s role. " +
                    "Keep it under 50 words. Difficulty: %s", request.getDomain(), request.getDifficulty());

            String aiFirstQuestion;
            try {
                aiFirstQuestion = chatModel.call(prompt);
            } catch (Exception e) {
                log.error("Ollama AI call failed during startInterview: {}", e.getMessage());
                aiFirstQuestion = "Explain the core concepts of " + request.getDomain() + " and its importance in modern architecture.";
            }

            InterviewQuestion firstQuestion = new InterviewQuestion();
            firstQuestion.setQuestionText(aiFirstQuestion);
            firstQuestion.setTopic(request.getDomain());
            firstQuestion.setDifficulty(request.getDifficulty());
            firstQuestion.setMaxScore(10);
            firstQuestion.setInterviewSession(session);
            firstQuestion = questionRepository.save(firstQuestion);

            log.info("Successfully created session: {} with first questionId: {}", session.getId(), firstQuestion.getId());
            return new StartInterviewResponse(
                    session.getId().toString(),
                    firstQuestion.getId().toString(),
                    firstQuestion.getQuestionText()
            );
        } catch (Exception e) {
            log.error("Critical failure in startInterview: {}", e.getMessage());
            throw new RuntimeException("Failed to initiate interview. Please try again.");
        }
    }

    @Override
    @Transactional
    public EvaluateAnswerResponse evaluateAnswer(EvaluateAnswerRequest request) {
        log.info("Evaluating answer for sessionId: {}, questionId: {}", request.getSessionId(), request.getQuestionId());
        try {
            Long questionId = Long.parseLong(request.getQuestionId());
            InterviewQuestion question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            String evaluationPrompt = String.format(
                    "Evaluate this answer for a %s role.\nQuestion: %s\nAnswer: %s\n" +
                            "Respond ONLY in this format -> Score: [0-10], Feedback: [Your text]",
                    question.getInterviewSession().getDomain(), question.getQuestionText(), request.getAnswerText());

            String aiEvaluation;
            try {
                aiEvaluation = chatModel.call(evaluationPrompt);
            } catch (Exception e) {
                log.error("AI Evaluation call failed: {}", e.getMessage());
                aiEvaluation = "Score: 5, Feedback: System was unable to process detailed feedback at this time.";
            }

            // Parsing with Fallbacks
            int dynamicScore = 5;
            String dynamicFeedback = aiEvaluation;
            try {
                if (aiEvaluation.contains("Score:") && aiEvaluation.contains("Feedback:")) {
                    String[] parts = aiEvaluation.split("Feedback:");
                    dynamicScore = Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
                    dynamicFeedback = parts[1].trim();
                }
            } catch (Exception e) {
                log.warn("Failed to parse AI response: {}. using defaults.", aiEvaluation);
            }

            InterviewAnswer answer = new InterviewAnswer();
            answer.setAnswerText(request.getAnswerText());
            answer.setInterviewQuestion(question);
            answer.setScore(dynamicScore);
            answer.setAiFeedback(dynamicFeedback);
            answerRepository.save(answer);

            boolean isComplete = question.getInterviewSession().getQuestions().size() >= 5;
            String nextQuestionId = null;
            String nextQuestionText = null;

            if (isComplete) {
                handleFinalFeedback(question.getInterviewSession(), dynamicScore);
            } else {
                nextQuestionText = generateNextQuestion(request.getAnswerText(), question);

                InterviewQuestion nextQuestion = new InterviewQuestion();
                nextQuestion.setQuestionText(nextQuestionText);
                nextQuestion.setInterviewSession(question.getInterviewSession());
                nextQuestion.setTopic(question.getTopic());
                nextQuestion.setDifficulty(question.getDifficulty());
                nextQuestion.setMaxScore(10);
                nextQuestion = questionRepository.save(nextQuestion);
                nextQuestionId = nextQuestion.getId().toString();
            }

            return new EvaluateAnswerResponse(dynamicScore, dynamicFeedback, isComplete, nextQuestionId, nextQuestionText);
        } catch (Exception e) {
            log.error("Error in evaluateAnswer: {}", e.getMessage());
            throw new RuntimeException("Evaluation failed. Please check logs.");
        }
    }

    private String generateNextQuestion(String lastAnswer, InterviewQuestion prevQ) {
        try {
            String prompt = "Based on the answer: '" + lastAnswer + "', ask a technical follow-up for " + prevQ.getTopic() + ". Max 50 words.";
            return chatModel.call(prompt);
        } catch (Exception e) {
            log.error("Next question generation failed: {}", e.getMessage());
            return "Can you elaborate on the best practices for implementing " + prevQ.getTopic() + " in a distributed system?";
        }
    }

    private void handleFinalFeedback(InterviewSession session, int lastScore) {
        log.info("Generating final report for session: {}", session.getId());
        InterviewFeedback feedback = new InterviewFeedback();
        feedback.setInterviewSession(session);
        feedback.setTotalScore(lastScore * 10);
        feedback.setStrengths("Technical core competency was demonstrated.");
        feedback.setWeaknesses("Need more focus on edge-case handling.");
        feedbackRepository.save(feedback);

        session.setEndedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewReportResponse getInterviewReport(Long sessionId) {
        log.info("Fetching report for sessionId: {}", sessionId);
        try {
            InterviewSession session = sessionRepository.findById(sessionId)
                    .orElseThrow(() -> new RuntimeException("Session not found"));

            InterviewFeedback feedback = feedbackRepository.findByInterviewSessionId(sessionId)
                    .orElse(new InterviewFeedback());

            InterviewReportResponse report = new InterviewReportResponse();
            report.setSessionId(session.getId());
            report.setTotalQuestions(session.getQuestions().size());
            report.setOverallFeedback("STRENGTHS: " + feedback.getStrengths() + "\nWEAKNESSES: " + feedback.getWeaknesses());

            List<QuestionFeedback> detailsList = session.getQuestions().stream()
                    .filter(q -> q.getAnswers() != null && !q.getAnswers().isEmpty())
                    .map(q -> {
                        QuestionFeedback qf = new QuestionFeedback();
                        qf.setQuestion(q.getQuestionText());
                        InterviewAnswer ans = q.getAnswers().get(0);
                        qf.setAnswer(ans.getAnswerText());
                        qf.setScore(ans.getScore());
                        qf.setFeedback(ans.getAiFeedback());
                        return qf;
                    }).collect(Collectors.toList());

            report.setDetails(detailsList);
            double average = detailsList.stream().mapToInt(QuestionFeedback::getScore).average().orElse(0.0);
            report.setAverageScore(Math.round(average * 10.0) / 10.0);

            return report;
        } catch (Exception e) {
            log.error("Failed to generate report for session {}: {}", sessionId, e.getMessage());
            throw new RuntimeException("Report generation failed.");
        }
    }
}