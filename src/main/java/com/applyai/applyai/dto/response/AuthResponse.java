package com.applyai.applyai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private Long id;    
    private String firstName;
    private String lastName;    
    private String email;

}
