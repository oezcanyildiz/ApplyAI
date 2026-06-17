package com.applyai.applyai.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.applyai.applyai.dto.response.DocumentResponse;
import com.applyai.applyai.enums.DocumentType;

public interface IDocumentService {

    public DocumentResponse uploadDocument(MultipartFile file, DocumentType documentType); 

    public List <DocumentResponse> getAllDocuments();

    public DocumentResponse getDocument(Long id);

    public Resource downloadDocument(Long id);

    public void deleteDocument(Long id);
    

}
