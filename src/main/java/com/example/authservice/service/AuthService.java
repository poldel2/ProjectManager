package com.example.authservice.service;

import com.example.authservice.dto.LoginDto;
import com.example.authservice.dto.RegistrationDto;
import com.example.authservice.model.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

public interface AuthService {
    Authentication login(LoginDto loginDto) throws Exception;
    UserEntity register(RegistrationDto registrationDto) throws Exception;
    UserEntity register(UserEntity user) throws Exception;
}
