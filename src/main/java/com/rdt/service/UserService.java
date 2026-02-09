package com.rdt.service;

import com.rdt.dto.LoginRequestDTO;
import com.rdt.dto.ResponseDto;
import com.rdt.dto.UserDTO;
import com.rdt.entity.User;

public interface UserService {
    ResponseDto registerUser(UserDTO user);
    boolean canUserGiveInterview(String userId);
    User login(LoginRequestDTO loginDto);
}