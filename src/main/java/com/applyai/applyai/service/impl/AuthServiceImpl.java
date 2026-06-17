package com.applyai.applyai.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.applyai.applyai.dto.request.LoginRequest;
import com.applyai.applyai.dto.request.RegisterRequest;
import com.applyai.applyai.dto.response.AuthResponse;
import com.applyai.applyai.dto.response.RegisterResponse;
import com.applyai.applyai.entity.User;
import com.applyai.applyai.enums.Role;
import com.applyai.applyai.exception.ConflictException;
import com.applyai.applyai.exception.UnauthorizedException;
import com.applyai.applyai.repository.UserRepository;
import com.applyai.applyai.security.JwtUtil;
import com.applyai.applyai.service.IAuthService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthServiceImpl implements IAuthService {

  
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl( UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public RegisterResponse register(RegisterRequest request) {

        log.info("Register attempt for email: {}", request.getEmail());
        
        if(userRepository.existsByEmail(request.getEmail())){
            log.warn("Registration failed - email already exists: {}", request.getEmail());
            throw new ConflictException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(Role.USER); // ← IMMER hartkodiert im Service, NICHT vom Client
        User savedUser =userRepository.save(user);

        log.info("User registered successfully: id={}, email={}", savedUser.getId(), savedUser.getEmail());

        return new RegisterResponse(
            "Registration successful. Please log in.",
            savedUser.getEmail(),
            savedUser.getId());
        
    }

    @Transactional(readOnly = true)
    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed - email not found: {}", request.getEmail());
                    return new UnauthorizedException("Invalid email or password");
                });
                
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Login failed - wrong password for email: {}", request.getEmail());
            throw new UnauthorizedException("Invalid email or password");
        }
        
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId());
        log.info("User logged in successfully: id={}", user.getId());
        return new AuthResponse(token, user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }
}
