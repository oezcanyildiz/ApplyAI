# 🚀 ApplyAI

> **KI-gestützter Bewerbungsmappe-Generator**  
> Java 21 · Spring Boot 3.5 · PostgreSQL · Claude AI · Docker

[![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-brightgreen?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=flat-square&logo=postgresql)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-ready-2496ED?style=flat-square&logo=docker)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)](LICENSE)

---

## 💡 Warum ApplyAI?

Nach über **150 Bewerbungen** und unzähligen Stunden damit, Lebensläufe und Anschreiben manuell anzupassen, habe ich das Tool gebaut, das ich mir gewünscht hätte.

**ApplyAI** liest deinen Lebenslauf (PDF), analysiert die Stellenanzeige und erstellt in unter 20 Sekunden eine maßgeschneiderte Bewerbungsmappe als Word-Dokument — powered by Claude AI.

> ⚠️ **Hinweis:** ApplyAI ist ein **Backend-fokussiertes Portfolio-Projekt**. Es gibt bewusst kein eigenes Frontend — ich bin Backend-Entwickler und möchte das ehrlich zeigen. Die API ist vollständig über **Swagger/OpenAPI** dokumentiert und testbar.

---

## ✨ Was ApplyAI kann

- 📄 **Lebenslauf hochladen** (PDF oder Word)
- 📋 **Stellenanzeige einfügen** (Freitext)
- 🤖 **Claude AI** analysiert beide Dokumente und optimiert den Lebenslauf für die Stelle
- 📝 **Maßgeschneidertes Anschreiben** wird automatisch generiert
- 📥 **Download als .docx** — direkt einsendefertig
- 📡 **Live-Fortschritt via WebSocket** — kein stilles Warten während die KI arbeitet
- 🔒 **Rate-Limiting** — max. 5 Generierungen pro Stunde pro User (Kostenschutz)

---

## 📊 Messbare Werte

| Metrik | Wert |
|--------|------|
| Generierungszeit | ~20 Sekunden |
| Kosten pro Generierung | ~0,01 € (Claude API) |
| Rate-Limit | 5 Anfragen / Stunde / User |
| API Endpoints | vollständig via Swagger dokumentiert |

---

## 🛠️ Tech Stack

| Bereich | Technologie |
|---------|------------|
| **Backend** | Java 21, Spring Boot 3.5 |
| **Security** | Spring Security, JWT, RBAC |
| **Datenbank** | PostgreSQL 16, Hibernate/JPA |
| **KI** | Claude API (Anthropic) |
| **PDF** | Apache PDFBox 3.x (Textextraktion) |
| **DOCX** | Apache POI (Dokumentgenerierung) |
| **Real-time** | WebSocket (STOMP) |
| **Rate-Limiting** | Bucket4j |
| **API-Doku** | Swagger / OpenAPI (springdoc) |
| **DevOps** | Docker, Docker Compose |
| **Deployment** | Kubernetes (geplant), Azure (geplant) |
| **CI/CD** | GitHub Actions (geplant) |

---

## 🏗️ Architektur & Flow

```
POST /api/applications/{id}/generate
        │
        ▼
ApplicationService (orchestriert den Flow)
        │
        ├── 1. PdfExtractorService (PDFBox)
        │       → Text aus Lebenslauf-PDF extrahieren
        │
        ├── 2. AiService (Claude API)
        │       → Prompt bauen + Claude aufrufen
        │       → ~20 Sekunden, ~0,01€ pro Anfrage
        │
        ├── 3. ContentParserService
        │       → [OPTIMIERTER_LEBENSLAUF] und [ANSCHREIBEN] aus KI-Antwort extrahieren
        │
        ├── 4. DocxGeneratorService (Apache POI)
        │       → Zwei .docx Dateien erstellen
        │
        └── 5. Document Entities in DB speichern
                → User kann Dokumente herunterladen
```

> Alle Schritte laufen **asynchron** (`@Async`) und senden **Live-Updates** via WebSocket (STOMP) an den Client.

---

## 🚀 Lokal starten

### Voraussetzungen
- Java 21+
- Docker & Docker Compose
- Maven 3.9+
- Claude API Key ([console.anthropic.com](https://console.anthropic.com))

### Setup

```bash
# Repository klonen
git clone https://github.com/oezcanyildiz/ApplyAI.git
cd ApplyAI

# .env Datei erstellen (Vorlage: .env.example)
cp .env.example .env
# CLAUDE_API_KEY, DB-Credentials etc. eintragen

# PostgreSQL starten
docker-compose up -d

# Anwendung starten
./mvnw spring-boot:run
```

### API testen

Swagger UI ist unter folgendem Link erreichbar:

```
http://localhost:8080/swagger-ui.html
```

---

## 📁 Projektstruktur

```
applyai/
├── src/main/java/com/applyai/applyai/
│   ├── controller/        # REST Endpoints
│   ├── service/           # Business-Logik
│   │   ├── impl/          # Service-Implementierungen
│   │   ├── AiService      # Claude API Integration
│   │   ├── PdfExtractorService
│   │   ├── DocxGeneratorService
│   │   ├── ContentParserService
│   │   └── RateLimiterService
│   ├── entity/            # JPA Entities
│   ├── repository/        # Spring Data Repositories
│   ├── security/          # JWT, AuthenticatedUser, Filter
│   ├── config/            # Spring, WebSocket Konfiguration
│   ├── dto/               # Request/Response DTOs
│   ├── mapper/            # Entity ↔ DTO Mapping
│   └── exception/         # Custom Exceptions + GlobalExceptionHandler
├── docker-compose.yml
└── .env.example
```

---

## 📈 Entwicklungsfortschritt

> 🔨 Aktiv in Entwicklung — täglich auf LinkedIn dokumentiert unter [#BuildInPublic](https://www.linkedin.com/in/ozcanyildiz-de/)

| Phase | Inhalt | Status |
|-------|--------|--------|
| Woche 1 | Setup, Auth, Application CRUD, Document Upload | ✅ Fertig |
| Woche 2 | Claude AI Integration, WebSocket, Swagger, Security-Hardening | ✅ Fertig |
| Woche 3 | Kubernetes, Docker Compose, Azure Deployment | 🔨 In Arbeit |
| Woche 4 | CI/CD, Tests, Polish | ⏳ Geplant |

---

## 👨‍💻 Autor

**Ozcan Yildiz** — Junior Backend-Entwickler  
📧 oezcan.yildiz95@gmail.com  
🔗 [LinkedIn](https://www.linkedin.com/in/ozcanyildiz-de/)  
💻 [GitHub](https://github.com/oezcanyildiz)

---

## 📄 Lizenz

MIT License — siehe [LICENSE](LICENSE)

---

<p align="center">
  <i>Entwickelt mit ❤️ und 150 Absagen.</i>
</p>
