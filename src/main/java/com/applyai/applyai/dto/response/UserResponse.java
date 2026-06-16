package com.applyai.applyai.dto.response;

import com.applyai.applyai.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;


import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Role role;
    private LocalDateTime createdAt;
}