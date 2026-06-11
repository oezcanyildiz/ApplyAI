package com.applyai.applyai.entity;

import com.applyai.applyai.enums.DocumentType;
import com.applyai.applyai.enums.FileFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "documents")
public class DocumentEntity extends BaseEntity {

      @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;      // "lebenslauf.pdf"

    @Column(nullable = false)
    private String filePath;      // "/uploads/user123/lebenslauf.pdf"

    private Long fileSize;        // Dateigröße in Bytes

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType documentType;  // Was ist es?

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileFormat fileFormat;      // Welches Format?

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private ApplicationEntity application; // optional, wenn zu Bewerbung gehört
}

