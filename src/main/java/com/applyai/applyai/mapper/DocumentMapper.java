package com.applyai.applyai.mapper;

import org.springframework.stereotype.Component;

import com.applyai.applyai.dto.response.DocumentResponse;
import com.applyai.applyai.entity.Document;

@Component
public class DocumentMapper {

    public DocumentResponse toResponse(Document document){
        return new DocumentResponse(
            document.getId(),
            document.getFileName(),
            document.getDocumentType(),
            document.getFileFormat(),
            document.getFileSize(),
            document.getCreatedAt()
        );
    }

}
