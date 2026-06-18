package com.applyai.applyai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.applyai.applyai.exception.BadRequestException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class AiService {

    @Value("${claude.api-key}")
    private String apiKey;

    @Value("${claude.model}")
    private String model;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public AiService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.anthropic.com")
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public String generateApplicationDocuments(
            String resumeText,
            String jobPostingText,
            String coverLetterTemplate) {

        log.info("Sending request to Claude API...");

        // Prompt bauen
        String prompt = buildPrompt(resumeText, jobPostingText, coverLetterTemplate);

        // Request Body bauen
        String requestBody = """
                {
                    "model": "%s",
                    "max_tokens": 4000,
                    "messages": [
                        {
                            "role": "user",
                            "content": %s
                        }
                    ]
                }
                """.formatted(model, toJson(prompt));

        try {
            // Claude API aufrufen
            String response = restClient.post()
                    .uri("/v1/messages")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header("x-api-key", apiKey)
                    .header("anthropic-version", "2023-06-01")
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            // Response parsen
            JsonNode jsonNode = objectMapper.readTree(response);
            String result = jsonNode
                    .get("content")
                    .get(0)
                    .get("text")
                    .asText();

            log.info("Claude API responded successfully");
            return result;

        } catch (Exception e) {
            log.error("Claude API call failed: {}", e.getMessage());
            throw new BadRequestException("AI service is currently unavailable!");
        }
    }

    private String buildPrompt(
            String resumeText,
            String jobPostingText,
            String coverLetterTemplate) {

        return """
                Du bist ein professioneller Bewerbungsexperte.
                
                Hier ist der aktuelle Lebenslauf des Bewerbers:
                ---
                %s
                ---
                
                Hier ist die Stellenanzeige:
                ---
                %s
                ---
                
                %s
                
                Bitte erstelle:
                1. OPTIMIERTER_LEBENSLAUF: Den Lebenslauf optimiert für diese Stelle
                2. ANSCHREIBEN: Ein maßgeschneidertes Anschreiben
                
                Formatiere die Antwort GENAU so:
                [OPTIMIERTER_LEBENSLAUF]
                (Lebenslauf hier)
                [/OPTIMIERTER_LEBENSLAUF]
                
                [ANSCHREIBEN]
                (Anschreiben hier)
                [/ANSCHREIBEN]
                """.formatted(
                resumeText,
                jobPostingText,
                coverLetterTemplate != null ?
                    "Anschreiben-Vorlage des Bewerbers (Stil beibehalten):\n---\n" + coverLetterTemplate + "\n---" :
                    "Erstelle ein professionelles Anschreiben basierend auf dem Lebenslauf."
        );
    }

    private String toJson(String text) {
        try {
            return objectMapper.writeValueAsString(text);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize prompt");
        }
    }
}