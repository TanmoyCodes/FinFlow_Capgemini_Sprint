# 💰 FinFlow – Microservices-Based Loan Processing System

## 📌 Overview

FinFlow is a scalable backend system built using **microservices architecture** to handle loan applications, authentication, and administrative operations efficiently.

The system is designed with a strong focus on **security, modularity, and real-world production practices**, making it a solid representation of enterprise-level backend development.

---

## 🏗️ Architecture

The system follows a **microservices architecture** with a centralized API Gateway.

```
Client → API Gateway → Microservices → Database
```

### Core Components:

* API Gateway (central entry point)
* Authentication Service
* Application Service
* Admin Service
* Eureka Server (Service Discovery)
* Config Server (Centralized Configuration)

---

## 🔄 Request Flow

1. Client sends a request (Frontend / Postman)
2. API Gateway intercepts and validates JWT
3. Request is routed to the appropriate microservice
4. Controller handles the request
5. Service layer processes business logic
6. Repository interacts with database
7. Response is returned via API Gateway

---

## 🔐 Security

* JWT-based authentication
* Role-based authorization using Spring Security
* Secure API access via Authorization headers
* Stateless session management

---

## ⚙️ Key Features

* 🔐 Secure authentication using JWT
* 🚪 API Gateway for routing and filtering
* 📡 Service discovery with Eureka
* ⚙️ Centralized configuration using Config Server
* 🔄 Asynchronous communication using RabbitMQ
* 🧪 Unit testing with JUnit & Mockito
* 🐳 Containerization using Docker
* 📘 API documentation with Swagger

---

## 🧱 Tech Stack

### Backend

* Java
* Spring Boot
* Spring Security
* Spring Cloud (Gateway, Eureka, Config Server)

### Communication

* REST APIs
* RabbitMQ (asynchronous messaging)

### Database

* MySQL / PostgreSQL

### DevOps & Tools

* Docker
* Maven
* GitHub

### Testing

* JUnit
* Mockito

---

## 🧪 Testing

The project includes:

* Unit testing using JUnit
* Mocking dependencies with Mockito
* Isolated service layer testing

---

## ⚠️ Error Handling

Centralized error handling ensures:

* Consistent API responses
* Clean controller logic
* Better debugging and traceability

---

## 🐳 Running the Project

```bash
docker-compose up --build
```

---

## 📘 Documentation

Detailed system flow and architecture are available in:

```
/docs/architecture.md
```

---

## 🎯 Why This Project?

This project demonstrates:

* Real-world **microservices architecture**
* Clean **layered backend design**
* Strong **security implementation**
* Scalable and maintainable system design

It is ideal for:

* Backend developer portfolios
* System design discussions
* Interview preparation (SDE roles)

---

## 👨‍💻 Author

**Tanmoy Debnath**

---
