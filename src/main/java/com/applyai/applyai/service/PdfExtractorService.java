package com.applyai.applyai.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import com.applyai.applyai.exception.BadRequestException;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class PdfExtractorService {

    public String extractTextFromPdf(String filePath) {
        log.info("Extracting text from PDF: {}", filePath);
        
        try (PDDocument document = Loader.loadPDF(new File(filePath))) {
            
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            
            if (text == null || text.isBlank()) {
                throw new BadRequestException("PDF contains no readable text!");
            }
            
            log.info("Successfully extracted {} characters from PDF", text.length());
            return text.trim();
            
        } catch (IOException e) {
            log.error("Failed to extract text from PDF: {}", filePath, e);
            throw new BadRequestException("Could not read PDF file!");
        }
    }
}