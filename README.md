# skill-progress-tracker

Skill Progress Tracker
A full-stack web application built with Spring Boot that helps users track their personal growth and skill development
across four key life domains. By logging time spent on various activities, users earn points and level up across
each skill category.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Error Handling & Validation](#error-handling--validation)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Application Pages](#application-pages)

---

## Overview

Skill Progress Tracker is a Spring Boot MVC web application where registered users can:

- Create and manage **activities** grouped by skill category
- **Log hours** spent on each activity
- Automatically accumulate **points** and **level up** in each skill category
- View their full **progress dashboard**
- **Export their activity log** as a CSV or PDF file, and manage a history of past exports (create, view, edit,
  retry, delete), backed by a dedicated `export-record` microservice
---
## Architecture

```
┌──────────────────┐        Feign Client          ┌──────────────────┐
│  skill-progress- │  ─────────────────────────▶  │  export-record   │
│  tracker         │   POST/GET/PUT/DELETE        │  microservice    │
│  (this app)      │   /api/v1/exportRecord/**    │                  │
│                  │  ◀─────────────────────────  │                  │
└──────────────────┘        JSON responses        └──────────────────┘
        │                                                    │
        ▼                                                    ▼
  skill_progress_tracker DB                            export_record DB
  (MySQL)                                               (MySQL, separate)
```

The application follows a **two-service architecture**:

1. **Skill Progress Tracker (this project)** — The main Spring Boot MVC application handling user accounts, activities,
   skill progress, and the web UI.
2. **Export Record Service** — An external microservice (running on `localhost:8081`) responsible for persisting and
   managing export record metadata. Communication is handled via a **Feign client** (`ExportClient`) with API-key
   authentication (`X-API-Key` header).

The `ExportFileService` in this project handles the actual file generation (CSV/PDF) locally, while the `ExportService`
delegates record CRUD operations to the microservice.

---

## Features

- **User Registration & Login** — Spring Security-managed authentication (form login, BCrypt password hashing via a
  custom `UserDetailsService`), backed by a persistent HTTP session.
- **Profile Management** — View and update personal details (first name, last name, email, profile picture URL).
- **Role-Based Access Control** — Two roles, `USER` and `ADMIN`, enforced by the Spring Security filter chain
  (`hasRole("ADMIN")` on `/admin/**`) and `@PreAuthorize` on selected service methods.
- **Admin User Management** — Admins can view all registered users, **deactivate or reactivate** any user's account
  (blocking or restoring login access without losing their data), and **permanently delete** a user account along
  with their related activities and progress logs.
- **Four Skill Categories** — `Education`, `Physical`, `Hobby`, `Professional`, seeded automatically on first startup.
- **Activity Management** — Create and delete personal activities within each category.
- **Skill Progress Logging** — Log hours spent on any activity with a short description. 1 hour = 1 point.
- **Log History & Editing** — View all individually logged entries and edit their descriptions independently of the
  aggregated progress dashboard.
- **Automatic Level Progression** — Category levels update automatically when point thresholds are crossed (
  `BEGINNER → INTERMEDIATE → ADVANCED → MASTER`).
- **Progress Dashboard** — Full overview of all category levels, points, and logged history per category.
- **Data Export & Export History** — Export the activity log (Activity + Description) as CSV or PDF, view past
  export attempts, edit their metadata, retry a failed export, and delete old records — all backed by the
  `export-record` microservice over a Feign client with API-key authentication.
- **Form Validation** — Full server-side validation with user-friendly error messages shown on form resubmission.

---

## Tech Stack

| Layer          | Technology                                      |
|----------------|-------------------------------------------------|
| Language       | Java 21                                         |
| Framework      | Spring Boot 4.0.6                               |
| Web            | Spring MVC (Thymeleaf templates)                |
| Persistence    | Spring Data JPA / Hibernate                     |
| Database       | MySQL 8+                                        |
| Security       | Spring Security (form login, BCrypt, role-based) |
| Validation     | Jakarta Bean Validation                         |
| HTTP Client    | Spring Cloud OpenFeign                          |
| PDF Generation | Apache PDFBox                                   |
| Boilerplate    | Lombok                                          |
| Build          | Apache Maven (Maven Wrapper included)           |
| Dev Tooling    | Spring Boot DevTools, Spring Boot Actuator      |

---

--- 
## Error Handling & Validation
The application includes full server-side validation on all forms using Jakarta Bean Validation (`@Valid`).
Invalid submissions automatically redisplay the form with field-specific error messages using Thymeleaf (`th:errors`).

All business rules are enforced in the service layer through custom exceptions, including:

- `ApplicationException` — Base application exception
- `DuplicateResourceException`
- `InvalidCredentialsException`
- `AccountDisabledException`
- `UnauthorizedActionException`
- `ExportGenerationException` — Thrown when CSV/PDF file generation fails
- `ExportInProgressException` — Thrown when a duplicate export is requested too quickly
- `ExportNotFoundException` — Thrown when an export record cannot be found
- `EntityNotFoundException` (with `UserNotFoundException`, `ActivityNotFoundException`, and
  `SkillProgressNotFoundException` as subtypes)

A centralized `GlobalExceptionHandler` (`@ControllerAdvice`) catches these exceptions and returns the appropriate
page with a user-friendly error message, ensuring the application remains stable and avoids Whitelabel Error Pages.

---

## Project Structure

```text
src/main/java/app/
├── Application.java
├── config/
│   ├── BeanConfiguration.java          # Bean definitions (e.g. PasswordEncoder)
│   └── SecurityConfiguration.java      # Spring Security filter chain & access rules
├── exception/                          # Custom exception hierarchy + GlobalExceptionHandler
│   ├── ApplicationException.java
│   ├── AccountDisabledException.java
│   ├── ActivityNotFoundException.java
│   ├── DuplicateResourceException.java
│   ├── EntityNotFoundException.java
│   ├── ExportGenerationException.java
│   ├── ExportInProgressException.java
│   ├── ExportNotFoundException.java
│   ├── GlobalExceptionHandler.java
│   ├── InvalidCredentialsException.java
│   ├── SkillProgressNotFoundException.java
│   ├── UnauthorizedActionException.java
│   └── UserNotFoundException.java
├── init/
│   ├── AdminSeeder.java                # Seeds a default admin account on startup
│   └── CategorySeeder.java             # Seeds the 4 categories on startup
├── model/
│   ├── entity/                         # JPA entities: User, Activity, Category, SkillProgress
│   │   ├── activity/Activity.java
│   │   ├── category/Category.java
│   │   ├── skill/SkillProgress.java
│   │   └── user/User.java, UserRole.java, ProgressLevel.java
│   ├── dto/                            # Request / response DTOs
│   │   ├── activity/                   # ActivityDto, ActivityEntryDto, ActivitySelectDto
│   │   ├── category/                   # CategoryProgressDto
│   │   ├── export/                     # ExportCreateRequestDto, ExportUpdateRequestDto,
│   │   │                               # ExportResponseDto, ExportType, ExportStatus
│   │   ├── skill/                      # SkillProgressDto, SkillProgressEditDto, SkillProgressLogDto
│   │   └── user/                       # UserDto, UserEditRequestDto, UserLoginRequestDto,
│   │                                   # UserRegisterRequestDto, UserProgressDto, AuthenticationUserDetails
│   └── mapper/                         # Static mapper classes (entity ↔ DTO)
│       ├── activity/ActivityMapper.java
│       ├── skill/SkillProgressMapper.java
│       └── user/UserMapper.java
├── repository/                         # Spring Data JPA repositories
│   ├── activity/ActivityRepository.java
│   ├── category/CategoryRepository.java
│   ├── skill/SkillProgressRepository.java
│   └── user/UserRepository.java
├── service/                             # Business logic services
│   ├── activity/ActivityService.java
│   ├── category/CategoryService.java
│   ├── export/
│   │   ├── client/ExportClient.java     # Feign client for the export-record microservice
│   │   ├── ExportFileService.java       # CSV & PDF generation logic
│   │   └── ExportService.java           # Export record CRUD via Feign
│   ├── skill/SkillProgressService.java
│   └── user/UserService.java
└── web/                                 # Spring MVC controllers
    ├── IndexController.java             # Landing, register, login, home pages
    ├── activity/ActivityController.java
    ├── category/CategoryController.java
    ├── export/
    │   ├── ExportController.java        # Create, details, history, retry, download, delete
    │   └── ExportUpdateController.java  # Update export record metadata
    └── user/
        ├── AdminController.java         # Admin-only user management
        └── UserController.java          # Profile & progress pages

src/main/resources/
├── application.properties
├── static/
│   ├── css/                             # Per-page stylesheets
│   │   ├── admin.css, category.css, common.css, export.css,
│   │   │   form.css, home.css, log-history.css, profile.css, progress.css
│   └── images/                          # UI assets
└── templates/                           # Thymeleaf HTML templates
    ├── activity/log-history.html
    ├── admin/users.html
    ├── category/education.html, hobby.html, physical.html, professional.html
    ├── error.html
    ├── exportHistory.html
    ├── exportRecord.html
    ├── updateRecords.html
    └── home.html, index.html, login.html, profile.html, progress.html, register.html
```

---

## Getting Started

**Prerequisites**

- Java 21 or later
- MySQL 8+ running locally
- Maven (or use the included `mvnw` wrapper)
- The **export-record microservice** running on `localhost:8081` (required for export features)
- An `API_KEY` environment variable set for authenticating with the export-record service

**Steps**

1. Clone the repository
   ```bash
   git clone <repository-url>
   cd skill-progress-tracker
   ```

2. Configure the database — see [Configuration](#configuration) below.

3. Set the API key environment variable
   ```bash
   export API_KEY=your-secret-api-key
   ```

4. Start the export-record microservice on port `8081`.

5. Run the application
   ```bash
   ./mvnw spring-boot:run
   # or on Windows
   mvnw.cmd spring-boot:run
   ```

6. Open in your browser
   ```
   http://localhost:8080
   ```

The database schema is created automatically by Hibernate on the first launch. The four skill categories and a default
admin account (`admin` / `admin123`) are seeded immediately.

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
Export-record microservice
export-record.service.url=http://localhost:8081/api/v1/exportRecord
export-record.service.api-key=${API_KEY}
```

---

## Application Pages

| URL                                | Method | Description                                                           |
|------------------------------------|--------|-----------------------------------------------------------------------|
| `/`                                | GET    | Landing / index page                                                  |
| `/register`                        | GET    | New user registration form                                            |
| `/register`                        | POST   | Submit registration                                                   |
| `/login`                           | GET    | Login form                                                            |
| `/home`                            | GET    | Dashboard – displays user stats and skill category levels             |
| `/users/profile`                   | GET    | View user profile with edit form                                      |
| `/users/profile`                   | PUT    | Update user profile                                                   |
| `/users/progress`                  | GET    | Full progress report across all four categories                       |
| `/users/logout`                    | POST   | Invalidates session and redirects to landing                          |
| `/admin/users`                     | GET    | **Admin-only** — view and manage all registered users                 |
| `/admin/users/{id}/status`         | PUT    | **Admin-only** — toggle a user's active/disabled status               |
| `/admin/users/delete`              | POST   | **Admin-only** — permanently delete a user account and all their data |
| `/category/{name}`                 | GET    | Category detail page (education / physical / hobby / professional)    |
| `/activity/add`                    | POST   | Add a new personal activity to a category                             |
| `/activity/delete`                 | POST   | Delete a personal activity                                            |
| `/activity/select`                 | POST   | Select an activity before logging progress                            |
| `/activity/log`                    | POST   | Submit an hours log entry for the selected activity                   |
| `/activity/log/history`            | GET    | View all logged entries with inline editing                           |
| `/activity/log/{id}`               | PUT    | Edit a log entry's description                                        |
| `/exportRecord`                    | GET    | Create a new export record (CSV or PDF)                               |
| `/exportRecord/details/{id}`       | GET    | View details of a specific export record                              |
| `/exportRecord/history`            | GET    | View all export records for the current user                          |
| `/exportRecord/retry/{id}`         | PUT    | Retry a failed export record                                          |
| `/exportRecord/download/{id}`      | GET    | Download the generated export file (CSV or PDF)                       |
| `/exportRecord/delete/{id}`        | POST   | Soft-delete an export record                                          |
| `/exportRecord/update/{id}`        | GET    | Show the update form for an export record                             |
| `/exportRecord/update/{id}`        | PUT    | Update export record metadata (file name, description, type)          |

---