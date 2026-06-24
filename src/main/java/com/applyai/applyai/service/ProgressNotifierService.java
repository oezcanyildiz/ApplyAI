package com.applyai.applyai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProgressNotifierService {

    private final SimpMessagingTemplate messagingTemplate;

    public ProgressNotifierService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendProgress(Long applicationId, String status, String message) {
        String destination = "/topic/applications/" + applicationId;
        ProgressUpdate update = new ProgressUpdate(status, message);
        log.info("Sending progress to {}: {}", destination, message);
        messagingTemplate.convertAndSend(destination, update);
    }

    public record ProgressUpdate(String status, String message) {}
}