package com.rdt.controller;

import com.rdt.dto.*;
import com.rdt.service.InterviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/interview")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping("/start")
    public ResponseEntity<StartInterviewResponse> startInterview(@Valid @RequestBody StartInterviewRequest request) {
        StartInterviewResponse response = interviewService.startInterview(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/evaluate")
    public ResponseEntity<EvaluateAnswerResponse> evaluateAnswer(@Valid @RequestBody EvaluateAnswerRequest request) {
        EvaluateAnswerResponse response = interviewService.evaluateAnswer(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/report/{sessionId}")
    public ResponseEntity<InterviewReportResponse> getReport(@PathVariable Long sessionId) { // Changed String to Long for consistency
        InterviewReportResponse response = interviewService.getInterviewReport(sessionId);
        return ResponseEntity.ok(response);
    }
}