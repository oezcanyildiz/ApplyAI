package com.applyai.applyai.security;

public record AuthenticatedUser(Long userId, String email, String role) {}