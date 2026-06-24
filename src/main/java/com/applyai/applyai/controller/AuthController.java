package com.applyai.applyai.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.applyai.applyai.dto.request.LoginRequest;
import com.applyai.applyai.dto.request.RegisterRequest;
import com.applyai.applyai.dto.response.AuthResponse;
import com.applyai.applyai.dto.response.RegisterResponse;
import com.applyai.applyai.service.IAuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> userRegister(@Valid @RequestBody RegisterRequest  request) {
        RegisterResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED); // 201
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> postLogin(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    

}
