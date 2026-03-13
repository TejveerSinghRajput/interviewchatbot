# 🤖 Mock AI Interview ChatBot 1.0
An AI-powered mock interview platform built with Java 21 + Spring Boot + Spring AI + Ollama that simulates real interview sessions with adaptive scoring and voice/text evaluation.

# ✨ Features
🎯 Domain-based question generation — Java, Python, Frontend, HR rounds
🎙️ Voice + Text evaluation — Answer via microphone or text input
🤖 AI-powered scoring — Adaptive scoring using Ollama Phi-3 model
🔐 Secure authentication — BCrypt password hashing with daily attempt limits
📄 Interview reports — Final PDF/JSON report with scores, strengths and weaknesses
🐳 Fully Dockerized — One command setup with Docker Compose

# 🛠️ Tech Stack
Layer          -       Technology
------------------------------
Language       -       Java 21
Framework      -       Spring Boot, Spring AI
AI Model       -       Ollama (Phi-3)
Speech-to-Text -       Faster-Whisper
Database       -       MySQL 8.0
Containerization -     Docker, Docker Compose
API Docs       -       Swagger (OpenAPI)
Build Tool     -       Maven

# 🚀 Quick Start
Prerequisites
1. Docker Desktop installed
2. Java 21
3. Maven

# Step 1 — Generate JAR
mvn clean package -DskipTests

# Step 2 — Build and Run
docker-compose up --build -d

# Step 3 — Open Swagger UI
http://localhost:8080/swagger-ui/index.html

# 🔧 Useful Commands
# Start all services
docker-compose up

# Stop all services
docker-compose down

# Rebuild after code changes
mvn clean package -DskipTests
docker-compose up --build -d

# Check running containers
docker ps

# View application logs
docker logs -f interviewchatbot-app

# Restart application only
docker-compose restart app

# Author
Tejveer Singh
Senior Software Engineer | Java | Spring Boot | Microservices | Fintech
LinkedIn profile - https://www.linkedin.com/in/tejveersinghrajput/

# For developer notes or more commands
Go to the note directory after checking out the project

⭐ If you find this useful, please star the repository!
