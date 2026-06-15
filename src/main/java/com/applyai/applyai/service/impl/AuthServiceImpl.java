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

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
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

        return new RegisterResponse(
            "Registration successful. Please log in.",
            savedUser.getEmail(),
            savedUser.getId());
        
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId());

        return new AuthResponse(token, user.getId(), user.getFirstName() , user.getLastName(), user.getEmail());
    }


}
