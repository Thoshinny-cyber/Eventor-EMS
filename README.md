📘 Eventor – Event Management System

Eventor is a full-stack event management platform built using React (Vite), Spring Boot 3, MySQL, Kafka, and containerized with Docker Compose.
The frontend is served via Nginx, while the backend handles API logic, event-driven messaging, and database operations.

📂 Project Structure
.
├── .idea/
├── Data-Dump/                # SQL dump files (database export)
├── frontend/                 # React + Vite frontend source
├── src/                      # Spring Boot backend source
├── target/                   # Compiled Spring Boot JAR
├── uploads/                  # Uploaded assets (images/PDFs)
├── app                       # Spring Boot application entry file
├── docker-compose.yaml       # Multi-service deployment file
├── dockerfile                # Backend Dockerfile
├── nginx.conf                # Frontend Nginx configuration
├── mvnw / mvnw.cmd           # Maven wrapper
├── Eventor-Balakrishnan-Thoshinny.pdf  # Project report
└── pom.xml                   # Maven dependencies

🔐 Environment Variables & Git Ignore

Add environment files to .gitignore:

# Environment files
.env
*.env
frontend/.env
backend/.env

Frontend (frontend/.env)
REACT_APP_API_BASE_URL=http://backend:8080

Backend (.env)
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/eventregistration
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
SPRING_MAIL_USERNAME=your_email
SPRING_MAIL_PASSWORD=your_app_password


⚠️ Never commit your .env files to GitHub.

🐳 Docker Deployment
Build and Start All Services
docker-compose up --build -d


This starts the following:

Service	Port	Description
MySQL	3306	Database
Zookeeper	2181	Kafka dependency
Kafka	9092	Event broker
Backend	8080	Spring Boot API
Frontend	3000	Nginx-hosted React UI
Stop Containers
docker-compose down

Stop + Remove Volumes
docker-compose down -v

🗄️ Database Import (MySQL Workbench)

To import your SQL dump:

Open MySQL Workbench

Navigate: Server → Data Import

Select the folder:

Data-Dump/


Click Start Import

Verify imported tables in eventregistration database.

🚀 Running Locally (Without Docker)
Backend
./mvnw spring-boot:run

Frontend
cd frontend
npm install
npm run dev

🌐 Access URLs
Component	URL
Frontend	http://localhost:3000

Backend API	http://localhost:8080/api/
**
MySQL DB	localhost:3306
✨ Features

User & Vendor authentication

Event creation and management

Ticket booking + Stripe payments

PDF ticket generation + Email notifications

Kafka-based event-driven messaging

Frontend served through Nginx

Fully containerized microservices

📦 Technology Stack

Frontend: React.js, Vite, Axios

Backend: Spring Boot 3, Spring Security, Hibernate

Messaging: Apache Kafka

Database: MySQL 8.0

Payments: Stripe API

Deployment: Docker, Docker Compose, Nginx
