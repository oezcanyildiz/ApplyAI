package com.applyai.applyai.dto.request;

import java.time.LocalDate;

import com.applyai.applyai.enums.ApplicationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateApplicationRequest {
    private String contactPerson;        // Ansprechpartner nachholen
    private ApplicationStatus status;    // Status ändern
    private LocalDate interviewDate;     // Gesprächstermin
}