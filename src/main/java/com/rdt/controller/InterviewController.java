package com.rdt.controller;

import com.rdt.dto.*;
import com.rdt.service.InterviewService;
import com.rdt.service.UserService;
import com.rdt.service.VoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/interview")
@Tag(name = "Interview Management Engine", description = "Endpoints for starting sessions and evaluating AI responses")
public class InterviewController {

    private final InterviewService interviewService;
    private final VoiceService voiceService;
    private final UserService userService;

    public InterviewController(InterviewService interviewService,
                               VoiceService voiceService,
                               UserService userService) {
        this.interviewService = interviewService;
        this.voiceService = voiceService;
        this.userService = userService;
    }

    @Operation(summary = "Start Interview Session", description = "Validates user registration and enforces a limit of 5 interviews per day.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Session started successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "429", description = "Daily limit of 5 interviews reached")
    })
    @PostMapping("/start")
    public ResponseEntity<?> startInterview(@Valid @RequestBody StartInterviewRequest request) {
        // 1. Check limit using Email instead of ID
        if (!userService.canUserGiveInterview(request.getEmailId())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Daily limit reached for " + request.getEmailId() + ". Try again tomorrow.");
        }

        // 2. The Service will now use the email to fetch user details and start session
        StartInterviewResponse response = interviewService.startInterview(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Evaluate Text Answer", description = "Sends user's text to Phi-3 for scoring and technical feedback.")
    @PostMapping("/evaluate")
    public ResponseEntity<EvaluateAnswerResponse> evaluateAnswer(@Valid @RequestBody EvaluateAnswerRequest request) {
        EvaluateAnswerResponse response = interviewService.evaluateAnswer(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Generate Final Report", description = "Aggregates all 5 questions and answers into a PDF/JSON summary of strengths and weaknesses.")
    @GetMapping("/report/{sessionId}")
    public ResponseEntity<InterviewReportResponse> getReport(@PathVariable Long sessionId) { // Changed String to Long for consistency
        InterviewReportResponse response = interviewService.getInterviewReport(sessionId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Process Voice Answer", description = "Uploads a .wav file to Whisper for transcription and then evaluates the text.")
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