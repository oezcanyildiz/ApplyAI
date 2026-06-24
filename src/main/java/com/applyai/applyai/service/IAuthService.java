package com.applyai.applyai.service;

import com.applyai.applyai.dto.request.LoginRequest;
import com.applyai.applyai.dto.request.RegisterRequest;
import com.applyai.applyai.dto.response.AuthResponse;
import com.applyai.applyai.dto.response.RegisterResponse;

public interface IAuthService {

    public RegisterResponse register(RegisterRequest request);
    public AuthResponse login(LoginRequest request);
    
}
