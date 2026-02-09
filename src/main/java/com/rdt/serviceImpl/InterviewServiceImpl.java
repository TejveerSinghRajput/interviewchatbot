package com.rdt.serviceImpl;

import com.rdt.dto.*;
import com.rdt.entity.*;
import com.rdt.repository.*;
import com.rdt.service.InterviewService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));

        InterviewSession session = new InterviewSession();
        session.setDomain(request.getDomain());
        session.setDifficulty(request.getDifficulty());
        session.setStartedAt(LocalDateTime.now());
        session.setUser(user);
        session = sessionRepository.save(session);

        // AI Generation for the FIRST question
        String prompt = String.format("Generate a unique, challenging %s level opening interview question for a %s role.",
                request.getDifficulty(), request.getDomain());
        String aiFirstQuestion = chatModel.call(prompt);

        InterviewQuestion firstQuestion = new InterviewQuestion();
        firstQuestion.setQuestionText(aiFirstQuestion);
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
        // 1. Retrieve the existing question from the database
        Long questionId = Long.parseLong(request.getQuestionId());
        InterviewQuestion question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with ID: " + questionId));

        // 2. Define the dynamic evaluation prompt for the Senior Technical Interviewer
        String evaluationPrompt = String.format(
                "As a Senior Technical Interviewer, evaluate this answer for a %s role.\n" +
                        "Question: %s\n" +
                        "Candidate Answer: %s\n" +
                        "Provide a score out of 10 and a brief feedback string. " +
                        "Format your response strictly as: Score: [number], Feedback: [text]",
                question.getInterviewSession().getDomain(),
                question.getQuestionText(),
                request.getAnswerText()
        );

        // 3. Call the local Ollama model via chatModel
        String aiEvaluation = chatModel.call(evaluationPrompt);

        // 4. Parse the AI response for score and feedback
        int dynamicScore = 0; // Default fallback
        String dynamicFeedback = aiEvaluation;

        try {
            if (aiEvaluation.contains("Score:") && aiEvaluation.contains("Feedback:")) {
                String scorePart = aiEvaluation.split("Feedback:")[0].replaceAll("[^0-9]", "");
                dynamicScore = Integer.parseInt(scorePart);
                dynamicFeedback = aiEvaluation.split("Feedback:")[1].trim();
            }
        } catch (Exception e) {
            dynamicScore = 5; // Default fallback score
            dynamicFeedback = aiEvaluation; // Use raw response if parsing fails
        }

        // 5. Persist the candidate's answer and AI evaluation
        InterviewAnswer answer = new InterviewAnswer();
        answer.setAnswerText(request.getAnswerText());
        answer.setInterviewQuestion(question);
        answer.setScore(dynamicScore);
        answer.setAiFeedback(dynamicFeedback);
        answerRepository.save(answer);

        // 6. Check if the interview should end (threshold: 5 questions)
        boolean isComplete = question.getInterviewSession().getQuestions().size() >= 5;

        String nextQuestionId = null;
        String nextQuestionText = null;

        if (isComplete) {
            // 7. Generate final feedback and close the session
            InterviewFeedback feedback = new InterviewFeedback();
            feedback.setInterviewSession(question.getInterviewSession());
            feedback.setTotalScore(dynamicScore * 10); // Placeholder calculation logic
            feedback.setStrengths("Candidate demonstrated depth in " + question.getTopic());
            feedback.setWeaknesses("Further exploration needed in complex scenarios.");
            feedbackRepository.save(feedback);

            // Mark session as ended
            question.getInterviewSession().setEndedAt(LocalDateTime.now());
            sessionRepository.save(question.getInterviewSession());
        } else {
            // 8. Generate and persist the next technical question
            nextQuestionText = chatModel.call("The candidate just answered: " + request.getAnswerText() +
                    ". Based on this, ask the next technical follow-up question for a " +
                    question.getInterviewSession().getDomain() + " role.");

            InterviewQuestion nextQuestion = new InterviewQuestion();
            nextQuestion.setQuestionText(nextQuestionText);
            nextQuestion.setInterviewSession(question.getInterviewSession());
            nextQuestion.setTopic(question.getTopic());
            nextQuestion.setDifficulty(question.getDifficulty());
            nextQuestion.setMaxScore(10);
            nextQuestion = questionRepository.save(nextQuestion);

            nextQuestionId = nextQuestion.getId().toString();
        }

        // 9. Return the consolidated response to the controller
        return new EvaluateAnswerResponse(
                dynamicScore,
                dynamicFeedback,
                isComplete,
                nextQuestionId,
                nextQuestionText
        );
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewReportResponse getInterviewReport(Long sessionId) {
        // 1. Fetch the session and handle errors
        InterviewSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found with ID: " + sessionId));

        // 2. Fetch the AI-generated feedback summary
        InterviewFeedback feedback = feedbackRepository.findByInterviewSessionId(sessionId)
                .orElse(new InterviewFeedback());

        InterviewReportResponse report = new InterviewReportResponse();
        report.setSessionId(session.getId());
        report.setTotalQuestions(session.getQuestions().size());

        // Combine Strengths and Weaknesses for the overall feedback string
        String summary = String.format("STRENGTHS:\n%s\n\nWEAKNESSES:\n%s",
                feedback.getStrengths(), feedback.getWeaknesses());
        report.setOverallFeedback(summary);

        // 3. Populate the 'details' list (Mapping Entities to DTOs)
        List<QuestionFeedback> detailsList = session.getQuestions().stream()
                .filter(q -> q.getAnswers() != null && !q.getAnswers().isEmpty()) // Only questions with answers
                .map(q -> {
                    QuestionFeedback qf = new QuestionFeedback();
                    qf.setQuestion(q.getQuestionText());

                    // Assuming one answer per question in this flow
                    InterviewAnswer ans = q.getAnswers().get(0);
                    qf.setAnswer(ans.getAnswerText());
                    qf.setScore(ans.getScore());
                    qf.setFeedback(ans.getAiFeedback());
                    return qf;
                })
                .collect(Collectors.toList());

        report.setDetails(detailsList);

        // 4. Calculate the real Average Score across all answers
        double average = session.getQuestions().stream()
                .flatMap(q -> q.getAnswers().stream())
                .mapToInt(InterviewAnswer::getScore)
                .average()
                .orElse(0.0);

        report.setAverageScore(Math.round(average * 10.0) / 10.0); // Rounded to 1 decimal place

        return report;
    }
}