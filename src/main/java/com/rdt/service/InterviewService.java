package com.rdt.service;

import com.rdt.dto.*;

public interface InterviewService {

    StartInterviewResponse startInterview(StartInterviewRequest request);

    EvaluateAnswerResponse evaluateAnswer(EvaluateAnswerRequest request);

    InterviewReportResponse getInterviewReport(Long sessionId);
}