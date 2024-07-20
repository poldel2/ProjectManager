package com.example.authservice.controller;

import com.example.authservice.config.JWTGenerator;
import com.example.authservice.dto.AuthResponseDto;
import com.example.authservice.kafka.KafkaProducer;
import com.example.authservice.service.KafkaProducerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.authservice.dto.LoginDto;
import com.example.authservice.dto.RegistrationDto;
import com.example.authservice.model.UserEntity;
import com.example.authservice.service.AuthService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthController {
    private final AuthService authService;
    private JWTGenerator jwtGenerator;
    @Autowired
    private final KafkaProducerService kafkaProducerService;

    public AuthController(AuthService authService, JWTGenerator jwtGenerator, KafkaProducerService kafkaProducerService) {
        this.authService = authService;
        this.jwtGenerator = jwtGenerator;
        this.kafkaProducerService = kafkaProducerService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

    @PostMapping("/login/login")
    public String login(@ModelAttribute("loginDto") LoginDto loginDto, HttpServletRequest request,
                                                 HttpServletResponse response) throws Exception {
        jwtGenerator.clearToken(response);

        Authentication authentication = authService.login(loginDto);
        String token  = jwtGenerator.generateToken(authentication);
        jwtGenerator.saveToken(token, request, response);
        kafkaProducerService.sendMessage(token);
        response.addHeader("Authorization", "Bearer " + token);
        return "redirect:/main";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserEntity());
        return "register";
    }

    @PostMapping("/register/save")
    public String processRegistration(@Valid @ModelAttribute("user") UserEntity user, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        authService.register(user);
        kafkaProducerService.sendMessage("New user registered: " + user.getUsername());
        return "redirect:/login";
    }
    @GetMapping("/user")
    public ResponseEntity<UserEntity> getCurrentUser(Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return ResponseEntity.ok(user);
    }
}
