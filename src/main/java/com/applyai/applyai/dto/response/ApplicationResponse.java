package com.applyai.applyai.dto.response;

import com.applyai.applyai.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApplicationResponse {
    private Long id;
    private String companyName;
    private String jobTitle;
    private String contactPerson;
    private String jobPostingUrl;
    private String jobPostingText;
    private String coverLetterTemplate;
    private Long resumeDocumentId;
    private ApplicationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String generatedContent; 
}