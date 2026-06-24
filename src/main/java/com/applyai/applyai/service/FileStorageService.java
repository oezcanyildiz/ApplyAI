package com.applyai.applyai.service;

import com.applyai.applyai.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadPath;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!");
        }
    }

    // Datei speichern - gibt den Pfad zurück
    public String saveFile(MultipartFile file, Long userId) {
        
        // 1. Checks ZUERST - bevor wir irgendwas auf dem Server machen
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty!");
        }
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new BadRequestException("File size must be less than 10MB");
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isBlank()) {
            throw new BadRequestException("File name cannot be empty");
        }
        String extension = originalFileName.contains(".")
                ? originalFileName.substring(originalFileName.lastIndexOf("."))
                : "";
        String uniqueFileName = UUID.randomUUID() + extension;


        // 2. Erst DANN Ordner erstellen und Datei speichern
        Path userDir = uploadPath.resolve("user-" + userId);
        try {
            Files.createDirectories(userDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create user directory!");
        }

        Path targetPath = userDir.resolve(uniqueFileName);
        try {
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Could not save file!");
        }
        return targetPath.toString();
    }

    // Datei laden - gibt Resource zurück
    public Resource loadFile(String filePath) {
        try {
            Path path = Paths.get(filePath).normalize();
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new BadRequestException("File not found: " + filePath);
            }
        } catch (MalformedURLException e) {
            throw new BadRequestException("File not found: " + filePath);
        }
    }

    // Datei löschen
    public void deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath).normalize();
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file!");
        }
    }
}