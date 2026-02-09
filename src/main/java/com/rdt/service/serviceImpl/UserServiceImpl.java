package com.rdt.service.serviceImpl;

import com.rdt.constant.Constants;
import com.rdt.dto.LoginRequestDTO;
import com.rdt.dto.ResponseDto;
import com.rdt.dto.UserDTO;
import com.rdt.entity.User;
import com.rdt.repository.UserRepository;
import com.rdt.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ResponseDto registerUser(UserDTO userDto) {
        log.info("Registering user: {}", userDto.getEmail());
        ResponseDto response = new ResponseDto();
        User user = new User();
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            log.warn("Registration failed: {} already exists", userDto.getEmail());
            response.setMsg(Constants.FAILURE);
            response.setMessage("User already exist!");
            response.setMsgId(Constants.FAILURE_MSG_ID);
        }
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setCreatedAt(LocalDateTime.now());
        user.setRole("USER"); // Default role

        // HASHING HAPPENS HERE
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(encodedPassword);
        log.debug("Password successfully hashed for user: {}", userDto.getEmail());
        userRepository.save(user);
        response.setMsg(Constants.SUCCESS);
        response.setMessage("User registered successfully !");
        response.setMsgId(Constants.SUCCESS_MSG_ID);
        return response;
    }

    @Override
    public boolean canUserGiveInterview(String email) {
        log.info("Inside canUserGiveInterview method :{}",email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        long count = userRepository.countInterviewsToday(user.getId(), startOfDay);
        log.debug("canUserGiveInterview count return : {}", count);
        return count < 5;
    }

    @Override
    public User login(LoginRequestDTO loginDto) {
        log.info("Login attempt for email: {}", loginDto.getEmail());

        // 1. Find user by email
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: User {} not found", loginDto.getEmail());
                    return new RuntimeException("Invalid email or password");
                });

        // 2. Verify hashed password
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            log.warn("Login failed: Incorrect password for user {}", loginDto.getEmail());
            throw new RuntimeException("Invalid email or password");
        }

        log.info("User {} logged in successfully", loginDto.getEmail());
        return user;
    }
}