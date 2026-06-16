package com.applyai.applyai.mapper;


import org.springframework.stereotype.Component;

import com.applyai.applyai.dto.response.ApplicationResponse;
import com.applyai.applyai.entity.Application;

@Component
public class ApplicationMapper {
    public ApplicationResponse toResponse(Application application) {
        return new ApplicationResponse(
                application.getId(),
                application.getCompanyName(),
                application.getJobTitle(),
                application.getContactPerson(),
                application.getJobPostingUrl(),
                application.getJobPostingText(),
                application.getCoverLetterTemplate(),
                application.getResumeDocument() != null 
                    ? application.getResumeDocument().getId() 
                    : null,
                application.getStatus(),
                application.getCreatedAt(),
                application.getUpdatedAt()
        );
    }
}