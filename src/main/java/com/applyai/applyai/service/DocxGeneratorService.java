package com.applyai.applyai.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.applyai.applyai.exception.BadRequestException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class DocxGeneratorService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public GeneratedDocument generateDocx(String content, String title, Long userId) {
        log.info("Generating DOCX for userId: {}, title: {}", userId, title);

        try (XWPFDocument document = new XWPFDocument()) {

            // Titel hinzufügen
            XWPFParagraph titleParagraph = document.createParagraph();
            XWPFRun titleRun = titleParagraph.createRun();
            titleRun.setText(title);
            titleRun.setBold(true);
            titleRun.setFontSize(16);
            titleRun.addBreak();

            // Inhalt - jede Zeile als eigenen Absatz
            String[] lines = content.split("\n");
            for (String line : lines) {
                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                run.setText(line);
                run.setFontSize(11);
            }

            // Ordner für User erstellen
            Path userDir = Paths.get(uploadDir, "user-" + userId).toAbsolutePath().normalize();
            Files.createDirectories(userDir);

            // Datei speichern
            String fileName = UUID.randomUUID() + "-" + title.replaceAll("\\s+", "_") + ".docx";
            Path targetPath = userDir.resolve(fileName);

            try (FileOutputStream out = new FileOutputStream(targetPath.toFile())) {
                document.write(out);
            }

            log.info("DOCX generated successfully: {}", targetPath);
            return new GeneratedDocument( targetPath.toString() ,Files.size(targetPath) );

        } catch (IOException e) {
            log.error("Failed to generate DOCX", e);
            throw new BadRequestException("Could not generate document!");
        }
    }
}