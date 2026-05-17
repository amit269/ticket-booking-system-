# Ticket Booking System

## Project Overview

Microservice-based ticket booking system built using Java and Spring Boot.

The project is being developed using a scalable microservice architecture where different services handle different business functionalities independently.

Currently, the project contains the User Service responsible for authentication and user management.

---

## Architecture

Current Architecture:

Client  
↓  
User Service  
↓  
MySQL Database  

Future Planned Services:
- Booking Service
- Payment Service
- Notification Service
- API Gateway
- Service Discovery

---

## Current Services

### user-service

Responsible for:
- User registration
- User authentication
- Password encryption
- Profile management

---

## Features

- User Registration API
- JWT-based Authentication
- Password Encryption using Spring Security
- Exception Handling
- Profile-based Configuration
- Unit Testing using JUnit and Mockito
- Layered Architecture Implementation

---

## Tech Stack

- Java
- Spring Boot
- Maven
- MySQL
- JUnit 5
- Mockito
- Git & GitHub

---

## Profiles

The project currently supports multiple environment configurations:

- local
- prod

Used for environment-specific application properties.

---

## Testing

Unit testing implemented using:
- JUnit 5
- Mockito

Test cases are written for service layer business logic.

---

## How to Run

1. Clone the repository
2. Configure MySQL database
3. Update application-local.properties
4. Run the user-service application
5. Access APIs using Postman

---

## Future Enhancements

- Booking Service
- Payment Service
- Notification Service
- API Gateway
- Service Discovery
- Frontend Integration
- Docker Support
- Cloud Deployment
