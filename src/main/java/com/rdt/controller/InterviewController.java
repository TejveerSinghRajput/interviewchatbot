package com.rdt.controller;

import com.rdt.dto.*;
import com.rdt.service.InterviewService;
import com.rdt.service.VoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/interview")
public class InterviewController {

    private final InterviewService interviewService;
    private final VoiceService voiceService;

    public InterviewController(InterviewService interviewService,
                               VoiceService voiceService) {
        this.interviewService = interviewService;
        this.voiceService = voiceService;
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

    @PostMapping(value = "/evaluate-voice", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public EvaluateAnswerResponse evaluateVoice(
            @RequestParam("audio") MultipartFile audio,
            @RequestParam("sessionId") String sessionId,
            @RequestParam("questionId") String questionId) {

        // 1. Transcribe audio to text
        String answerText = voiceService.transcribe(audio);

        // 2. Build request and call existing evaluation logic
        EvaluateAnswerRequest request = new EvaluateAnswerRequest();
        request.setSessionId(sessionId);
        request.setQuestionId(questionId);
        request.setAnswerText(answerText);

        return interviewService.evaluateAnswer(request);
    }
}