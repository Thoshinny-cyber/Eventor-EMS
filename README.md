# 📘 Eventor – Event Management System

Eventor is a full-stack event management platform built using **React (Vite)**, **Spring Boot 3**, **MySQL**, **Kafka**, and containerized with **Docker Compose**.  
The frontend is served using **Nginx**, while the backend handles REST APIs, event-driven workflows, PDF ticket generation, and email notifications.

## 📂 Project Structure

```
.
├── .idea/
├── Data-Dump/                     # SQL dump files (database export)
├── frontend/                      # React + Vite frontend
├── src/                           # Spring Boot backend source
├── target/                        # Compiled Spring Boot JAR
├── uploads/                       # Uploaded files (tickets, images)
├── app                            # Spring Boot entry file
├── docker-compose.yaml            # Multi-service orchestration
├── dockerfile                     # Backend Dockerfile
├── nginx.conf                     # Nginx config for frontend hosting
├── mvnw / mvnw.cmd                # Maven wrapper
├── Eventor-Balakrishnan-Thoshinny.pdf
└── pom.xml                        # Maven dependencies
```

## 🔐 Environment Variables & Git Ignore

Add these to **.gitignore**:

```
# Environment files
.env
*.env
frontend/.env
backend/.env
```

### Frontend (`frontend/.env`)
```
REACT_APP_API_BASE_URL=http://backend:8080
```

### Backend (`.env`)
```
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/eventregistration
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
SPRING_MAIL_USERNAME=your_email
SPRING_MAIL_PASSWORD=your_app_password
```

⚠️ **Never commit `.env` files to GitHub.**

## 🐳 Docker Deployment

### Build & Start All Services
```bash
docker-compose up --build -d
```

### Stop Services
```bash
docker-compose down
```

### Stop & Remove Volumes
```bash
docker-compose down -v
```

### Services & Ports

| Service | Port | Purpose |
|--------|------|----------|
| Frontend (Nginx) | 3000 | React UI |
| Backend (Spring Boot) | 8080 | REST API |
| Kafka | 9092 | Messaging |
| Zookeeper | 2181 | Kafka dependency |
| MySQL | 3306 | Database |

## 🗄️ Import SQL Dump (MySQL Workbench)

1. Open *MySQL Workbench*  
2. Go to **Server → Data Import**  
3. Select the folder:
   ```
   Data-Dump/
   ```
4. Choose *Import from Self-Contained File*  
5. Start the import  
6. Verify tables inside `eventregistration` database  

## 🚀 Running Without Docker

### Backend
```bash
./mvnw spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm run dev
```

## 🌐 Access URLs

| Component | URL |
|----------|-----|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8080/api/ |
| MySQL | localhost:3306 |

## ✨ Features

- User & Vendor authentication  
- Event creation and management  
- Ticket booking workflow  
- Stripe-powered payments  
- PDF ticket generation  
- Email notifications  
- Kafka-based asynchronous event handling  
- Fully containerized microservices  
- Nginx-hosted production-ready UI  

## 📦 Technology Stack

- **Frontend**: React.js, Vite, Axios  
- **Backend**: Spring Boot 3, Spring Security, Hibernate, JavaMail  
- **Messaging**: Apache Kafka  
- **Database**: MySQL 8.0  
- **Payments**: Stripe API  
- **Deployment**: Docker, Docker Compose, Nginx  

## 🧑‍💻 Author

**Balakrishnan Thoshinny**  
Event Management System – Full-Stack Implementation
