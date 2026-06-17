package com.applyai.applyai.controller;

import org.springframework.http.HttpHeaders;
import java.util.List;


import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.applyai.applyai.dto.response.DocumentResponse;
import com.applyai.applyai.enums.DocumentType;
import com.applyai.applyai.service.IDocumentService;



@RestController
@RequestMapping("/api/documents")
public class DocumentController {
       private final IDocumentService documentService;

    public DocumentController(IDocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public ResponseEntity <DocumentResponse> uploadDocument(
        @RequestParam("file") MultipartFile file,
        @RequestParam("documentType") DocumentType documentType){
        return new ResponseEntity<>(
            documentService.uploadDocument(file, documentType),HttpStatus.CREATED
        );
    }

    @GetMapping()
    public ResponseEntity <List<DocumentResponse>> getAllDocuments(){
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @GetMapping("/{id}")
    public ResponseEntity <DocumentResponse> getDocument(@PathVariable Long id){
        return ResponseEntity.ok(documentService.getDocument(id));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) {
        Resource resource = documentService.downloadDocument(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <Void> deleteDocument(@PathVariable Long id){
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

}
