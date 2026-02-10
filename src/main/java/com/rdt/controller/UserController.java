package com.rdt.controller;

import com.rdt.dto.LoginRequestDTO;
import com.rdt.dto.ResponseDto;
import com.rdt.dto.UserDTO;
import com.rdt.entity.User;
import com.rdt.service.InterviewService;
import com.rdt.service.UserService;
import com.rdt.service.VoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Registration & Limits")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register user with unique email")
    public ResponseEntity<ResponseDto> register(@RequestBody UserDTO user) {
        log.info("Inside register method");
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @GetMapping("/checklimit/{emailId}")
    @Operation(summary = "Check if user has reached the daily limit of 5 interviews")
    public ResponseEntity<String> checkLimit(@PathVariable String emailId) {
        if (userService.canUserGiveInterview(emailId)) {
            return ResponseEntity.ok("User is eligible for interview.");
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                                 .body("Daily limit reached! Please try again tomorrow.");
        }
    }

    @Operation(summary = "User Login", description = "Authenticates user and verifies BCrypt hashed password.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginDto) {
        try {
            User user = userService.login(loginDto);
            // In a real app, you'd return a JWT token here. For now, we return user info.
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}