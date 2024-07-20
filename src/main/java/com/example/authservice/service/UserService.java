package com.example.authservice.service;


import com.example.authservice.dto.RegistrationDto;
import com.example.authservice.model.UserEntity;
import org.apache.catalina.User;

public interface UserService {
    UserEntity saveUser(RegistrationDto registrationDTO);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);
}
