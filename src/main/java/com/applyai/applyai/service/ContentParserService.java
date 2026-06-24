package com.applyai.applyai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.applyai.applyai.exception.BadRequestException;

@Slf4j
@Service
public class ContentParserService {

    private static final String RESUME_START = "[OPTIMIERTER_LEBENSLAUF]";
    private static final String RESUME_END = "[/OPTIMIERTER_LEBENSLAUF]";
    private static final String COVER_LETTER_START = "[ANSCHREIBEN]";
    private static final String COVER_LETTER_END = "[/ANSCHREIBEN]";

    public String extractResume(String generatedContent) {
        return extractBetween(generatedContent, RESUME_START, RESUME_END);
    }

    public String extractCoverLetter(String generatedContent) {
        return extractBetween(generatedContent, COVER_LETTER_START, COVER_LETTER_END);
    }

    private String extractBetween(String text, String startTag, String endTag) {
        int startIndex = text.indexOf(startTag);
        int endIndex = text.indexOf(endTag);

        if (startIndex == -1 || endIndex == -1) {
            log.error("Could not find tags {} or {} in generated content", startTag, endTag);
            throw new BadRequestException("AI response format is invalid!");
        }

        startIndex += startTag.length();
        return text.substring(startIndex, endIndex).trim();
    }
}