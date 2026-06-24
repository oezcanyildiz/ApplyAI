package com.applyai.applyai.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateApplicationRequest {


    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    private String contactPerson;

    private String jobPostingUrl;

    @NotBlank(message = "Job posting text is required")
    private String jobPostingText;

    private Long resumeDocumentId;

    // Optional - User's eigene Anschreiben-Vorlage als Text
    private String coverLetterTemplate;
}
