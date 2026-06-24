package com.applyai.applyai.entity;

import java.time.LocalDate;

import com.applyai.applyai.enums.ApplicationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "applications")
public class Application extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String jobTitle;

    private String contactPerson;

    private String jobPostingUrl;

    private LocalDate interviewDate;
    
    @Column(columnDefinition = "TEXT")
    private String jobPostingText;

    // Optional - User's eigene Anschreiben-Vorlage als Text
    @Column(columnDefinition = "TEXT")
    private String coverLetterTemplate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "resume_document_id")
    private Document resumeDocument; // welches Resume wurde verwendet

    @Column(columnDefinition = "TEXT")
    private String generatedContent; // KI-Ergebnis temporär speichern

}
