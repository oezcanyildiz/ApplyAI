package com.applyai.applyai.dto.response;

import com.applyai.applyai.enums.DocumentType;
import com.applyai.applyai.enums.FileFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DocumentResponse {
    private Long id;
    private String fileName;
    private DocumentType documentType;
    private FileFormat fileFormat;
    private Long fileSize;
    private LocalDateTime createdAt;
}