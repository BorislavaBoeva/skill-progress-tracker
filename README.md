# skill-progress-tracker

Skill Progress Tracker
A full-stack web application built with Spring Boot that helps users track their personal growth and skill development
across four key life domains. By logging time spent on various activities, users earn points and level up across
each skill category.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Error Handling & Validation](#error-handling--validation)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Application Pages](#application-pages)
- [Planned Features](#planned-features)

---

## Overview

Skill Progress Tracker is a Spring Boot MVC web application where registered users can:

- Create and manage **activities** grouped by skill category
- **Log hours** spent on each activity
- Automatically accumulate **points** and **level up** in each skill category
- View their full **progress dashboard**
- Export their data as a **CSV file** for future reference - planned-features

---

## Features

- **User Registration & Login** — Secure account creation with BCrypt password hashing and session management.
- **Profile Management** — View and update personal details (first name, last name, email, profile picture URL).
- **Role-Based Access Control** — Two roles, `USER` and `ADMIN`, with admin-only endpoints protected by server-side
  role checks.
- **Admin User Management** — Admins can view all registered users, **deactivate or reactivate** any user's account
  (blocking or restoring login access without losing their data), and **permanently delete** a user account along
  with their related activities and progress logs.
- **Four Skill Categories** — `Education`, `Physical`, `Hobby`, `Professional`, seeded automatically on first startup.
- **Activity Management** — Create and delete personal activities within each category.
- **Skill Progress Logging** — Log hours spent on any activity with a short description. 1 hour = 1 point.
- **Automatic Level Progression** — Category levels update automatically when point thresholds are crossed (
  `BEGINNER → INTERMEDIATE → ADVANCED → MASTER`).
- **Progress Dashboard** — Full overview of all category levels, points, and logged history per category.
- **Form Validation** — Full server-side validation with user-friendly error messages shown on form resubmission.

---

## Tech Stack

| Layer       | Technology                                 |
|-------------|--------------------------------------------|
| Language    | Java 21                                    |
| Framework   | Spring Boot 4.0.6                          |
| Web         | Spring MVC (Thymeleaf templates)           |
| Persistence | Spring Data JPA / Hibernate                |
| Database    | MySQL 8+                                   |
| Security    | Spring Security Crypto (BCrypt)            |
| Validation  | Jakarta Bean Validation                    |
| Boilerplate | Lombok                                     |
| Build       | Apache Maven (Maven Wrapper included)      |
| Dev Tooling | Spring Boot DevTools, Spring Boot Actuator |

---

--- 
## Error Handling & Validation
The application includes full server‑side validation on all forms using Jakarta Bean Validation (@Valid).
Invalid submissions automatically redisplay the form with field‑specific error messages using Thymeleaf (th:errors).

All business rules are enforced in the service layer through custom exceptions such as:

DuplicateResourceException

InvalidCredentialsException

UnauthorizedActionException

EntityNotFoundException

Controllers catch these exceptions and return the appropriate page with a user‑friendly error message, ensuring the application remains stable and avoids Whitelabel Error Pages.

---

## Project Structure

```text
src/main/java/app/
├── Application.java
├── config/
│   └── BeanConfiguration.java       # Bean definitions (e.g. PasswordEncoder)
├── init/
│   └── CategorySeeder.java          # Seeds the 4 categories on startup
├── model/
│   ├── entity/                      # JPA entities: User, Activity, Category, SkillProgress
│   ├── dto/                         # Request / response DTOs
│   └── mapper/                      # Static mapper classes (entity <-> DTO)
├── repository/                      # Spring Data JPA repositories
├── service/                         # Business logic services
└── web/                             # Spring MVC controllers

src/main/resources/
├── application.properties
├── static/
│   ├── css/                         # Per-page stylesheets
│   └── images/                      # UI assets
└── templates/                       # Thymeleaf HTML templates
```

```
```

---

## Getting Started

**Prerequisites**

- Java 21 or later
- MySQL 8+ running locally
- Maven (or use the included `mvnw` wrapper)

**Steps**

1. Clone the repository
   ```bash
   git clone <repository-url>
   cd skill-progress-tracker
   ```

2. Configure the database — see [Configuration](#configuration) below.

3. Run the application
   ```bash
   ./mvnw spring-boot:run
   # or on Windows
   mvnw.cmd spring-boot:run
   ```

4. Open in your browser
   ```
   http://localhost:8080
   ```

The database schema is created automatically by Hibernate on the first launch, and the four skill categories are seeded
immediately.

---

## Configuration

Edit `src/main/resources/application.properties`:

```properties
# Database connection
spring.datasource.url=jdbc:mysql://localhost:3306/skill_progress_tracker?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
# Schema management (use 'validate' or 'none' in production)
spring.jpa.hibernate.ddl-auto=update
```

```
```

---

## Application Pages

| URL                        | Description                                                           |
|----------------------------|-----------------------------------------------------------------------|
| `/`                        | Landing / index page                                                  |
| `/register`                | New user registration form                                            |
| `/login`                   | Login form                                                            |
| `/home`                    | Dashboard – displays user stats and skill category levels             |
| `/users/profile`           | View and edit user profile                                            |
| `/users/progress`          | Full progress report across all four categories                       |
| `/users/logout`            | Invalidates session and redirects to landing                          |
| `/admin/users`             | **Admin-only** — view and manage all registered users                 |
| `/admin/users/{id}/status` | **Admin-only** — toggle a user's active/disabled status (PUT)         |
| `/admin/users/delete`      | **Admin-only** — permanently delete a user account and all their data |
| `/category/{name}`         | Category detail page (education / physical / hobby / professional)    |
| `/activity/add`            | Add a new personal activity to a category                             |
| `/activity/delete`         | Delete a personal activity                                            |
| `/activity/select`         | Select an activity before logging progress                            |
| `/activity/log`            | Submit an hours log entry for the selected activity                   |

---

## Planned Features

- **CSV Export** — Export personal progress data as a CSV file for future reference.