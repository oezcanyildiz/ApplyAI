package com.applyai.applyai.service.impl;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.applyai.applyai.dto.response.DocumentResponse;
import com.applyai.applyai.entity.Document;
import com.applyai.applyai.entity.User;
import com.applyai.applyai.enums.DocumentType;
import com.applyai.applyai.enums.FileFormat;
import com.applyai.applyai.exception.BadRequestException;
import com.applyai.applyai.exception.NotFoundException;
import com.applyai.applyai.mapper.DocumentMapper;
import com.applyai.applyai.repository.DocumentRepository;
import com.applyai.applyai.repository.UserRepository;
import com.applyai.applyai.security.SecurityUtil;
import com.applyai.applyai.service.FileStorageService;
import com.applyai.applyai.service.IDocumentService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Service
public class DocumentServiceImpl implements IDocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final DocumentMapper documentMapper;
    private final FileStorageService fileStorageService;

    public DocumentServiceImpl(DocumentRepository documentRepository, 
            UserRepository userRepository,
            DocumentMapper documentMapper,
            FileStorageService fileStorageService){
        this.documentRepository = documentRepository;
        this.userRepository=userRepository;
        this.documentMapper=documentMapper;
        this.fileStorageService=fileStorageService;
    }

    @Transactional
    @Override
    public DocumentResponse uploadDocument(MultipartFile file, DocumentType documentType) {
        Long userId =SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                log.warn("User not found {}", userId);
                return new NotFoundException("User not found");
                
            });

        // 2. Dateiformat prüfen
        String contentType = file.getContentType();
        FileFormat fileFormat;
        if (contentType != null && contentType.equals("application/pdf")) {
            fileFormat = FileFormat.PDF;
        } else if (contentType != null && (
                contentType.equals("application/msword") ||
                contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
            fileFormat = FileFormat.WORD;
        } else {
            throw new BadRequestException("Only PDF and Word files are allowed!");
        }

        // 3. Datei speichern
        String filePath = fileStorageService.saveFile(file, userId);

        // 4. Document Entity erstellen
        Document document = new Document();
        document.setFileName(file.getOriginalFilename());
        document.setFilePath(filePath);
        document.setFileSize(file.getSize());
        document.setFileFormat(fileFormat);
        document.setDocumentType(documentType);
        document.setUser(user);

        // 5. In DB speichern
        Document savedDocument = documentRepository.save(document);
        log.info("Document uploaded: id={}, userId={}, fileName={}", 
            savedDocument.getId(), userId, file.getOriginalFilename());

        // 6. Response zurückgeben
        return documentMapper.toResponse(savedDocument);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DocumentResponse> getAllDocuments() {
        Long userId = SecurityUtil.getCurrentUserId();
        log.info("User fetsching all Documents {}" ,userId);
        return documentRepository.findByUserId(userId)
            .stream()
            .map(documentMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public DocumentResponse getDocument(Long id) {
        Long userId=SecurityUtil.getCurrentUserId();
        Document document=getDocumentfromDB(id, userId);
        return documentMapper.toResponse(document);
    }

    @Transactional(readOnly = true)
    @Override
    public Resource downloadDocument(Long id) {
        Long userId= SecurityUtil.getCurrentUserId();
        Document document=getDocumentfromDB(id, userId);
        return fileStorageService.loadFile(document.getFilePath());
    }

    @Transactional
    @Override
    public void deleteDocument(Long id) {
        Long userId= SecurityUtil.getCurrentUserId();
        Document document=getDocumentfromDB(id, userId);
        fileStorageService.deleteFile(document.getFilePath());
        documentRepository.delete(document);
        log.info("Document deleted: id={}, userId={}", id, userId);
    }

    // ↓ Hilfsmethode INNERHALB der Klasse!
    private Document getDocumentfromDB(Long id, Long userId){
        return documentRepository.findByIdAndUserId(id, userId)
            .orElseThrow(()-> {
                log.warn("Document {} ist not for this User {}", id , userId );
                return new NotFoundException("Document not found");

            });

        }

}
