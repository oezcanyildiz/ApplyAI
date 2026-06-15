package com.applyai.applyai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.applyai.applyai.entity.Document;
import com.applyai.applyai.enums.DocumentType;


public interface DocumentRepository extends JpaRepository<Document, Long> {

    List <Document> findByUserId(Long userId);

    List<Document> findByApplicationId(Long applicationId);

    List<Document> findByApplicationIdAndDocumentType(Long applicationId, DocumentType documentType);

}
