package com.rdt.serviceImpl;

import com.rdt.dto.*;
import com.rdt.entity.*;
import com.rdt.repository.*;
import com.rdt.service.InterviewService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
                                ChatModel chatModel) {
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
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));

        InterviewSession session = new InterviewSession();
        session.setDomain(request.getDomain());
        session.setDifficulty(request.getDifficulty());
        session.setStartedAt(LocalDateTime.now());
        session.setUser(user);
        session = sessionRepository.save(session);

        // Generate the first question (Later this will be an AI call)
        InterviewQuestion firstQuestion = new InterviewQuestion();
        firstQuestion.setQuestionText("Tell me about yourself and your experience with " + request.getDomain());
        firstQuestion.setTopic(request.getDomain());
        firstQuestion.setDifficulty(request.getDifficulty());
        firstQuestion.setMaxScore(10);
        firstQuestion.setInterviewSession(session);
        firstQuestion = questionRepository.save(firstQuestion);

        return new StartInterviewResponse(
                session.getId().toString(),
                firstQuestion.getId().toString(),
                firstQuestion.getQuestionText()
        );
    }

    @Override
    @Transactional
    public EvaluateAnswerResponse evaluateAnswer(EvaluateAnswerRequest request) {
        Long questionId = Long.parseLong(request.getQuestionId());

        InterviewQuestion question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        // 1. Save the current answer
        InterviewAnswer answer = new InterviewAnswer();
        answer.setAnswerText(request.getAnswerText());
        answer.setInterviewQuestion(question);
        answer.setScore(8); // Mock score - will be replaced by Spring AI
        answer.setAiFeedback("Good technical depth.");
        answerRepository.save(answer);

        // 2. Check if the interview should end (e.g., after 5 questions)
        boolean isComplete = question.getInterviewSession().getQuestions().size() >= 5;

        String nextQuestionId = null;
        String nextQuestionText = null;

        if (isComplete) {
            // 3. Use feedbackRepository to save the final summary
            InterviewFeedback feedback = new InterviewFeedback();
            feedback.setInterviewSession(question.getInterviewSession());
            feedback.setTotalScore(80);
            feedback.setStrengths("Strong Java Core");
            feedback.setWeaknesses("System Design");
            feedbackRepository.save(feedback);
        } else{
            // 4. Generate the next technical question using Spring AI
            nextQuestionText = chatModel.call("The candidate just answered: " + request.getAnswerText() +
                    ". Ask the next technical follow-up question for a " + question.getInterviewSession().getDomain() + " role.");

            // 5. Persist the generated question so it can be answered in the next turn
            InterviewQuestion nextQuestion = new InterviewQuestion();
            nextQuestion.setQuestionText(nextQuestionText);
            nextQuestion.setInterviewSession(question.getInterviewSession());
            nextQuestion.setTopic(question.getTopic());
            nextQuestion.setDifficulty(question.getDifficulty());
            nextQuestion.setMaxScore(10);
            nextQuestion = questionRepository.save(nextQuestion);

            nextQuestionId = nextQuestion.getId().toString();
        }

        return new EvaluateAnswerResponse(
                8,
                "Good explanation!",
                isComplete,
                nextQuestionId,
                nextQuestionText
        );
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewReportResponse getInterviewReport(Long sessionId) {
        // 1. Fetch the session
        InterviewSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found with ID: " + sessionId));

        // 2. Fetch the feedback summary using feedbackRepository
        InterviewFeedback feedback = feedbackRepository.findByInterviewSessionId(sessionId)
                .orElse(new InterviewFeedback()); // Return empty if not yet generated

        // 3. Map to Response DTO
        InterviewReportResponse report = new InterviewReportResponse();
        report.setSessionId(session.getId());
        report.setTotalQuestions(session.getQuestions() != null ? session.getQuestions().size() : 0);

        // Populate feedback details from the repository
        report.setOverallFeedback(feedback.getImprovementSuggestions());

        // Calculate average score from questions
        if (session.getQuestions() != null) {
            double average = session.getQuestions().stream()
                    .flatMap(q -> q.getAnswers().stream())
                    .mapToInt(InterviewAnswer::getScore)
                    .average()
                    .orElse(0.0);
            report.setAverageScore(average);
        }

        return report;
    }
}