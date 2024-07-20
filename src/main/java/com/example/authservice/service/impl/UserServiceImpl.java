package com.example.authservice.service.impl;



import com.example.authservice.dto.RegistrationDto;
import com.example.authservice.model.Role;
import com.example.authservice.model.UserEntity;
import com.example.authservice.repository.RoleRepository;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserEntity saveUser(RegistrationDto registrationDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registrationDTO.getUsername());
        userEntity.setEmail(registrationDTO.getEmail());
        userEntity.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        Role role = roleRepository.findByName("User");
        userEntity.setRoles(Collections.singletonList(role));
        userRepository.save(userEntity);
        return userEntity;
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}