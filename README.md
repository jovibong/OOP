# OOP

Appointment and Queue Management System for SingHealth Clinics

## Fullstack Project: Vite + React Frontend & Spring Boot Backend

This repository contains a **fullstack web application** with:

- **Frontend**: [Vite](https://vitejs.dev/) + React
- **Backend**: [Spring Boot](https://spring.io/projects/spring-boot)

## 📂 Folder Structure

```
├── vite-react-frontend/ # React frontend (Vite)
└── springboot-backend/  # Spring Boot backend (Java)
```

Refer to [ARCHITECTURE.md](./ARCHITECTURE.md) to understand more on the layered architecture pattern.

## 🚀 Getting Started

### Prerequisites

- **Frontend**: Node.js (v18+) and npm installed → [Download Node.js](https://nodejs.org/)
- **Backend**: JDK 17+ installed  
  (Maven **not required**, the project uses Maven Wrapper: `mvnw` / `mvnw.cmd`)

---

### ▶️ Running the Frontend

```bash
cd vite-react-frontend
npm install
npm run dev
```

### ▶️ Running the Backend

```bash
cd springboot-backend
# macOS/Linux
./mvnw spring-boot:run

# Windows PowerShell
.\mvnw.cmd spring-boot:run
```
