package com.applyai.applyai.service.impl;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.applyai.applyai.dto.request.CreateApplicationRequest;
import com.applyai.applyai.dto.request.UpdateApplicationRequest;
import com.applyai.applyai.dto.response.ApplicationResponse;
import com.applyai.applyai.entity.Application;
import com.applyai.applyai.entity.Document;
import com.applyai.applyai.entity.User;
import com.applyai.applyai.enums.ApplicationStatus;
import com.applyai.applyai.enums.DocumentType;
import com.applyai.applyai.enums.FileFormat;
import com.applyai.applyai.exception.BadRequestException;
import com.applyai.applyai.exception.NotFoundException;
import com.applyai.applyai.exception.TooManyRequestsException;
import com.applyai.applyai.mapper.ApplicationMapper;
import com.applyai.applyai.repository.ApplicationRepository;
import com.applyai.applyai.repository.DocumentRepository;
import com.applyai.applyai.repository.UserRepository;
import com.applyai.applyai.security.SecurityUtil;
import com.applyai.applyai.service.AiService;
import com.applyai.applyai.service.ContentParserService;
import com.applyai.applyai.service.DocxGeneratorService;
import com.applyai.applyai.service.GeneratedDocument;
import com.applyai.applyai.service.IApplicationService;
import com.applyai.applyai.service.PdfExtractorService;
import com.applyai.applyai.service.ProgressNotifierService;
import com.applyai.applyai.service.RateLimiterService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ApplicationServiceImpl implements IApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final ApplicationMapper applicationMapper;
    private final DocumentRepository documentRepository;
    private final PdfExtractorService pdfExtractorService;
    private final AiService aiService;
    private final ContentParserService contentParserService;
    private final DocxGeneratorService docxGeneratorService;
    private final ProgressNotifierService progressNotifierService;
    private final RateLimiterService rateLimiterService;
    
    public ApplicationServiceImpl(
            ApplicationRepository applicationRepository,
            UserRepository userRepository,
            ApplicationMapper applicationMapper,
            DocumentRepository documentRepository,
            PdfExtractorService pdfExtractorService,
            AiService aiService,
            ContentParserService contentParserService,
            DocxGeneratorService docxGeneratorService,
            ProgressNotifierService progressNotifierService,
            RateLimiterService rateLimiterService
        
        ) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.applicationMapper = applicationMapper;
        this.documentRepository = documentRepository;
        this.pdfExtractorService = pdfExtractorService;
        this.aiService = aiService;
        this.contentParserService=contentParserService;
        this.docxGeneratorService=docxGeneratorService;
        this.progressNotifierService=progressNotifierService;
        this.rateLimiterService=rateLimiterService;
    }

    @Transactional
    @Override
    public ApplicationResponse createApplication(CreateApplicationRequest request) {

        // 1. Eingeloggten User holen
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found in DB for userId: {}", userId);
                    return new NotFoundException("User not found");
            });

        // 2. Resume Document holen (falls angegeben)
        Document resumeDocument = null;
        if (request.getResumeDocumentId() != null) {
            resumeDocument = documentRepository.findByIdAndUserId(
                    request.getResumeDocumentId(), userId)
                    .orElseThrow(() -> {
                        log.warn("Document not found in DB for userId: {}", userId);
                        return new NotFoundException("Document not found");
                        }
                    );
        }

        // 3. Application Entity manuell befüllen
        Application application = new Application();
        application.setCompanyName(request.getCompanyName());
        application.setJobTitle(request.getJobTitle());
        application.setContactPerson(request.getContactPerson());
        application.setJobPostingUrl(request.getJobPostingUrl());
        application.setJobPostingText(request.getJobPostingText());
        application.setCoverLetterTemplate(request.getCoverLetterTemplate());
        application.setResumeDocument(resumeDocument);
        application.setUser(user);
        application.setStatus(ApplicationStatus.DRAFT); // immer DRAFT am Anfang

        // 4. Speichern
        Application savedApplication = applicationRepository.save(application);

        // 5. Response zurückgeben
        return applicationMapper.toResponse(savedApplication);
    }

    @Transactional
    @Override
    public ApplicationResponse updateApplication(Long id, UpdateApplicationRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();

        // 1. Application holen
        Application application = findApplicationByIdAndUserId(id, userId);

        // 3. Felder aktualisieren (nur wenn sie im Request gesetzt sind)
        if (request.getContactPerson() != null) {
            application.setContactPerson(request.getContactPerson());
        }
        if (request.getStatus() != null) {
            application.setStatus(request.getStatus());
        }
        if (request.getInterviewDate() != null) {
            application.setInterviewDate(request.getInterviewDate());
        }

        // 4. Speichern
        Application updatedApplication = applicationRepository.save(application);

        // 5. Response zurückgeben
        return applicationMapper.toResponse(updatedApplication);

    }

    @Transactional(readOnly = true)
    @Override
    public ApplicationResponse getApplicationById(Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        Application application = findApplicationByIdAndUserId(id, userId);
        return applicationMapper.toResponse(application);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ApplicationResponse> getAllApplications() {
        Long userId = SecurityUtil.getCurrentUserId();
        log.info("Fetching all applications for userId: {}", userId);
        return applicationRepository.findByUserId(userId)
                .stream()
                .map(applicationMapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public void deleteApplication(Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        Application application = findApplicationByIdAndUserId(id, userId);
        applicationRepository.delete(application);
    }

    @Async
    @Transactional
    public void generateApplication(Long id, Long userId) {

        if (!rateLimiterService.tryConsume(userId)) {
            progressNotifierService.sendProgress(id, "FAILED", "Limit erreicht. Bitte später erneut versuchen.");
            throw new TooManyRequestsException("Rate limit exceeded. Maximum 5 generations per hour.");
        }

        Application application = findApplicationByIdAndUserId(id, userId);

        progressNotifierService.sendProgress(id, "STARTED", "Generierung gestartet...");

        if (application.getResumeDocument() == null) {
            progressNotifierService.sendProgress(id, "FAILED", "Kein Lebenslauf hinterlegt!");
            throw new BadRequestException("No resume document attached to this application!");
        }

        progressNotifierService.sendProgress(id, "EXTRACTING", "Lese Lebenslauf...");
        String resumeText = pdfExtractorService.extractTextFromPdf(
                application.getResumeDocument().getFilePath());

        progressNotifierService.sendProgress(id, "ASKING_AI", "Frage KI nach Optimierung...");
        String aiResult;
        try {
            aiResult = aiService.generateApplicationDocuments(
                    resumeText,
                    application.getJobPostingText(),
                    application.getCoverLetterTemplate());
        } catch (Exception e) {
            progressNotifierService.sendProgress(id, "FAILED", "KI-Anfrage fehlgeschlagen!");
            throw e;
        }

        application.setGeneratedContent(aiResult);

        progressNotifierService.sendProgress(id, "PARSING", "Verarbeite KI-Antwort...");
        String resumeContent = contentParserService.extractResume(aiResult);
        String coverLetterContent = contentParserService.extractCoverLetter(aiResult);

        progressNotifierService.sendProgress(id, "GENERATING_DOCX", "Erstelle Dokumente...");
     
        GeneratedDocument resumeDocxPath = docxGeneratorService.generateDocx(
                resumeContent, "Lebenslauf_" + application.getCompanyName(), userId);
        GeneratedDocument coverLetterDocxPath = docxGeneratorService.generateDocx(
                coverLetterContent, "Anschreiben_" + application.getCompanyName(), userId);
        
  
        Document generatedResume = new Document();
        generatedResume.setFileName("Lebenslauf_" + application.getCompanyName() + ".docx");
        generatedResume.setFilePath(resumeDocxPath.filePath());
        generatedResume.setFileSize(resumeDocxPath.fileSize());
        generatedResume.setDocumentType(DocumentType.GENERATED_RESUME);
        generatedResume.setFileFormat(FileFormat.WORD);
        generatedResume.setUser(application.getUser());
        generatedResume.setApplication(application);
        documentRepository.save(generatedResume);

        Document generatedCoverLetter = new Document();
        generatedCoverLetter.setFileName("Anschreiben_" + application.getCompanyName() + ".docx");
        generatedCoverLetter.setFilePath(coverLetterDocxPath.filePath());
        generatedCoverLetter.setFileSize(coverLetterDocxPath.fileSize());
        generatedCoverLetter.setDocumentType(DocumentType.GENERATED_COVER_LETTER);
        generatedCoverLetter.setFileFormat(FileFormat.WORD);
        generatedCoverLetter.setUser(application.getUser());
        generatedCoverLetter.setApplication(application);
        documentRepository.save(generatedCoverLetter);

        application.setStatus(ApplicationStatus.APPLIED);
        applicationRepository.save(application);

        log.info("AI generation completed for applicationId: {}", id);
        progressNotifierService.sendProgress(id, "DONE", "Fertig! Dokumente sind bereit.");
    }


    // ↓ Hilfsmethode INNERHALB der Klasse!
    private Application findApplicationByIdAndUserId(Long id, Long userId) {
        return applicationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> {
                    log.warn("Application not found for applicationId: {}, userId: {}", id, userId);
                    return new NotFoundException("Application not found");
                });

            }
}
