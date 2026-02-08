package com.rdt.controller;

import com.rdt.dto.*;
import com.rdt.service.InterviewService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/interview")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping("/start")
    public StartInterviewResponse startInterview(
            @RequestBody StartInterviewRequest request) {
        return interviewService.startInterview(request);
    }

    @PostMapping("/evaluate")
    public EvaluateAnswerResponse evaluateAnswer(
            @RequestBody EvaluateAnswerRequest request) {
        return interviewService.evaluateAnswer(request);
    }

    @GetMapping("/report/{sessionId}")
    public InterviewReportResponse getReport(
            @PathVariable String sessionId) {
        return interviewService.getInterviewReport(sessionId);
    }
}
