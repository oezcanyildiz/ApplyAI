# 🚀 ApplyAI

> **AI-powered job application generator & tracker**  
> Built with Java 21 · Spring Boot 3.4 · PostgreSQL · Kubernetes · Claude AI

[![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4-brightgreen?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=flat-square&logo=postgresql)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-ready-2496ED?style=flat-square&logo=docker)](https://www.docker.com/)
[![Kubernetes](https://img.shields.io/badge/Kubernetes-deployed-326CE5?style=flat-square&logo=kubernetes)](https://kubernetes.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)](LICENSE)

---

## 💡 Why ApplyAI?

After **150+ job applications** and countless hours manually tailoring CVs and cover letters, I built the tool I wished existed.

**ApplyAI** takes your resume, reads the job posting, and generates a fully tailored, professional application package in seconds — powered by Claude AI.

---

## ✨ Features

### 📄 AI Application Generator
- Upload your resume (PDF)
- Paste a job posting or provide a link
- Claude AI analyzes the job requirements and tailors your resume
- Generates a personalized cover letter
- Downloads a complete, professional application package as PDF
- **Live progress updates via WebSocket**

### 📊 Application Tracker
- Every generated application is automatically saved
- Track status: `Applied` → `Interview` → `Offer / Rejected`
- Manage contacts, dates, and documents in one place
- Dashboard with statistics and insights
- Real-time notifications via WebSocket

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| **Backend** | Java 21, Spring Boot 3.4 |
| **Security** | Spring Security, JWT, RBAC |
| **Database** | PostgreSQL 16, Hibernate/JPA |
| **Real-time** | WebSocket (STOMP) |
| **AI** | Claude API (Anthropic) |
| **PDF** | Apache PDFBox, iText |
| **Frontend** | React, TypeScript, Tailwind CSS |
| **DevOps** | Docker, Docker Compose |
| **Orchestration** | Kubernetes (K8s) |
| **Cloud** | AWS / GCP |
| **CI/CD** | GitHub Actions |

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────┐
│                  API Gateway                     │
└────────────────────┬────────────────────────────┘
                     │
        ┌────────────┼────────────┐
        │            │            │
   ┌────▼────┐  ┌────▼────┐  ┌───▼─────┐
   │  Auth   │  │Document │  │  AI     │
   │ Service │  │ Service │  │ Service │
   └─────────┘  └────┬────┘  └───┬─────┘
                     │           │
                ┌────▼───────────▼────┐
                │    PDF Generator    │
                └─────────────────────┘
                     │
              ┌──────▼──────┐
              │   Tracker   │
              │   Service   │
              └─────────────┘
```

---

## 🚀 Getting Started

### Prerequisites
- Java 21+
- Docker & Docker Compose
- Maven 3.9+

### Run locally

```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/applyai.git
cd applyai

# Start PostgreSQL with Docker
docker-compose up -d postgres

# Run the application
./mvnw spring-boot:run
```

### Run with Docker Compose (full stack)

```bash
docker-compose up --build
```

The application will be available at `http://localhost:8080`

---

## 📁 Project Structure

```
applyai/
├── auth-service/          # Authentication & JWT
├── document-service/      # PDF upload & text extraction
├── ai-service/            # Claude API integration
├── pdf-generator/         # Application package generation
├── tracker-service/       # Application tracking & notifications
├── api-gateway/           # Routing & load balancing
├── frontend/              # React + TypeScript UI
├── k8s/                   # Kubernetes manifests
├── .github/workflows/     # CI/CD pipelines
└── docker-compose.yml
```

---

## 📈 Development Progress

> 🔨 Currently in active development — built in public!

| Week | Goal | Status |
|------|------|--------|
| Week 1 | Project setup, Auth Service, CI/CD | 🔨 In Progress |
| Week 2 | Document Service, Claude AI Integration | ⏳ Planned |
| Week 3 | PDF Generator, Tracker Service, Frontend | ⏳ Planned |
| Week 4 | Kubernetes Deployment, Cloud, Polish | ⏳ Planned |

Follow my daily progress on [LinkedIn](https://www.linkedin.com/in/YOUR_PROFILE) 📣

---

## 🤝 Contributing

This is a personal portfolio project, but feedback and suggestions are always welcome!  
Feel free to open an issue or reach out on LinkedIn.

---

## 👨‍💻 Author

**Ozcan Yildiz** — Junior Backend Developer  
📧 oezcan.yildiz95@gmail.com  
🔗 [LinkedIn](https://www.linkedin.com/in/YOUR_PROFILE)  
🌐 [boardly.one](https://boardly.one)

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

---

<p align="center">
  <i>Built with ❤️ and 150 rejections.</i>
</p>


---

## 🇩🇪 Deutsche Version

# 🚀 ApplyAI

> **KI-gestützter Bewerbungsmappe-Generator & Bewerbungs-Tracker**  
> Entwickelt mit Java 21 · Spring Boot 3.4 · PostgreSQL · Kubernetes · Claude AI

---

## 💡 Warum ApplyAI?

Nach über **150 Bewerbungen** und unzähligen Stunden damit, Lebensläufe und 
Anschreiben manuell anzupassen, habe ich das Tool gebaut, das ich mir gewünscht hätte.

**ApplyAI** liest deinen Lebenslauf, analysiert die Stellenanzeige und erstellt 
in Sekunden eine vollständig maßgeschneiderte Bewerbungsmappe – powered by Claude AI.

---

## ✨ Funktionen

### 📄 KI Bewerbungsmappe Generator
- Lebenslauf (PDF) hochladen
- Stellenanzeige einfügen oder Link angeben
- Claude AI analysiert die Anforderungen und passt deinen Lebenslauf an
- Generiert ein personalisiertes Anschreiben
- Lädt eine komplette, professionelle Bewerbungsmappe als PDF herunter
- **Live-Fortschritt via WebSocket**

### 📊 Bewerbungs-Tracker
- Jede generierte Bewerbung wird automatisch gespeichert
- Status verfolgen: `Beworben` → `Gespräch` → `Zusage / Absage`
- Ansprechpartner, Termine und Dokumente an einem Ort
- Dashboard mit Statistiken
- Echtzeit-Benachrichtigungen via WebSocket

---

## 👨‍💻 Autor

**Ozcan Yildiz** — Junior Backend-Entwickler  
📧 oezcan.yildiz95@gmail.com  
🔗 [LinkedIn](https://www.linkedin.com/in/YOUR_PROFILE)  
🌐 [boardly.one](https://boardly.one)

---

<p align="center">
  <i>Entwickelt mit ❤️ und 150 Absagen.</i>
</p>
