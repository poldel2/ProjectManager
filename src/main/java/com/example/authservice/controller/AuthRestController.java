package com.example.authservice.controller;

import com.example.authservice.config.JWTGenerator;
import com.example.authservice.dto.AuthResponseDto;
import com.example.authservice.dto.LoginDto;
import com.example.authservice.dto.RegistrationDto;
import com.example.authservice.kafka.KafkaProducer;
import com.example.authservice.model.UserEntity;
import com.example.authservice.service.AuthService;
import com.example.authservice.service.KafkaProducerService;
import com.example.authservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthRestController {
    private final JWTGenerator jwtGenerator;
    private final UserService userService;
    private final AuthService authService;
    private final KafkaProducerService kafkaProducerService;

    public AuthRestController(JWTGenerator jwtGenerator, UserService userService, AuthService authService, KafkaProducerService kafkaProducerService) {
        this.jwtGenerator = jwtGenerator;
        this.userService = userService;
        this.authService = authService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) throws Exception {
        //jwtGenerator.clearToken(response);

        Authentication authentication = authService.login(loginDto);
        String token  = jwtGenerator.generateToken(authentication);
        //jwtGenerator.saveToken(token, request, response);
        kafkaProducerService.sendMessage(token);
        //response.addHeader("Authorization", "Bearer " + token);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserEntity> register(@RequestBody RegistrationDto registrationDto) {
        UserEntity user = userService.saveUser(registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getAuthenticatedUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(user);
    }
}


